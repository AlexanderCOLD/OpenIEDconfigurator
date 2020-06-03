package controllers.link;

import application.GUI;
import controllers.MainContextMenu;
import controllers.graphicNode.Connector;
import controllers.graphicNode.ConnectorType;
import controllers.graphicNode.GraphicNode;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Соединение между элементами
 */

public class Link extends Polyline {

	private final ChangeListener<? super Number> xs = (e, nv, ov)-> updateStartPosition(), ys = (e, nv, ov)-> updateStartPosition(), xt = (e, nv, ov)-> updateEndPosition(), yt = (e, nv, ov)-> updateEndPosition();
	private final double originalWidth = 2, selectedWidth = 3;
	private GraphicNode sourceGraphicNode, targetGraphicNode;
	private Connector sourceConnector, targetConnector;
	private boolean connected = false;

	public Link(){ setStrokeWidth(originalWidth); setStroke(Color.web("#dc4b48")); }

	public void createConnection() {
		if(sourceConnector!=null && targetConnector!=null && !connected){
			if(sourceConnector.getConnectorType()==ConnectorType.inputConnector) reverse(); // source - выход, target - вход
			sourceGraphicNode.layoutXProperty().addListener(xs);
			sourceGraphicNode.layoutYProperty().addListener(ys);
			targetGraphicNode.layoutXProperty().addListener(xt);
			targetGraphicNode.layoutYProperty().addListener(yt);

			setOnMouseEntered(e->{ setStrokeWidth(selectedWidth); setStroke(Color.web("#dc4b48")); toFront(); });
			setOnMouseExited(e-> { setStrokeWidth(originalWidth); setStroke(Color.WHITE); toBack(); });

			toBack();
			initContextMenu();
			setStrokeWidth(originalWidth);
			setStroke(Color.WHITE);
			sourceGraphicNode.getConnections().add(this);
			targetGraphicNode.getConnections().add(this);
			sourceConnector.getConnections().add(this);
			targetConnector.getConnections().add(this);
			Point2D srsPoint = sourceGraphicNode.sceneToLocal(sourceConnector.localToScene(sourceConnector.getWidth()/2,sourceConnector.getHeight()/2));
			Point2D trgPoint = targetGraphicNode.sceneToLocal(targetConnector.localToScene(targetConnector.getWidth()/2,targetConnector.getHeight()/2));
			connectorOffset = Math.max(srsPoint.getY(), trgPoint.getY())/2;
			updatePosition();
			connected = true;
		}
		else System.err.println("Connection error");
	}

	private void reverse(){
		Connector tempCon = sourceConnector; sourceConnector = targetConnector; targetConnector = tempCon;
		GraphicNode tempNode = sourceGraphicNode; sourceGraphicNode = targetGraphicNode; targetGraphicNode = tempNode;
	}

	/**
	 * Инициализация контекстного меню
	 */
	private void initContextMenu(){
		ContextMenu cmElement = new ContextMenu();
		MenuItem remove = new MenuItem("Удалить");
		cmElement.getItems().addAll(remove);
		remove.setOnAction(e -> remove());
		setOnContextMenuRequested(e-> {
			if(GUI.getCurrentContextMenu()!=null) GUI.getCurrentContextMenu().hide();
			cmElement.show(GUI.get(), e.getScreenX(), e.getScreenY());
			GUI.setCurrentContextMenu(cmElement);
		});
	}

	/**
	 * Удалить соединение
	 */
	public void remove() {
		sourceGraphicNode.layoutXProperty().removeListener(xs);
		sourceGraphicNode.layoutYProperty().removeListener(ys);
		targetGraphicNode.layoutXProperty().removeListener(xt);
		targetGraphicNode.layoutYProperty().removeListener(yt);
		if(getParent()!=null) ((Pane)getParent()).getChildren().remove(this);

		sourceGraphicNode.getConnections().remove(this);
		targetGraphicNode.getConnections().remove(this);
		sourceConnector.getConnections().remove(this);
		targetConnector.getConnections().remove(this);
	}

	/** Перерисовать соединение */
	public void updatePosition(){ updateStartPosition(); updateEndPosition(); }

	private void updateStartPosition(){
		Point2D globalPoint = sourceConnector.localToScene(sourceConnector.getPrefWidth()/2, sourceConnector.getPrefHeight()/2);
		Point2D parentPoint = getParent().sceneToLocal(globalPoint); // конвертация координат
		setStart(parentPoint.getX(), parentPoint.getY());
	}

	private void updateEndPosition(){
		Point2D globalPoint = targetConnector.localToScene(targetConnector.getPrefWidth()/2, targetConnector.getPrefHeight()/2);
		Point2D parentPoint = getParent().sceneToLocal(globalPoint); // конвертация координат
		setEnd(parentPoint.getX(), parentPoint.getY());
	}

	private Point2D sourcePoint = new Point2D(1, 1), targetPoint = new Point2D(1, 1);

	public void setStart(double x, double y) { sourcePoint = new Point2D(x, y); draw(); }
	public void setEnd(double x, double y) { targetPoint = new Point2D(x, y); draw(); }

	private List<Point2D> path = new ArrayList<>();

	public void draw() {
		getPoints().clear();
		if(sourceGraphicNode.getLayoutX() > sourcePoint.getX()-10) { buildPath(sourcePoint, targetPoint);	}
		else { buildPath(targetPoint, sourcePoint); Collections.reverse(path); }
		path.forEach(p -> getPoints().addAll(p.getX(), p.getY()));
	}

	/**
	 * minExtension - минимальная длина возле коннектора
	 * offset - смещение - учет положения коннектора
	 * averOffset - от 0 до 1 - прижим к Source (0 - середина между Source и Target)
	 * upButtomOffset - смещение Average над или под элементом
	 * totalOffset - итоговое смещение
	 */
	private final double minExtension = 15, averOffset = 0.35, upButtomOffset = 10;
	private double connectorOffset = 0;

	/**
	 * Построение пути
	 */
	private void buildPath(Point2D start, Point2D end) {

		path.clear();
		path.add(start);

		if (start.getX() - minExtension >=  end.getX() + minExtension + Math.abs(start.getX() - end.getX())*averOffset - connectorOffset) {

			double averageX = (start.getX() + end.getX()) / 2; // на момент соединения
			if(sourceGraphicNode !=null && targetGraphicNode !=null) { averageX = (start.getX() + end.getX()) / 2 - Math.abs(start.getX() - end.getX())*averOffset; }

			path.add(new Point2D(averageX + connectorOffset, start.getY()));
			path.add(new Point2D(averageX + connectorOffset, end.getY()));
		}
		else{
			double offset = this.connectorOffset;
			double averageY = (start.getY() + end.getY()) / 2; // на момент соединения
			if(sourceGraphicNode !=null && targetGraphicNode !=null) {
				if(sourceGraphicNode.getLayoutY() < targetGraphicNode.getLayoutY() + targetGraphicNode.getHeight()/2)  { averageY = targetGraphicNode.getLayoutY() - upButtomOffset; offset*=-1; }
				else { averageY = targetGraphicNode.getLayoutY() + targetGraphicNode.getHeight() + upButtomOffset; }
			}

			path.add(new Point2D(start.getX() - minExtension - this.connectorOffset, start.getY()));
			path.add(new Point2D(start.getX() - minExtension - this.connectorOffset, averageY + offset));
			path.add(new Point2D(end.getX() + minExtension + this.connectorOffset, averageY + offset));
			path.add(new Point2D(end.getX() + minExtension + this.connectorOffset, end.getY()));
		}
		path.add(end);
	}

	public Connector getSourceConnector() { return sourceConnector; }
	public Connector getTargetConnector() {
		return targetConnector;
	}
	public void setSourceConnector(Connector sourceConnector) {	this.sourceConnector = sourceConnector;	sourceGraphicNode = sourceConnector==null ? null : sourceConnector.getGraphicNode(); }
	public void setTargetConnector(Connector targetConnector) {	this.targetConnector = targetConnector;	targetGraphicNode = targetConnector==null ? null : targetConnector.getGraphicNode(); }
}
