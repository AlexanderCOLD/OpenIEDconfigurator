package controllers;

import controllers.elements.GraphicNode;
import controllers.elements.Link;
import javafx.scene.layout.AnchorPane;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Статический класс для соединения элементов
 */
public class LinkController {

	private static Link tempLink = new Link();
	private static Link link;

	public static void addSource2Link(GraphicNode source, AnchorPane sourceConnector) { if(link == null) link = new Link(); link.setSource(source); link.setSourceConnector(sourceConnector); }
	public static void addTarget2Link(GraphicNode target, AnchorPane targetConnector) {
		if(link == null)
			link = new Link();
		link.setTarget(target);
		link.setTargetConnector(targetConnector); }
	public static void clearLink() { link = null; }

	public static void createConnection() {
		if(link != null) {
			if(link.getSource()!= link.getTarget()) {
				if(!connectionExists()) {
//					CurrentProject.getElements().add(link);
				link.createConnection();
				}
				else System.out.println("Connection already exists");
			}
		}
		
		clearLink();
		hideTemporaryLink();
	}

	/**
	 * Проверка существования соединения
	 * @return - true если такое соединение уже существует
	 */
	private static boolean connectionExists(){
		for(Link l: ProjectController.getConnections()) {
			if(link.getSourceConnector()==l.getSourceConnector() && link.getTargetConnector()==l.getTargetConnector()) return true;
			if(link.getSourceConnector()==l.getTargetConnector() && link.getTargetConnector()==l.getSourceConnector()) return true;
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
	 * Переместить второй конец соедиющей линии
	 * @param endX - конечное положение Х
	 * @param endY - конечное полодение У
	 */
	public static void moveTemporaryLink(double endX, double endY) { tempLink.setEnd(endX, endY); }

	/**
	 * Скрыть временную соединяющую линию
	 */
	public static void hideTemporaryLink() {
		AnchorPane parent = (AnchorPane) tempLink.getParent(); LinkController.tempLink.setVisible(false);  if(parent!=null) parent.getChildren().remove(tempLink); }
}
