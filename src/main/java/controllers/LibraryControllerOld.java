package controllers;

import application.GUI;
import controllers.dialogs.LibraryDialog;
import controllers.elements.GraphicNode;
import controllers.object.DragContainer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.Pane;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project MGBuilder
 * @description Класс для создания библиотеки
 */
public class LibraryControllerOld {

	/**
	 * project_pane - панель куда перемещают элемент
	 * library_pane - панель откуда (библиотека) перемещают элемент
	 * rootPane - корневая панель (то над чем его можно перемещать)
	 */
	private Pane project_pane, library_pane, rootPane;
//	private LibraryShadowNode shadowIcon = null;
	private EventHandler<DragEvent> dragOverRoot = null;
	private EventHandler<DragEvent> dragDroppedOnProject = null;
	private EventHandler<DragEvent> dragOverProjectPane = null;
	private static LibraryControllerOld self;
	private DataFormat tempFormat = new DataFormat();
	private DragContainer tempDC =  new DragContainer();
	private Point2D targetPoint;
//	private EquipmentType targetType;
	private ClipboardContent content = new ClipboardContent(){{put(new DataFormat(), new DragContainer());}};;

	public void initialize(){
		new Thread(() -> Platform.runLater(() -> loadLibrary())).start();
	}

	private void loadLibrary(){

		library_pane = LibraryDialog.getLibraryPane();
		rootPane = GUI.get();

		GraphicNode gn = new GraphicNode();
		library_pane.getChildren().add(gn);
		gn.relocate(10,10);

//		buildDragHandlers();
//		shadowIcon = new LibraryShadowNode();
//		shadowIcon.setVisible(false);
//		shadowIcon.setOpacity(0.5);
//		rootPane.getChildren().add(shadowIcon);

		/**
		 * Добавляем значки на панель библиотеки
		 */
//		for(EquipmentType type: EquipmentType.values()){
//			LibraryNode ln = new LibraryNode(type);
//			addDragDetection(ln);
//			library_pane.getChildren().add(ln);
//		}
	}


	/**
	 * Начало перемещения в библиотеке
	 */
//	private void addDragDetection(LibraryNode dragIcon) {
//		dragIcon.setOnDragDetected (event -> {
//			rootPane.setOnDragOver(dragOverRoot);
//			project_pane.setOnDragOver(dragOverProjectPane);
//			project_pane.setOnDragDropped(dragDroppedOnProject);
//
//			targetType = ((LibraryNode) event.getSource()).getType();
//
//			shadowIcon.setType(targetType);
//			shadowIcon.relocateToPoint(new Point2D (event.getSceneX(), event.getSceneY()));
//			shadowIcon.startDragAndDrop (TransferMode.ANY).setContent(content);
//			shadowIcon.setVisible(true);
//			shadowIcon.setMouseTransparent(true);
//			shadowIcon.toFront();
//			event.consume();
//		});
//	}


	/**
	 * Инициализация обработчиков перемещения
	 */
//	private void buildDragHandlers() {
//
//		/**
//		 * Перемещение над библиотекой
//		 */
//		dragOverRoot = e -> {
//			if(project_pane==null) return;
//			Point2D p = project_pane.sceneToLocal(e.getSceneX(), e.getSceneY());
//			if (project_pane.boundsInLocalProperty().get().contains(p)) {
//				e.acceptTransferModes(TransferMode.ANY);
//				shadowIcon.relocateToPoint(new Point2D(e.getSceneX(), e.getSceneY()));
//			}
//			e.consume();
//		};
//
//		/**
//		 * Перемещение над проектом
//		 */
//		dragOverProjectPane = e -> {
//			e.acceptTransferModes(TransferMode.ANY);
//			shadowIcon.relocateToPoint( new Point2D(e.getSceneX(), e.getSceneY()));
//			e.consume();
//		};
//
//		/**
//		 * Элемент брошен в проект
//		 */
//		dragDroppedOnProject = e -> {
//			targetPoint = new Point2D(e.getSceneX(), e.getSceneY());
//			e.setDropCompleted(true);
//		};
//
//		/**
//		 * Обработка брошенной иконки
//		 */
//		rootPane.setOnDragDone (e -> {
//			project_pane.removeEventHandler(DragEvent.DRAG_OVER, dragOverProjectPane);
//			project_pane.removeEventHandler(DragEvent.DRAG_DROPPED, dragDroppedOnProject);
//			rootPane.removeEventHandler(DragEvent.DRAG_OVER, dragOverRoot);
//			shadowIcon.setVisible(false);
//			if(targetType!=null && targetPoint!=null){
//				GraphicNode node = new GraphicNode(targetType);
//				PanelsController.getCurrentPanel().getChildren().add(node);
//				Point2D localCoords = node.getParent().sceneToLocal(targetPoint.getX()-node.getWidth()/2, targetPoint.getY()-node.getHeight()/2);
//				node.relocate(localCoords.getX(), localCoords.getY());
//				targetPoint = null;
//				targetType = null;
//			}
//			e.consume();
//		});
//	}

	public static LibraryControllerOld get() { if(self==null) self = new LibraryControllerOld(); return self; }
	public void setProject_pane(Pane project_pane) { this.project_pane = project_pane; }
	public void setLibrary_pane(Pane library_pane) { this.library_pane = library_pane; }
	public void setRootPane(Pane rootPane) { this.rootPane = rootPane; }
}
