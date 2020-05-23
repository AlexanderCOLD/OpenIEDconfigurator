package controllers.tree;

import application.GUI;
import controllers.PanelsController;
import controllers.graphicNode.GraphicNode;
import controllers.graphicNode.GraphicNodeController;
import controllers.object.DragContainer;
import iec61850.DS;
import iec61850.IECObject;
import iec61850.LN;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

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
	private EventHandler<DragEvent> dragOverGUI, dragDropped, dragDone;
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

				currentGraphicNode = GraphicNodeController.getProjectNodeList().get(((IECObject) object).getUID());
				if(currentGraphicNode.getParent() == null){

					GUI.get().addEventHandler(DragEvent.DRAG_OVER, dragOverGUI);    // Перемещение над GUI
					GUI.get().addEventHandler(DragEvent.DRAG_DROPPED, dragDropped); // Иконка брошена
					GUI.get().addEventHandler(DragEvent.DRAG_DONE, dragDone);       // После перемещения

					GUI.get().getChildren().add(currentGraphicNode);
					currentGraphicNode.startDragAndDrop(TransferMode.ANY).setContent(content);
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
		dragOverGUI = e -> { e.acceptTransferModes(TransferMode.ANY); currentGraphicNode.relocate(e.getSceneX() - 50, e.getSceneY() - 10); e.consume(); };

		/* Элемент брошен */
		dragDropped = e -> {
			removeHandlers();

			Point2D point = GUI.get().getTabPane().sceneToLocal(e.getSceneX(), e.getSceneY());

			/* Если брошен над активной панелью */
			if((point.getX() > 0 && point.getX() < GUI.get().getTabPane().getWidth()) && (point.getY() > 0 && point.getY() < GUI.get().getTabPane().getHeight()-35)){

				Point2D projectPoint = PanelsController.getSelectedPanel().sceneToLocal(e.getSceneX(), e.getSceneY());
				PanelsController.getSelectedPanel().getChildren().add(currentGraphicNode);
				currentGraphicNode.relocate(projectPoint.getX() - 50, projectPoint.getY() - 10);
				currentGraphicNode.setOpacity(1.0);
				currentGraphicNode.toFront();
				currentGraphicNode.updateGrid();
				currentGraphicNode.setMouseTransparent(false);
			}
			currentGraphicNode = null;
			e.setDropCompleted(true);
			e.consume();
		};

		/* После окончания перемещения */
		dragDone = e->{ removeHandlers(); e.consume(); };
	}

	/**
	 * Удалить обработчики после перемещения
	 */
	private void removeHandlers(){
		if(currentGraphicNode!=null) { Pane parent = (Pane) currentGraphicNode.getParent();	if(parent != PanelsController.getSelectedPanel()) parent.getChildren().remove(currentGraphicNode); }
		GUI.get().removeEventHandler(DragEvent.DRAG_OVER, dragOverGUI);
		GUI.get().removeEventHandler(DragEvent.DRAG_DROPPED, dragDropped);
		GUI.get().removeEventHandler(DragEvent.DRAG_DROPPED, dragDone);
	}

	public static DragTreeController get(){ if(self==null) self = new DragTreeController(); return self; }
}
