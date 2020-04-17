package controllers.tree;

import application.GUI;
import controllers.PanelsController;
import controllers.graphicNode.GraphicNode;
import controllers.graphicNode.GraphicNodeController;
import controllers.object.DragContainer;
import iec61850.DS;
import iec61850.LN;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import tools.BiHashMap;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project MGBuilder
 * @description Класс для создания библиотеки
 */
public class DragTreeController {

	private static DragTreeController self;
	private static TreeView tree;
	private GraphicNode currentGraphicNode;
	private final BiHashMap<Object, GraphicNode> graphicNodeList = new BiHashMap<>(); // key = Icon, value = shadowIcon
	private EventHandler<DragEvent> dragOverGUI, dragDroppedToProject, dragDone;
	private final ClipboardContent content = new ClipboardContent(){{put(new DataFormat(), new DragContainer());}};;

	public DragTreeController(){ buildDragHandlers(); }

	/**
	 * Перемещение элементов из дерева в проект
	 * (Элементы лежат в NodeController ProjectNodeList)
	 * @param tree - Дерево проекта
	 */
	public void addTreeDragDetection(TreeView tree) {
		if(DragTreeController.tree != null) return;

		/* Начало перемещения */
		tree.setOnDragDetected (e -> {
			Object object = TreeController.getSelectedItem();
			if(object.getClass()==LN.class || object.getClass()== DS.class){

				currentGraphicNode = GraphicNodeController.getProjectNodeList().getValue(object);
				if(currentGraphicNode.getParent() == null){

					GUI.get().addEventFilter(DragEvent.DRAG_OVER, dragOverGUI); 			// Перемещение над GUI
					GUI.get().addEventFilter(DragEvent.DRAG_DROPPED, dragDroppedToProject); // Если иконка брошена в текущую панель
//					GUI.get().addEventFilter(DragEvent.DRAG_DONE, dragDone); 				// Окончание перемещения

					GUI.get().getChildren().add(currentGraphicNode);
					currentGraphicNode.startDragAndDrop(TransferMode.COPY).setContent(content);
					currentGraphicNode.setOpacity(0.3);
					currentGraphicNode.relocate(e.getSceneX(), e.getSceneY());
					currentGraphicNode.setMouseTransparent(true);
					currentGraphicNode.toFront();
				}
			}
			e.consume();
		});
		DragTreeController.tree = tree;
	}


	/**
	 * Инициализация обработчиков перемещения
	 */
	private void buildDragHandlers() {

		/* Перемещение над главным окном GUI */
		dragOverGUI = e -> {
			Point2D point = GUI.get().getTabPane().sceneToLocal(e.getSceneX(), e.getSceneY());

			if(point.getY() > 0 && point.getY() < GUI.get().getTabPane().getHeight()-35){
				currentGraphicNode.setVisible(true);
				e.acceptTransferModes(TransferMode.COPY);
				currentGraphicNode.relocate(e.getSceneX() - 50, e.getSceneY() - 10);
			}
			else currentGraphicNode.setVisible(false);
			e.consume();
		};

		/* Элемент брошен в проект */
		dragDroppedToProject = e -> {
			Point2D point = GUI.get().getTabPane().sceneToLocal(e.getSceneX(), e.getSceneY());
			currentGraphicNode.setVisible(true);

			/* Если брошен над активной панелью */
			if((point.getX() > 0 && point.getX() < GUI.get().getTabPane().getWidth()) && (point.getY() > 0 && point.getY() < GUI.get().getTabPane().getHeight()-35)){

				Point2D projectPoint = PanelsController.getSelectedPanel().sceneToLocal(e.getSceneX(), e.getSceneY());
				PanelsController.getSelectedPanel().getChildren().add(currentGraphicNode);
				currentGraphicNode.relocate(projectPoint.getX() - 50, projectPoint.getY() - 10);
				currentGraphicNode.setOpacity(1.0);
				currentGraphicNode.toFront();
				currentGraphicNode.updateGrid();
				GraphicNodeController.addHandlers(currentGraphicNode);
			}
			else{ Pane pane = (Pane) currentGraphicNode.getParent(); if(pane != null) pane.getChildren().remove(currentGraphicNode); }

			System.out.println("Ивенты удалены");
			GUI.get().removeEventFilter(DragEvent.DRAG_OVER, dragOverGUI); 			   // Перемещение над GUI
			GUI.get().removeEventFilter(DragEvent.DRAG_DROPPED, dragDroppedToProject); // Если иконка брошена в текущую панель
			GUI.get().removeEventFilter(DragEvent.DRAG_DONE, dragDone); 			   // Окончание перемещения

			currentGraphicNode = null;
			e.setDropCompleted(true);
			e.consume();
		};


		/* Когда перетаскивание закончено - удаляем все обработчики */
		dragDone = e -> {
//			System.out.println("DONE");
//			GUI.get().removeEventFilter(DragEvent.DRAG_OVER, dragOverGUI); 			   // Перемещение над GUI
//			GUI.get().removeEventFilter(DragEvent.DRAG_DROPPED, dragDroppedToProject); // Если иконка брошена в текущую панель
//			GUI.get().removeEventFilter(DragEvent.DRAG_DONE, dragDone); 			   // Окончание перемещения
//			e.consume();
		};

	}


	public static DragTreeController get(){ if(self==null) self = new DragTreeController(); return self; }
}
