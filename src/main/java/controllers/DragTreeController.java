package controllers;

import application.GUI;
import controllers.dialogs.LibraryDialog;
import controllers.elements.GraphicNode;
import controllers.object.DragContainer;
import iec61850.LN;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import tools.BiHashMap;
import tools.saveload.SaveLoadObject;

import java.io.File;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project MGBuilder
 * @description Класс для создания библиотеки
 */
public class DragTreeController {

	/**
	 * project_pane - панель куда перемещают элемент
	 * library_pane - панель откуда (библиотека) перемещают элемент
	 * rootPane - корневая панель (то над чем его можно перемещать)
	 */

	private static DragTreeController self;
	private GraphicNode shadowNode;
	private BiHashMap<GraphicNode, GraphicNode> shadowNodes = new BiHashMap<>(); // key = Icon, value = shadowIcon
	private EventHandler<DragEvent> dragEnteredGUI, dragExitedGUI;
	private EventHandler<DragEvent> dragOverGUI, dragOverLib, dragDropped, dragDone;
	private double offsetX, offsetY;
	private ClipboardContent content = new ClipboardContent(){{put(new DataFormat(), new DragContainer());}};;

	public DragTreeController(){
		buildDragHandlers();
	}


	public static void addToController(GraphicNode node){
		if(self==null) self = new DragTreeController();

		GraphicNode shadowIcon = new GraphicNode();
		shadowIcon.setOpacity(0.5);
		shadowIcon.setUserData(node.getUserData());
		self.shadowNodes.put(node, shadowIcon);
		self.addDragDetection(node);
	}

boolean added = false;

	/**
	 * Начало перемещения в библиотеке
	 */
	private void addDragDetection(GraphicNode dragNode) {

		/**
		 * Начало перемещения
		 */
		dragNode.setOnDragDetected (e -> {

			GUI.get().addEventFilter(DragEvent.DRAG_ENTERED, dragEnteredGUI);  // Добавить теневую иконку если затащили на GUI
			GUI.get().addEventFilter(DragEvent.DRAG_EXITED, dragExitedGUI);  // Удалить теневую иконку если вышли из GUI

			GUI.get().addEventFilter(DragEvent.DRAG_OVER, dragOverGUI); // Перемещение над GUI
			LibraryDialog.get().addEventFilter(DragEvent.DRAG_OVER, dragOverLib); // Перемещение над библиотекой

			GUI.get().addEventFilter(DragEvent.DRAG_DROPPED, dragDropped); // Если иконка брошена в текущую панель
			LibraryDialog.get().addEventFilter(DragEvent.DRAG_DONE, dragDone); // Окончание перемещения

			offsetX = e.getX(); offsetY = e.getY();
			if(offsetX > ((GraphicNode)e.getSource()).getWidth() || offsetY > 20){ e.consume(); return; } // Только заголовок

			shadowNode = shadowNodes.getValue(e.getSource());
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
	private void buildDragHandlers() {

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
			String name = ((LN) shadowNode.getUserData()).getClassType();
			LN ln = SaveLoadObject.load(LN.class, new File(String.format("library/%s.xml",name)));
			GraphicNode node = PanelsController.createNode(ln);
			Point2D point = PanelsController.getSelectedPanel().sceneToLocal(e.getSceneX(), e.getSceneY());
			node.relocate(point.getX() - offsetX, point.getY() - offsetY);
			e.setDropCompleted(true);
			e.consume();
		};


		/**
		 * Когда перетаскивание закончено - удаляем теневую иконку и удаляем все обработчики
		 */
		dragDone = e -> {
			if(shadowNode.getParent()!=null) ((Pane) shadowNode.getParent()).getChildren().remove(shadowNode);

			GUI.get().removeEventFilter(DragEvent.DRAG_ENTERED, dragEnteredGUI);
			GUI.get().removeEventFilter(DragEvent.DRAG_EXITED, dragExitedGUI);

			GUI.get().removeEventFilter(DragEvent.DRAG_OVER, dragOverGUI);
			LibraryDialog.get().removeEventFilter(DragEvent.DRAG_OVER, dragOverLib);

			LibraryDialog.get().removeEventFilter(DragEvent.DRAG_DONE, dragDone);
			GUI.get().removeEventFilter(DragEvent.DRAG_DROPPED, dragDropped);
			e.consume();
		};

	}

}
