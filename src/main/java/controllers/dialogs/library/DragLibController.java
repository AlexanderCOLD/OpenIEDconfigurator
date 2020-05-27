package controllers.dialogs.library;

import application.GUI;
import controllers.dialogs.AssistantDialog;
import iec61850.*;
import controllers.PanelsController;
import controllers.graphicNode.GraphicNode;
import controllers.graphicNode.GraphicNodeController;
import controllers.object.DragContainer;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import tools.SaveLoadObject;

import java.io.File;
import java.util.HashMap;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project MGBuilder
 * @description Класс для создания библиотеки
 */
public class DragLibController {

	private static DragLibController self;
	private GraphicNode shadowNode;
	private final HashMap<String, GraphicNode> shadowNodes = new HashMap<>(); // key = UID, теневые иконки

	private EventHandler<DragEvent> dragEnteredGUI, dragExitedGUI;
	private EventHandler<DragEvent> dragOverGUI, dragOverLib, dragDropped, dragDone;
	private double offsetX, offsetY;
	private final ClipboardContent content = new ClipboardContent(){{put(new DataFormat(), new DragContainer());}};

	/**
	 * Добавить иконку для перемещений
	 */
	public static void addToController(GraphicNode node){
		if(self==null) self = new DragLibController();

		GraphicNode shadowNode = new GraphicNode(node.getIecObject()); shadowNode.setOpacity(0.3);
		self.shadowNodes.put(node.getId(), shadowNode);
		self.addDragDetection(node);
	}

	/**
	 * Начало перемещения в библиотеке
	 */
	private void addDragDetection(GraphicNode dragNode) {
		/**
		 * Начало перемещения
		 */
		dragNode.setOnDragDetected (e -> {

			offsetX = e.getX(); offsetY = e.getY();
//			if(offsetX > ((GraphicNode)e.getSource()).getWidth() || offsetY > 20){ e.consume(); return; } // Только заголовок

			GUI.get().addEventHandler(DragEvent.DRAG_ENTERED, dragEnteredGUI);     // Добавить теневую иконку если затащили на GUI
			GUI.get().addEventHandler(DragEvent.DRAG_EXITED, dragExitedGUI);       // Удалить теневую иконку если вышли из GUI

			GUI.get().addEventHandler(DragEvent.DRAG_OVER, dragOverGUI);           // Перемещение над GUI
			LibraryDialog.get().addEventHandler(DragEvent.DRAG_OVER, dragOverLib); // Перемещение над библиотекой

			GUI.get().addEventHandler(DragEvent.DRAG_DROPPED, dragDropped);        // Если иконка брошена в текущую панель
			LibraryDialog.get().addEventHandler(DragEvent.DRAG_DONE, dragDone);    // Окончание перемещения

			shadowNode = shadowNodes.get(((GraphicNode) e.getSource()).getId());
			if(!LibraryDialog.get().getChildren().contains(shadowNode)) LibraryDialog.get().getChildren().add(shadowNode);
			shadowNode.startDragAndDrop(TransferMode.COPY).setContent(content);
			shadowNode.relocate(e.getSceneX(), e.getSceneY());
			shadowNode.setMouseTransparent(true);
			shadowNode.toFront();
			e.consume();
		});
	}


	/**
	 * Инициализация обработчиков перемещения
	 */
	{
		/**
		 * Перебрасывание теневой иконки
		 */
		dragEnteredGUI = e->{ if(shadowNode !=null && !GUI.get().getChildren().contains(shadowNode)){ GUI.get().getChildren().add(shadowNode); e.acceptTransferModes(TransferMode.COPY); shadowNode.setVisible(false); } e.consume(); };
		dragExitedGUI = e->{ if(shadowNode !=null && !LibraryDialog.get().getChildren().contains(shadowNode)){ LibraryDialog.get().getChildren().add(shadowNode); e.acceptTransferModes(TransferMode.COPY); shadowNode.setVisible(false); } e.consume(); };

		/**
		 * Перемещение над библиотекой
		 */
		dragOverLib = e -> {
			e.acceptTransferModes(TransferMode.COPY);
			shadowNode.relocate(e.getSceneX() - offsetX, e.getSceneY() - offsetY);
			shadowNode.setVisible(true);
			e.consume();
		};

		/* Перемещение над главным окном GUI */
		dragOverGUI = e -> {
			Point2D point = GUI.get().getTabPane().sceneToLocal(e.getSceneX(), e.getSceneY());

			if((point.getX() > 0 && point.getX() < GUI.get().getTabPane().getWidth()) && (point.getY() > 0 && point.getY() < GUI.get().getTabPane().getHeight()-35)){
				shadowNode.setVisible(true);
				e.acceptTransferModes(TransferMode.COPY);
				shadowNode.relocate(e.getSceneX() - offsetX, e.getSceneY() - offsetY);
			}
			else shadowNode.setVisible(false);
			e.consume();
		};

		/**
		 * Элемент брошен в проект
		 */
		dragDropped = e -> {

			try {
				String name = shadowNode.getIecObject().getType();

				File lib = new File(String.format("library/AddLN/%s.xml",name));
				if(!lib.exists()) lib = new File(String.format("library/LN/%s.xml",name));
				if(!lib.exists()) lib = new File(String.format("library/DS/%s.xml",name));
				if(!lib.exists()){ System.err.println("Library: " + name + " is not found"); return; }

				String objectName = AssistantDialog.requestText("Введите название", "Введите уникальное название элемента", shadowNode.getIecObject().getName());
				if(objectName==null) return;

				/* LD в который помещаем новый объект */
				LD ld = (LD) PanelsController.getSelectedIECObject(); if(ld==null) { System.err.println("LD not found"); return; }

				IECObject iecObject = null;

				/* Если LN, создаем и добавляем в текущий LD */
				if(lib.getPath().contains("LN")){
					LN ln = SaveLoadObject.load(LN.class, lib);
					ld.getLogicalNodeList().add((LN) ln);
					iecObject = ln;
				}
				else if(lib.getPath().contains("DS")){
					DS ds = SaveLoadObject.load(DS.class, lib);
					ld.getDataSets().add(ds);
					iecObject = ds;
				}

				if(iecObject==null) { System.err.println("Error of element creating"); return; }

				/* Задаем тэги дополнительного элемента */
				iecObject.setName(objectName);
				iecObject.getTags().add("additional");
				for(IECObject obj: CLDUtils.objectListOf(iecObject)) obj.getTags().add("additional");

				/* Создание граф. элемента и установка в панель */
				GraphicNode node = GraphicNodeController.createGraphicNode(iecObject);
				GraphicNodeController.getProjectNodeList().put(iecObject.getUID(), node);
				PanelsController.getSelectedPanel().getChildren().add(node);

				Point2D point = PanelsController.getSelectedPanel().sceneToLocal(e.getSceneX(), e.getSceneY());
				node.relocate(point.getX() - offsetX, point.getY() - offsetY); node.updateGrid();

			} catch (Exception exception) { exception.printStackTrace(); }

			e.setDropCompleted(true);
			e.consume();
		};


		/**
		 * Когда перетаскивание закончено - удаляем теневую иконку и удаляем все обработчики
		 */
		dragDone = e -> {
			if(shadowNode.getParent()!=null) ((Pane) shadowNode.getParent()).getChildren().remove(shadowNode);

			GUI.get().removeEventHandler(DragEvent.DRAG_ENTERED, dragEnteredGUI);
			GUI.get().removeEventHandler(DragEvent.DRAG_EXITED, dragExitedGUI);

			GUI.get().removeEventHandler(DragEvent.DRAG_OVER, dragOverGUI);
			LibraryDialog.get().removeEventHandler(DragEvent.DRAG_OVER, dragOverLib);

			LibraryDialog.get().removeEventHandler(DragEvent.DRAG_DONE, dragDone);
			GUI.get().removeEventHandler(DragEvent.DRAG_DROPPED, dragDropped);
			e.consume();
		};

	}

}
