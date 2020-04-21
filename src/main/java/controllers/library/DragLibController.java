package controllers.library;

import application.GUI;
import controllers.PanelsController;
import controllers.graphicNode.GraphicNode;
import controllers.graphicNode.GraphicNodeController;
import controllers.object.DragContainer;
import iec61850.LN;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import tools.ArrayMap;
import tools.SaveLoadObject;

import java.io.File;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project MGBuilder
 * @description Класс для создания библиотеки
 */
public class DragLibController {

	private static DragLibController self;
	private GraphicNode shadowNode;
	private final ArrayMap<GraphicNode, GraphicNode> shadowNodes = new ArrayMap<>(); // key = Icon, value = shadowIcon
	private EventHandler<DragEvent> dragEnteredGUI, dragExitedGUI;
	private EventHandler<DragEvent> dragOverGUI, dragOverLib, dragDropped, dragDone;
	private double offsetX, offsetY;
	private final ClipboardContent content = new ClipboardContent(){{put(new DataFormat(), new DragContainer());}};



	public DragLibController(){
		buildDragHandlers();
	}


	public static void addToController(GraphicNode node){
		if(self==null) self = new DragLibController();

		GraphicNode shadowNode = new GraphicNode(node.getUserData());
		shadowNode.setOpacity(0.3);
		self.shadowNodes.put(node, shadowNode);
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

			GUI.get().addEventHandler(DragEvent.DRAG_ENTERED, dragEnteredGUI);  // Добавить теневую иконку если затащили на GUI
			GUI.get().addEventHandler(DragEvent.DRAG_EXITED, dragExitedGUI);  // Удалить теневую иконку если вышли из GUI

			GUI.get().addEventHandler(DragEvent.DRAG_OVER, dragOverGUI); // Перемещение над GUI
			LibraryDialog.get().addEventHandler(DragEvent.DRAG_OVER, dragOverLib); // Перемещение над библиотекой

			GUI.get().addEventHandler(DragEvent.DRAG_DROPPED, dragDropped); // Если иконка брошена в текущую панель
			LibraryDialog.get().addEventHandler(DragEvent.DRAG_DONE, dragDone); // Окончание перемещения

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
			GraphicNode node = GraphicNodeController.createGraphicNode(ln);
			Point2D point = PanelsController.getSelectedPanel().sceneToLocal(e.getSceneX(), e.getSceneY());
			PanelsController.getSelectedPanel().getChildren().add(node);
			node.relocate(point.getX() - offsetX, point.getY() - offsetY); node.updateGrid();
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
