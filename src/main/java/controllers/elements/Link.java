package controllers.elements;

import controllers.ContextMenuController;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;

import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Соединение между элементами
 */

public class Link extends CubicCurve{
	
	private double xSoff = 0, ySoff = 0; // point offset
	private double xToff = 0, yToff = 0; // point offset
	private ChangeListener<? super Number> listnXs = (e, nv, ov)-> setStartX(nv.doubleValue()+ xSoff);
	private ChangeListener<? super Number> listnYs = (e, nv, ov)-> setStartY(nv.doubleValue()+ ySoff);
	private ChangeListener<? super Number> listnXt = (e, nv, ov)-> setEndX(nv.doubleValue()+ xToff);
	private ChangeListener<? super Number> listnYt = (e, nv, ov)-> setEndY(nv.doubleValue()+ yToff);

	private GraphicNode source, target;
	private AnchorPane sourceConnector, targetConnector;
	private String sourceID, targetID, sourceConnectorID, targetConnectorID;
	private boolean connected = false;

	public Link(){
		setStrokeWidth(2);
		setStroke(Color.RED);
		controlX1Property().bind( Bindings.add(startXProperty(), 0)); controlX2Property().bind( Bindings.add(endXProperty(),-0));
		controlY1Property().bind( Bindings.add(startYProperty(), 0) ); controlY2Property().bind( Bindings.add(endYProperty(),-0));

		setOnMouseEntered(event -> { setStrokeWidth(2); setStroke(Color.RED); });
		setOnMouseExited(event -> { setStrokeWidth(1); setStroke(Color.BLACK); });
	}

	public void createConnection() {
		if(source!=null && target!=null && !connected){
			source.layoutXProperty().addListener(listnXs);
			source.layoutYProperty().addListener(listnYs);
			target.layoutXProperty().addListener(listnXt);
			target.layoutYProperty().addListener(listnYt);

			updateOffset();
			toFront();
			initContextMenu();
			connected = true;
			setStrokeWidth(1);
			setStroke(Color.BLACK);
			setCursor(Cursor.HAND);
			source.registerLink(this);
		    target.registerLink(this);
		}
		else System.err.println("Connection error");
	}

	/**
	 * Инициализация контекстного меню
	 */
	private void initContextMenu(){
		ContextMenu cmElement = new ContextMenu();
		MenuItem remove = new MenuItem("Удалить");
		cmElement.getItems().addAll(remove);
		remove.setOnAction(e -> removeSelf());
		setOnContextMenuRequested(e-> ContextMenuController.showContextMenu(cmElement, e));
	}

	/**
	 * Удалить соединение
	 */
	public void removeSelf() {
		if(listnXs !=null) source.layoutXProperty().removeListener(listnXs);
		if(listnYs !=null) source.layoutYProperty().removeListener(listnYs);
		if(listnXt !=null) target.layoutXProperty().removeListener(listnXt);
		if(listnYt !=null) target.layoutYProperty().removeListener(listnYt);
//		CurrentProject.getElements().remove(this);

		source.removeLink(this);
		target.removeLink(this);
	}

	/**
	 * Рассчет сдвигов для учето положения коннектора
	 */
	private void updateOffset(){
		xSoff = sourceConnector.getLayoutX() + sourceConnector.getPrefWidth()/2;
		ySoff = sourceConnector.getLayoutY() + sourceConnector.getPrefHeight()/2;
		xToff = targetConnector.getLayoutX() + targetConnector.getPrefWidth()/2;
		yToff = targetConnector.getLayoutY() + targetConnector.getPrefHeight()/2;
		reDraw();
	}

	/**
	 * Перерировать положение соединительной линии
	 */
	public void reDraw() {
		setStartX(source.getLayoutX()+ xSoff);
		setStartY(source.getLayoutY()+ ySoff);
		setEndX(target.getLayoutX()+ xToff);
		setEndY(target.getLayoutY()+ yToff);
	}

	public void setStart(double x, double y) { setStartX(x); setStartY(y); xSoff = x; ySoff = y; }
	public void setEnd(double x, double y) { setEndX(x); setEndY(y); xToff = x; yToff = y;}

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
	public String getSourceID() {
		return sourceID;
	}
	public String getTargetID() {
		return targetID;
	}
	public String getSourceConnectorID() {
		return sourceConnectorID;
	}
	public String getTargetConnectorID() {
		return targetConnectorID;
	}
	public void setSource(GraphicNode source) {
		this.sourceID = source.getId();
		this.source = source;
	}
	public void setTarget(GraphicNode target) {
		this.targetID = target.getId();
		this.target = target;
	}
	public void setSourceConnector(AnchorPane sourceConnector) {
		this.sourceConnectorID = sourceConnector.getId();
		this.sourceConnector = sourceConnector;
	}
	public void setTargetConnector(AnchorPane targetConnector) {
		this.targetConnectorID = targetConnector.getId();
		this.targetConnector = targetConnector;
	}
	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}
	public void setTargetID(String targetID) {
		this.targetID = targetID;
	}
	public void setSourceConnectorID(String sourceConnectorID) {
		this.sourceConnectorID = sourceConnectorID;
	}
	public void setTargetConnectorID(String targetConnectorID) {
		this.targetConnectorID = targetConnectorID;
	}
}
