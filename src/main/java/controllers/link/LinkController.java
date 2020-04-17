package controllers.link;

import application.GUI;
import controllers.PanelsController;
import controllers.graphicNode.Connector;
import controllers.graphicNode.ConnectorType;
import controllers.graphicNode.GraphicNode;
import controllers.object.DragContainer;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Статический класс для соединения элементов
 */
public class LinkController {

	private static final ArrayList<Link> connections = new ArrayList<>();

	private static final Link tempLink = new Link();
	public static void addSource2Link(Connector sourceConnector) { tempLink.setSourceConnector(sourceConnector); }
	public static void addTarget2Link(Connector targetConnector) { tempLink.setTargetConnector(targetConnector); }


	public static void createConnection() {
		if(tempLink.getSourceConnector()!=null && tempLink.getTargetConnector()!=null){
			GraphicNode sourceGraphicNode = tempLink.getSourceConnector().getGraphicNode();
			GraphicNode targetGraphicNode = tempLink.getTargetConnector().getGraphicNode();

			if(sourceGraphicNode != targetGraphicNode) {
				if(isOutput2Input()){
					if(!connectionExists()) {
						Link link = new Link();
						link.setSourceConnector(tempLink.getSourceConnector());
						link.setTargetConnector(tempLink.getTargetConnector());
						PanelsController.getSelectedPanel().getChildren().add(link);
						link.createConnection();
					}
					else GUI.writeMessage("Connection already exists");
				}
				else GUI.writeErrMessage("Please connect only outputs to input connectors");
			}
			else GUI.writeErrMessage("Unacceptable connection");

			tempLink.setSourceConnector(null); tempLink.setTargetConnector(null);
		}
	}

	/**
	 * Проверка существования соединения
	 * @return - true если такое соединение уже существует
	 */
	private static boolean connectionExists(){
		for(Link link: connections) {
			if(link==tempLink) continue;
			if(tempLink.getSourceConnector()==link.getSourceConnector() && tempLink.getTargetConnector()==link.getTargetConnector()) return true;
			if(tempLink.getSourceConnector()==link.getTargetConnector() && tempLink.getTargetConnector()==link.getSourceConnector()) return true;
		}
		return false;
	}

	private static boolean isOutput2Input(){
		ConnectorType sourceType = tempLink.getSourceConnector().getConnectorType();
		ConnectorType targetType = tempLink.getTargetConnector().getConnectorType();
		return sourceType!=targetType;
	}

	/**
	 * Показать временную соединяющую линию
	 * @param startX - начальное положение Х
	 * @param startY - начальное положение У
	 */
	public static void showTemporaryLink(double startX, double startY) {
		PanelsController.getSelectedPanel().getChildren().add(LinkController.tempLink);
		LinkController.tempLink.setStart(startX, startY);
		LinkController.tempLink.setVisible(true);
		LinkController.tempLink.toBack();
	}

	/**
	 * Переместить второй конец соединяющей линии
	 * @param endX - конечное положение Х
	 * @param endY - конечное полодение У
	 */
	public static void moveTemporaryLink(double endX, double endY) { tempLink.setEnd(endX, endY); }

	/**
	 * Скрыть временную соединяющую линию
	 */
	public static void hideTemporaryLink() {
		Pane parent = (Pane) tempLink.getParent(); if(parent!=null) parent.getChildren().remove(tempLink);
		tempLink.setSourceConnector(null); tempLink.setTargetConnector(null);
//		if(mouseMoved==null) buildMouseEvents();
	}

	/**
	 * /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 * /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 * 												Обработчики коннекторов
	 */

	private static final ArrayList<Connector> connectorList = new ArrayList<>();
	private static EventHandler <MouseEvent> linkDragDetected;
	private static EventHandler <DragEvent> linkDragDropped, linkDragOver, linkDragDone;

	private static EventHandler <MouseEvent> connectorMouseEntered, connectorMouseExited;
	private static EventHandler <DragEvent> connectorDragMouseEntered, connectorDragMouseExited;

	private static final ClipboardContent content = new ClipboardContent(){{put(new DataFormat(), new DragContainer());}};

	public static void addConnectorHandlers(Connector connector){
		if(linkDragDetected==null) initialize();
		if(!connectorList.contains(connector)){
			connector.addEventFilter(MouseEvent.DRAG_DETECTED, linkDragDetected);
			connector.addEventFilter(DragEvent.DRAG_DROPPED, linkDragDropped);
			connector.addEventFilter(MouseEvent.MOUSE_ENTERED, connectorMouseEntered);
			connector.addEventFilter(MouseEvent.MOUSE_EXITED, connectorMouseExited);
			connector.addEventHandler(DragEvent.DRAG_ENTERED, connectorDragMouseEntered);
			connector.addEventHandler(DragEvent.DRAG_EXITED, connectorDragMouseExited);
			connectorList.add(connector);
		}
	}

	public static void removeConnectorHandlers(Connector connector){
		if(linkDragDetected==null) initialize();
		if(connectorList.contains(connector)){
			connector.removeEventFilter(MouseEvent.DRAG_DETECTED, linkDragDetected);
			connector.removeEventFilter(DragEvent.DRAG_DROPPED, linkDragDropped);
			connector.removeEventFilter(MouseEvent.MOUSE_ENTERED, connectorMouseEntered);
			connector.removeEventFilter(MouseEvent.MOUSE_EXITED, connectorMouseExited);
			connector.removeEventFilter(DragEvent.DRAG_ENTERED, connectorDragMouseEntered);
			connector.removeEventFilter(DragEvent.DRAG_EXITED, connectorDragMouseExited);
			connectorList.remove(connector);
		}
	}

	private static void initialize() {

		/**
		 * Обработчики выделения коннектора
		 */
		connectorMouseEntered = e-> ((Connector) e.getSource()).setSelected(true);
		connectorMouseExited = e-> ((Connector) e.getSource()).setSelected(false);
		connectorDragMouseEntered = e-> ((Connector) e.getSource()).setSelected(true);
		connectorDragMouseExited = e-> ((Connector) e.getSource()).setSelected(false);

		/**
		 * Обработчик начала соединения (у соурса)
		 * */
		linkDragDetected = e -> {
			Connector connector = (Connector) e.getSource();
			GraphicNode graphicNode = connector.getGraphicNode();
			Pane parent = (Pane) graphicNode.getParent();

			parent.setOnDragOver(linkDragOver);
			parent.setOnDragDone(linkDragDone);

			Point2D globalPoint = connector.localToScene(connector.getPrefWidth()/2, connector.getPrefHeight()/2);
			Point2D parentPoint = parent.sceneToLocal(globalPoint); // конвертация координат

			LinkController.addSource2Link(connector);
			LinkController.showTemporaryLink(parentPoint.getX(), parentPoint.getY());
			connector.startDragAndDrop (TransferMode.ANY).setContent(content);
			e.consume();
		};

		/**
		 * Обаботчик перемещения соединения
		 */
		linkDragOver = e -> {
			e.acceptTransferModes(TransferMode.ANY);
			LinkController.moveTemporaryLink(e.getX(), e.getY());
			e.consume();
		};

		/**
		 * Обработчик конца соединения (у таргета)
		 */
		linkDragDropped = e -> {
			Connector connector = (Connector) e.getSource();
			GraphicNode graphicNode = connector.getGraphicNode();
			Pane pane = (Pane) graphicNode.getParent();

			pane.setOnDragOver(null);
			pane.setOnDragDropped(null);

			LinkController.addTarget2Link(connector);
			LinkController.createConnection();
			e.setDropCompleted(true);
			e.consume();
		};

		/**
		 * Обработчик окончания соединения (не важно, успешного или нет)
		 */
		linkDragDone = e -> {
			Pane pane = (Pane) e.getSource();
			pane.setOnDragOver(null);
			pane.setOnDragDropped(null);
			LinkController.createConnection();
			LinkController.hideTemporaryLink();
			e.consume();
		};

	}



	/**
	 * /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 * /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 * 										Работа над корректировкой соединенией
	 */

	private static ObservableList<Double> points;
	private static Link link;
	private static double dragBorders = 3;
	private static int dragPoint = 0;

	private static EventHandler<MouseEvent> mouseMoved;
	private static EventHandler<DragEvent> mouseDragged;

	private static void buildMouseEvents(){
		mouseMoved = e->{
			if(e.getTarget().getClass()==Link.class){
				link = (Link) e.getTarget();
				points = link.getPoints();
				Point2D mousePosition = link.sceneToLocal(e.getSceneX(), e.getSceneY());
				double x = mousePosition.getX(), y = mousePosition.getY();
				if(points.size()==12){
					if(y > (points.get(7) - dragBorders) && y < (points.get(7) + dragBorders)) {
						if(x > (points.get(4) - dragBorders) && x < (points.get(4) + dragBorders)) { link.setCursor(Cursor.CROSSHAIR); dragPoint = 2; }
						else if (x > (points.get(6) - dragBorders) && x < (points.get(6) + dragBorders)) { link.setCursor(Cursor.CROSSHAIR); dragPoint = 3; }
						else { link.setCursor(Cursor.DEFAULT); dragPoint = 0; }
					}
					else if(y > (points.get(3) - dragBorders) && y < (points.get(3) + dragBorders) && x > (points.get(2) - dragBorders) && x < (points.get(2) + dragBorders)){ link.setCursor(Cursor.CROSSHAIR); dragPoint = 1; }
					else if(y > (points.get(9) - dragBorders) && y < (points.get(9) + dragBorders) && x > (points.get(8) - dragBorders) && x < (points.get(8) + dragBorders)){ link.setCursor(Cursor.CROSSHAIR); dragPoint = 4; }
					else { link.setCursor(Cursor.DEFAULT); dragPoint = 0; }
				}
//				else if(points.size()==8){
//					if(y > (points.get(3) - dragBorders) && y < (points.get(3) + dragBorders) && x > (points.get(2) - dragBorders) && x < (points.get(2) + dragBorders)){ link.setCursor(Cursor.CROSSHAIR); dragPoint = 1; }
//					else if(y > (points.get(9) - dragBorders) && y < (points.get(9) + dragBorders) && x > (points.get(8) - dragBorders) && x < (points.get(8) + dragBorders)){ link.setCursor(Cursor.CROSSHAIR); dragPoint = 4; }
//					else { link.setCursor(Cursor.DEFAULT); dragPoint = 0; }
//				}
				System.out.println("DRAG POINT: " + dragPoint);
			}
		};

		mouseDragged = event -> {
			System.out.println(123);
		};

		GUI.get().addEventFilter(MouseEvent.MOUSE_MOVED, mouseMoved);
	}

	public static ArrayList<Link> getConnections() { return connections; }
}
