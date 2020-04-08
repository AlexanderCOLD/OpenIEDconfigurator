package controllers;

import application.GUI;
import controllers.elements.GraphicNode;
import controllers.elements.Link;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Статический класс для соединения элементов
 */
public class LinkController {

	private static Link tempLink = new Link();

	public static void addSource2Link(GraphicNode source, AnchorPane sourceConnector) { tempLink.setSource(source); tempLink.setSourceConnector(sourceConnector); }
	public static void addTarget2Link(GraphicNode target, AnchorPane targetConnector) {	tempLink.setTarget(target); tempLink.setTargetConnector(targetConnector); }
	private static EventHandler<MouseEvent> mouseMoved;
	private static EventHandler<DragEvent> mouseDragged;

	public static void createConnection() {
		if(tempLink.getTarget()!=null && tempLink.getSource()!=null && tempLink.getSource() != tempLink.getTarget()) {
			if(!connectionExists()) {
				Link link = new Link();
				link.setSource(tempLink.getSource()); link.setTarget(tempLink.getTarget());
				link.setSourceConnector(tempLink.getSourceConnector()); link.setTargetConnector(tempLink.getTargetConnector());
				PanelsController.getSelectedPanel().getChildren().add(link);
				link.createConnection();
			}
			else System.out.println("Connection already exists");
			tempLink.setSource(null); tempLink.setTarget(null);
		}
	}

	/**
	 * Проверка существования соединения
	 * @return - true если такое соединение уже существует
	 */
	private static boolean connectionExists(){
		for(Link link: ProjectController.getConnections()) {
			if(link==tempLink) continue;
			if(tempLink.getSourceConnector()==link.getSourceConnector() && tempLink.getTargetConnector()==link.getTargetConnector()) return true;
			if(tempLink.getSourceConnector()==link.getTargetConnector() && tempLink.getTargetConnector()==link.getSourceConnector()) return true;
		}
		return false;
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
		tempLink.setSource(null); tempLink.setTarget(null); tempLink.setSourceConnector(null); tempLink.setTargetConnector(null);
//		if(mouseMoved==null) buildMouseEvents();
	}

	private static ObservableList<Double> points;
	private static Link link;
	private static double dragBorders = 3;
	private static int dragPoint = 0;

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


}
