package controllers.elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import controllers.ContextMenuController;
import controllers.DragContainer;
import controllers.LinkController;
import controllers.dialogs.EditDialog;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Графический элемент
 */
public class GraphicNode extends AnchorPane {

    private ContextMenu cmElement;
    private EventHandler <MouseEvent> linkHandleDragDetected;
    private EventHandler <DragEvent> linkHandleDragDropped, linkDragOver, linkDragDropped;
    private EventHandler <MouseEvent> nodeDragDetected;
    private EventHandler <DragEvent> nodeDragOver, nodeDragDropped;

    protected StringProperty name = new SimpleStringProperty();
    private double currentX, currentY, offsetX, offsetY;

    private ArrayList<AnchorPane> connectors = new ArrayList<>();
    private ClipboardContent content = new ClipboardContent();
    private boolean overPanel = false;
    private ArrayList<Link> links = new ArrayList<>();
//    private Equipment eq;

    public GraphicNode() {
        String path = "/view/FXML/GraphicNode.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try { fxmlLoader.load(); } catch (IOException exception) { throw new RuntimeException(exception); }
        setId(UUID.randomUUID().toString());
        content.put(new DataFormat(), new DragContainer());
    }

    @FXML
    private void initialize() {
        Node dragPanel = null;
        buildNodeDragHandlers();
        buildLinkDragHandlers();
        setOnDragDetected(nodeDragDetected);
        for(Node node:getChildren()){
            String id = node.getId();
            if(id!=null){
                if(id.contains("dragPanel")) buildDragPanelHandlers(node);
                if(id.contains("connector")) buildConnectorHandlers((AnchorPane) node);
                if(id.contains("title_bar")) { name.set(((Label)node).getText()); ((Label)node).textProperty().bind(name); }
            }
        }
//        name.addListener((o, ov, nv) -> { if(eq !=null) eq.setName(nv); });

    }

    /**
     * Обработчики для панели
     * @param dragPanel - прозрачная панель (SVGPath)
     */
    private void buildDragPanelHandlers(Node dragPanel){
        dragPanel.setOnMouseEntered(e -> onMouseEntered());
        dragPanel.setOnMouseExited(e -> onMouseExited());
        dragPanel.setOnDragEntered(e -> onMouseEntered());
        dragPanel.setOnDragExited(e -> onMouseExited());
        dragPanel.addEventFilter(MouseEvent.MOUSE_PRESSED, e ->{ if(e.getButton() == MouseButton.PRIMARY) if(e.getClickCount()==2) openEditor(); });
        dragPanel.setCursor(Cursor.HAND);
        if(cmElement==null){
            cmElement = new ContextMenu();
            MenuItem parameter = new MenuItem("Параметры");
            MenuItem remove = new MenuItem("Удалить");
            cmElement.getItems().addAll(parameter, remove);

            remove.setOnAction(e -> removeSelf());
            parameter.setOnAction(e -> openEditor());
        }
        dragPanel.setOnContextMenuRequested(e -> ContextMenuController.showContextMenu(cmElement, e));
    }

    private void openEditor(){ EditDialog.show("тут нужно передать обьект для редактирования"); }

    /**
     * Обработчик для коннекторов
     * @param c - коннектор
     */
    private void buildConnectorHandlers(AnchorPane c){
        c.setOpacity(0);
        c.setOnMouseEntered(e->c.setOpacity(1)); c.setOnMouseExited(e->c.setOpacity(0));
        c.setOnDragEntered(e ->c.setOpacity(1)); c.setOnDragExited(e->setOpacity(1));
        c.setOnDragDetected(linkHandleDragDetected);
        c.setOnDragDropped(linkHandleDragDropped);
        c.setCursor(Cursor.HAND);
        connectors.add(c);
    }
    private void onMouseEntered(){
        for(Node c:connectors) c.setOpacity(1);
        for(Node child:getChildren())
            if(child.getClass()== Ellipse.class) {
                ((Ellipse)child).setStrokeWidth(2); ((Ellipse)child).setStroke(Color.RED);
            }
        overPanel = true;
    }

    private void onMouseExited(){
        for(Node c:connectors) c.setOpacity(0);
        for(Node child:getChildren())
            if(child.getClass()== Ellipse.class) {
                ((Ellipse)child).setStrokeWidth(1);
                ((Ellipse)child).setStroke(Color.BLACK);
            }
        overPanel = false;
    }
    /**
     * Обработчики перемещения элемента
     */
    public void buildNodeDragHandlers() {
        /**
         *  Обработчик начала перетаскивания элемента
         */
        nodeDragDetected = e -> {
            // если курсор над панелью SVGPath
            if(overPanel){
                getParent().setOnDragOver(nodeDragOver);
                getParent().setOnDragDropped(nodeDragDropped);
                offsetX = e.getX(); offsetY = e.getY();
                startDragAndDrop (TransferMode.ANY).setContent(content);
                e.consume();
            }
        };

        /**
         * Обработчик перемещения элемента в проекте
         */
        nodeDragOver = e -> {
            e.acceptTransferModes(TransferMode.ANY);
            Point2D localCoords = getParent().sceneToLocal(e.getSceneX(), e.getSceneY());
            relocate(localCoords.getX()- offsetX, localCoords.getY()- offsetY);
            e.consume();
        };

        /**
         * Обработчик броска элемента в проекте
         */
        nodeDragDropped = e -> {
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);
            e.setDropCompleted(true);
            e.consume();
        };
    }

    /**
     * Обработчики соединений
     */
    private void buildLinkDragHandlers() {
        /**
         * Обработчик начала соединения
         */
        linkHandleDragDetected = e -> {
            AnchorPane connector = (AnchorPane) e.getSource();
            getParent().setOnDragOver(linkDragOver);
            getParent().setOnDragDropped(linkDragDropped);
            double x = getLayoutX() + connector.getLayoutX() + connector.getPrefWidth()/2;
            double y = getLayoutY() + connector.getLayoutY() + connector.getPrefHeight()/2;
            LinkController.showTemporaryLink(x,y);
            LinkController.addSource2Link(this, connector);
            startDragAndDrop (TransferMode.ANY).setContent(content);
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
         * Обработчик конца соединения
         */
        linkHandleDragDropped = e -> {
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);
            AnchorPane connector = ((AnchorPane)e.getSource());
            LinkController.addTarget2Link(this, connector);
            LinkController.createConnection();
            LinkController.clearLink();
            e.setDropCompleted(true);
            e.consume();
        };

        /**
         * Обработчик неуспешного соединения
         */
        linkDragDropped = e -> {
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);
            LinkController.hideTemporaryLink();
            LinkController.clearLink();
            e.setDropCompleted(true);
            e.consume();
        };
    }

    /**
     * Удалить данный элемент из проекта
     */
    public void removeSelf(){
        ArrayList<Link> temp = new ArrayList<>(links);
        for(Link l:temp) l .removeSelf();
//        CurrentProject.getElements().remove(this);
    }

    /**
     * Удалить соединения на коннекторе
     */
    private void removeConnections(AnchorPane connector){ ArrayList<Link> temp = new ArrayList<>(links); for(Link l:temp) if(l.getSourceConnector()==connector || l.getTargetConnector()==connector) l .removeSelf(); }

//    public EquipmentType getType () { return mType; }

    public void setName(String name) { this.name.set(name); }
    public String getName() { return this.name.get(); }

    @Override
    public void relocate(double x, double y) {
        super.relocate(x, y);
        currentX=x; currentY=y;
//        if(eq!=null) { eq.setLayoutX(x); eq.setLayoutY(y); }
    }

    public void registerLink(Link link) { links.add(link); }
    public void removeLink(Link link) { links.remove(link); }

    public ArrayList<AnchorPane> getConnectors() { return connectors; }

//    public Equipment getEquipment() { return eq; }
//    public void setEquipment(Equipment equipment) { this.eq = equipment; }
}

