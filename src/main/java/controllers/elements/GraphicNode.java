package controllers.elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import application.GUI;
import controllers.ContextMenuController;
import controllers.ProjectController;
import controllers.object.DragContainer;
import controllers.LinkController;
import controllers.dialogs.EditDialog;
import iec61850.DO;
import iec61850.DS;
import iec61850.LN;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Графический элемент
 */
public class GraphicNode extends AnchorPane {

    @FXML private Label title_bar;
    @FXML private AnchorPane mainPanel;

    private ContextMenu cmElement;
    private double x, y, offsetX, offsetY;
    private EventHandler <MouseEvent> linkDragDetected;
    private EventHandler <DragEvent> linkDragDropped, linkDragOver, linkDragDone;

    private EventHandler <MouseEvent> dragDetected;
    private EventHandler <DragEvent> dragEvent, dragDone;

    private StringProperty name = new SimpleStringProperty();
    private BooleanProperty selected = new SimpleBooleanProperty(false);
    private BooleanProperty draggable = new SimpleBooleanProperty(false);

    private ArrayList<AnchorPane> leftConnectors = new ArrayList<>();
    private ArrayList<AnchorPane> rightConnectors = new ArrayList<>();

    private ClipboardContent content = new ClipboardContent(){{put(new DataFormat(), new DragContainer());}};
    private boolean overPanel = false;
    private ArrayList<Link> links = new ArrayList<>();
    private ChangeListener<Boolean> selectionListener;

    private int grid = 30; // Step

    public GraphicNode() {
        String path = "/view/FXML/GraphicNode.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try { fxmlLoader.load(); } catch (IOException exception) { throw new RuntimeException(exception); }
        setId(UUID.randomUUID().toString());
    }

    @Override
    public void setUserData(Object value) {
        super.setUserData(value);
        setMaxHeight(100);

        if(value.getClass()==LN.class){
            LN ln = (LN) value;
            int size = Math.max(ln.getDataSetInput().getDataObject().size(), ln.getDataSetOutput().getDataObject().size());
            mainPanel.getChildren().clear();
            for(int i=0; i<size; i++) {
                BorderPane borderPane = new BorderPane();
                mainPanel.getChildren().add(borderPane);
                AnchorPane.setLeftAnchor(borderPane,0.0);
                AnchorPane.setRightAnchor(borderPane,0.0);
                AnchorPane.setTopAnchor(borderPane,15.0*i);
//                borderPane.setStyle("-fx-border-color: RED; -fx-border-width: 1");
            }
            addConnector(ln.getDataSetInput(), leftConnectors);
            addConnector(ln.getDataSetOutput(), rightConnectors);
            title_bar.setText(ln.getName());
        }
    }

    @FXML
    private void initialize() { }

    public void prepareHandlers(){
        buildHandlers();
        buildClickHandlers(this);

        selectionListener = (o,ov,nv) ->{
            if(nv) setStyle("-fx-background-color: -fx-second-color; -fx-border-color: #dc4b48; -fx-border-radius: 7; -fx-background-radius: 7; -fx-border-width: 2");
            else setStyle("-fx-background-color: -fx-second-color; -fx-border-color: -fx-first-color; -fx-border-radius: 7; -fx-background-radius: 7; -fx-border-width: 1.5");
        };
        draggable.addListener((o, ov, nv) -> {
            if(nv) {
                for(AnchorPane connector:leftConnectors) { connector.addEventFilter(MouseEvent.DRAG_DETECTED, linkDragDetected); connector.addEventFilter(DragEvent.DRAG_DROPPED, linkDragDropped); }
                for(AnchorPane connector:rightConnectors) { connector.addEventFilter(MouseEvent.DRAG_DETECTED, linkDragDetected); connector.addEventFilter(DragEvent.DRAG_DROPPED, linkDragDropped); }
                addEventHandler(MouseEvent.DRAG_DETECTED, dragDetected);
                selected.addListener(selectionListener);
            }
            else {
                for(AnchorPane connector:leftConnectors) { connector.removeEventFilter(MouseEvent.DRAG_DETECTED, linkDragDetected); connector.removeEventFilter(DragEvent.DRAG_DROPPED, linkDragDropped); }
                for(AnchorPane connector:rightConnectors) { connector.removeEventFilter(MouseEvent.DRAG_DETECTED, linkDragDetected); connector.removeEventFilter(DragEvent.DRAG_DROPPED, linkDragDropped); }
                removeEventHandler(MouseEvent.DRAG_DETECTED, dragDetected);
                selected.removeListener(selectionListener);
            }
        });
    }

    private void addConnector(DS dataSet, ArrayList<AnchorPane> connectorsList){
        for(DO dataObject:dataSet.getDataObject()){
            AnchorPane pane = new AnchorPane(); pane.setStyle("-fx-background-color: transparent");
            AnchorPane connector = new AnchorPane(); connector.setStyle("-fx-background-color: WHITE; -fx-background-radius: 5"); connector.setPrefSize(7,7); Scale scale = new Scale(); scale.setPivotX(3.5); scale.setPivotY(3.5); connector.getTransforms().add(scale);
            Label label = new Label(dataObject.getDataAttributeName()!=null ? String.format("%s (%s)", dataObject.getDataAttributeName(), dataObject.getDataObjectName()) : String.format("(%s)", dataObject.getDataObjectName()));
            label.setTextFill(Color.WHITE); /*label.setFont(Font.font(10.0));*/
            pane.getChildren().add(connector);
            pane.getChildren().add(label);
            BorderPane bp = (BorderPane) mainPanel.getChildren().get(connectorsList.size());

            AnchorPane.setTopAnchor(label, 3.0); AnchorPane.setLeftAnchor(label, 7.0); AnchorPane.setRightAnchor(label, 7.0);

            AnchorPane.setTopAnchor(connector, 8.0);
            if(connectorsList==leftConnectors){ AnchorPane.setLeftAnchor(connector, -5.0); label.setAlignment(Pos.CENTER_LEFT); bp.setLeft(pane); }
            else{ AnchorPane.setRightAnchor(connector, -5.0); label.setAlignment(Pos.CENTER_RIGHT); bp.setRight(pane); }

            connectorsList.add(connector);

            connector.setOnMouseEntered(e->{ connector.setStyle("-fx-background-color: RED; -fx-background-radius: 5");   ((Scale)connector.getTransforms().get(0)).setX(1.4); ((Scale)connector.getTransforms().get(0)).setY(1.4); connector.toFront(); });
            connector.setOnMouseExited(e-> { connector.setStyle("-fx-background-color: WHITE; -fx-background-radius: 5"); ((Scale)connector.getTransforms().get(0)).setX(1.0); ((Scale)connector.getTransforms().get(0)).setY(1.0); });
            connector.setOnDragEntered(e->{ connector.setStyle("-fx-background-color: RED; -fx-background-radius: 5");    ((Scale)connector.getTransforms().get(0)).setX(1.4); ((Scale)connector.getTransforms().get(0)).setY(1.4); connector.toFront(); });
            connector.setOnDragExited(e-> { connector.setStyle("-fx-background-color: WHITE; -fx-background-radius: 5");  ((Scale)connector.getTransforms().get(0)).setX(1.0); ((Scale)connector.getTransforms().get(0)).setY(1.0); });
        }
    }

    private void buildHandlers(){

        dragDetected = e->{
            offsetX = e.getX(); offsetY = e.getY();
            if(offsetY<20 && !GUI.isCtrl()){ getParent().addEventFilter(DragEvent.DRAG_OVER, dragEvent); getParent().addEventFilter(DragEvent.DRAG_DONE, dragDone); startDragAndDrop(TransferMode.ANY).setContent(content); }
            e.consume();
        };
        dragEvent = e->{ relocate(e.getX() - offsetX, e.getY() - offsetY); e.acceptTransferModes(TransferMode.ANY); e.consume(); };
        dragDone = e->{
            getParent().removeEventFilter(DragEvent.DRAG_DONE, dragDone); getParent().removeEventFilter(DragEvent.DRAG_OVER, dragEvent);
            updateGrid();
            e.consume();
        };


        /**
         * Обработчик начала соединения
         * */
        linkDragDetected = e -> {
            AnchorPane connector = (AnchorPane) e.getSource();
            getParent().setOnDragOver(linkDragOver); getParent().setOnDragDone(linkDragDone);
            Point2D globalPoint = connector.localToScene(connector.getPrefWidth()/2, connector.getPrefHeight()/2); Point2D parentPoint = getParent().sceneToLocal(globalPoint); // конвертация координат
            LinkController.addSource2Link(this, connector); LinkController.showTemporaryLink(parentPoint.getX(), parentPoint.getY());
            startDragAndDrop (TransferMode.ANY).setContent(content);
            e.consume();
        };

        /**
         * Обаботчик перемещения соединения
         */
        linkDragOver = e -> { e.acceptTransferModes(TransferMode.ANY); LinkController.moveTemporaryLink(e.getX(), e.getY()); e.consume(); };

        /**
         * Обработчик конца соединения (у таргета)
         */
        linkDragDropped = e -> {
            getParent().setOnDragOver(null); getParent().setOnDragDropped(null);
            AnchorPane connector = ((AnchorPane)e.getSource());
            LinkController.addTarget2Link(this, connector); LinkController.createConnection();
            e.setDropCompleted(true);
            e.consume();
        };

        /**
         * Обработчик неуспешного соединения
         */
        linkDragDone = e -> {
            getParent().setOnDragOver(null); getParent().setOnDragDropped(null);
            LinkController.createConnection(); LinkController.hideTemporaryLink();
            e.consume();
        };
    }


    /**
     * Обработчики для панели
     * @param node - панель для обработки
     */
    private void buildClickHandlers(Node node){
        node.addEventFilter(MouseEvent.MOUSE_PRESSED, e ->{ if(e.getButton() == MouseButton.PRIMARY) ProjectController.setSelectedNode(this); /*if(e.getClickCount()==2) openEditor();*/ });
//        node.setCursor(Cursor.HAND);
        if(cmElement==null){
            cmElement = new ContextMenu();
//            MenuItem parameter = new MenuItem("Параметры");
            MenuItem remove = new MenuItem("Удалить");
            cmElement.getItems().addAll( remove);

            remove.setOnAction(e -> remove());
//            parameter.setOnAction(e -> openEditor());
        }
        node.setOnContextMenuRequested(e -> ContextMenuController.showContextMenu(cmElement, e));
    }

    private void openEditor(){ EditDialog.show("тут нужно передать обьект для редактирования"); }

    /**
     * Удалить данный элемент из проекта
     */
    public void remove(){ ArrayList<Link> temp = new ArrayList<>(links); for(Link l:temp) l .remove(); if(getParent()!=null) ((Pane)getParent()).getChildren().remove(this); }

    @Override
    public void relocate(double x, double y) { super.relocate(x, y); this.x=x; this.y=y; }
    public void updateGrid(){ relocate(Math.round(x/grid)*grid, Math.round(y/grid)*grid); } // округляет координаты до решетки


    public void registerLink(Link link) { links.add(link); }
    public void removeLink(Link link) { links.remove(link); }

    public boolean isSelected() { return selected.get(); }
    public BooleanProperty selectedProperty() { return selected; }
    public void setSelected(boolean selected) { this.selected.set(selected); }

    public boolean isDraggable() { return draggable.get(); }
    public BooleanProperty draggableProperty() { return draggable; }
    public void setDraggable(boolean draggable) { this.draggable.set(draggable); }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public void setName(String name) { this.name.set(name); }
}

