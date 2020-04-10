package controllers.elements;

import controllers.ContextMenuController;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import javax.xml.bind.annotation.XmlTransient;
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

	private ChangeListener<? super Number> xs = (e, nv, ov)-> updateStartPosition(), ys = (e, nv, ov)-> updateStartPosition(), xt = (e, nv, ov)-> updateEndPosition(), yt = (e, nv, ov)-> updateEndPosition();
	private double origWidth = 2, selWidht = 3;
	private GraphicNode source, target;
	private AnchorPane sourceConnector, targetConnector;
	private boolean connected = false;

	public Link(){
		setStrokeWidth(origWidth);
		setStroke(Color.web("#dc4b48"));
	}

	public void createConnection() {
		if(source!=null && target!=null && !connected){
			if(source.getLayoutX() > target.getLayoutX()) reverse();
			source.layoutXProperty().addListener(xs);
			source.layoutYProperty().addListener(ys);
			target.layoutXProperty().addListener(xt);
			target.layoutYProperty().addListener(yt);
			setOnMouseEntered(e->{ setStrokeWidth(selWidht); setStroke(Color.web("#dc4b48")); toFront(); });
			setOnMouseExited(e-> { setStrokeWidth(origWidth); setStroke(Color.WHITE); setCursor(Cursor.DEFAULT); });

			toFront();
			initContextMenu();
			setStrokeWidth(origWidth);
			setStroke(Color.WHITE);
			source.registerLink(this);
			target.registerLink(this);
			Point2D srsPoint = source.sceneToLocal(sourceConnector.localToScene(3.5,3.5));
			Point2D trgPoint = target.sceneToLocal(targetConnector.localToScene(3.5,3.5));
			offset = Math.max(srsPoint.getY(), trgPoint.getY())/2;
			updatePosition();
			connected = true;
		}
		else System.err.println("Connection error");
	}

	private void reverse(){
		AnchorPane tempCon = sourceConnector; sourceConnector = targetConnector; targetConnector = tempCon;
		GraphicNode tempNode = source; source = target; target = tempNode;
	}

	/**
	 * Инициализация контекстного меню
	 */
	private void initContextMenu(){
		ContextMenu cmElement = new ContextMenu();
		MenuItem remove = new MenuItem("Удалить");
		cmElement.getItems().addAll(remove);
		remove.setOnAction(e -> remove());
		setOnContextMenuRequested(e-> ContextMenuController.showContextMenu(cmElement, e));
	}

	/**
	 * Удалить соединение
	 */
	public void remove() {
		source.layoutXProperty().removeListener(xs);
		source.layoutYProperty().removeListener(ys);
		target.layoutXProperty().removeListener(xt);
		target.layoutYProperty().removeListener(yt);
		if(getParent()!=null) ((Pane)getParent()).getChildren().remove(this);

		source.removeLink(this);
		target.removeLink(this);
	}

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
		if(source.getLayoutX() > sourcePoint.getX()- 10) { buildPath(sourcePoint, targetPoint);	}
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
	public double minExtension = 30, offset = 0, averOffset = 0.4, upButtomOffset = 10;

	/**
	 * Построение пути source - правая сторона
	 * target - левая сторона
	 */
	private void buildPath(Point2D start, Point2D end) {
		path.clear();
		path.add(start);


		if (start.getX() - minExtension >=  end.getX() + minExtension + Math.abs(start.getX() - end.getX())*averOffset - offset ) {

			double averageX = (start.getX() + end.getX()) / 2; // на момент соединения
			if(source!=null && target!=null) { averageX = (start.getX() + end.getX()) / 2 - Math.abs(start.getX() - end.getX())*averOffset; }

			path.add(new Point2D(averageX + offset, start.getY()));
			path.add(new Point2D(averageX + offset, end.getY()));
		}
		else{
			double offset = this.offset;
			double averageY = (start.getY() + end.getY()) / 2; // на момент соединения
			if(source!=null && target!=null) {
				if(source.getLayoutY() < target.getLayoutY() + target.getHeight()/2)  { averageY = target.getLayoutY() - upButtomOffset; offset*=-1; }
				else { averageY = target.getLayoutY() + target.getHeight() + upButtomOffset; }
			}

			path.add(new Point2D(start.getX() - minExtension - this.offset, start.getY()));
			path.add(new Point2D(start.getX() - minExtension - this.offset, averageY + offset));
			path.add(new Point2D(end.getX() + minExtension + this.offset, averageY + offset));
			path.add(new Point2D(end.getX() + minExtension + this.offset, end.getY()));
		}
		path.add(end);
	}

	@XmlTransient
	public GraphicNode getSource() {
		return source;
	}
	@XmlTransient
	public GraphicNode getTarget() {
		return target;
	}
	@XmlTransient
	public AnchorPane getSourceConnector() {
		return sourceConnector;
	}
	@XmlTransient
	public AnchorPane getTargetConnector() {
		return targetConnector;
	}
	public void setSource(GraphicNode source) {
		this.source = source;
	}
	public void setTarget(GraphicNode target) {
		this.target = target;
	}
	public void setSourceConnector(AnchorPane sourceConnector) {
		this.sourceConnector = sourceConnector;
	}
	public void setTargetConnector(AnchorPane targetConnector) {
		this.targetConnector = targetConnector;
	}
}
