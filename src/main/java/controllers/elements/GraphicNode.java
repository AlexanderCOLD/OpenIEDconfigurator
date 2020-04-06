package controllers.elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import controllers.ContextMenuController;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Графический элемент
 */
public class GraphicNode extends AnchorPane {

    @FXML private Label title_bar;
    @FXML private AnchorPane mainPanel;
    @FXML private VBox vbox;

    private ContextMenu cmElement;
    private double currentX, currentY, offsetX, offsetY;
    private EventHandler <MouseEvent> linkHandleDragDetected;
    private EventHandler <DragEvent> linkHandleDragDropped, linkDragOver, linkDragDropped;
    private EventHandler <MouseEvent> mousePressed, mouseDragged;

    private StringProperty name = new SimpleStringProperty();
    private BooleanProperty selected = new SimpleBooleanProperty(false);
    private BooleanProperty draggable = new SimpleBooleanProperty(false);

    private ArrayList<AnchorPane> leftConnectors = new ArrayList<>();
    private ArrayList<AnchorPane> rightConnectors = new ArrayList<>();

    private ClipboardContent content = new ClipboardContent();
    private boolean overPanel = false;
    private ArrayList<Link> links = new ArrayList<>();

    private double x, y;

    public GraphicNode() {
        String path = "/view/FXML/GraphicNode.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try { fxmlLoader.load(); } catch (IOException exception) { throw new RuntimeException(exception); }
        setId(UUID.randomUUID().toString());
        content.put(new DataFormat(), new DragContainer());

//        buildNodeHandlers();
//        buildLinkHandlers();
//        setOnDragDetected(nodeDragDetected);
//        buildDragPanelHandlers(this);

        buildHandlers();
    }

    @Override
    public void setUserData(Object value) {
        super.setUserData(value);
        if(value.getClass()==LN.class){
            LN ln = (LN) value;
            int size = Math.max(ln.getDataSetInput().getDataObject().size(), ln.getDataSetOutput().getDataObject().size());
            for(int i=0; i<size; i++) vbox.getChildren().add(new BorderPane());
            addConnector(ln.getDataSetInput(), leftConnectors);
            addConnector(ln.getDataSetOutput(), rightConnectors);
            title_bar.setText(ln.getName());
        }
    }

    @FXML
    private void initialize() {

        selected.addListener((o, ov, nv) -> { System.out.println("Selected: "+ nv); });
        draggable.addListener((o, ov, nv) -> { if(nv) addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseDragged); else removeEventFilter(MouseEvent.MOUSE_DRAGGED, mouseDragged); });
    }

    private void addConnector(DS dataSet, ArrayList<AnchorPane> connectorsList){
        for(DO dataObject:dataSet.getDataObject()){
            AnchorPane pane = new AnchorPane(); pane.setStyle("-fx-background-color: transparent");
            AnchorPane connector = new AnchorPane(); connector.setStyle("-fx-background-color: WHITE; -fx-background-radius: 5"); connector.setPrefSize(7,7);
            Label label = new Label(dataObject.getDataAttributeName()!=null ? String.format("%s (%s)", dataObject.getDataAttributeName(), dataObject.getDataObjectName()) : String.format("(%s)", dataObject.getDataObjectName()));
            label.setTextFill(Color.WHITE); /*label.setFont(Font.font(10.0));*/
            pane.getChildren().add(connector);
            pane.getChildren().add(label);
            BorderPane bp = (BorderPane) vbox.getChildren().get(connectorsList.size());

            AnchorPane.setTopAnchor(connector, 8.0);
            AnchorPane.setTopAnchor(label, 3.0); AnchorPane.setLeftAnchor(label, 7.0); AnchorPane.setRightAnchor(label, 7.0);

            if(connectorsList==leftConnectors){ AnchorPane.setLeftAnchor(connector, -4.0); label.setAlignment(Pos.CENTER_LEFT); bp.setLeft(pane); }
            else{ AnchorPane.setRightAnchor(connector, -4.0); label.setAlignment(Pos.CENTER_RIGHT); bp.setRight(pane); }

            connectorsList.add(connector);

            connector.setOnMouseEntered(e->{ connector.setStyle("-fx-background-color: RED; -fx-background-radius: 5"); connector.setPrefSize(8,8); });
            connector.setOnMouseExited(e-> { connector.setStyle("-fx-background-color: WHITE; -fx-background-radius: 5"); connector.setPrefSize(7,7); } );
            connector.setOnDragEntered(e->{ connector.setStyle("-fx-background-color: RED; -fx-background-radius: 5"); connector.setPrefSize(8,8); });
            connector.setOnDragExited(e-> { connector.setStyle("-fx-background-color: WHITE; -fx-background-radius: 5"); connector.setPrefSize(7,7); } );
            connector.setOnDragDetected(linkHandleDragDetected);
            connector.setOnDragDropped(linkHandleDragDropped);
        }
    }

    private void buildHandlers(){
        addEventFilter(MouseEvent.MOUSE_PRESSED, e->{
            offsetX = e.getX(); offsetY = e.getY();
        });
    }


    /**
     * Обработчики для панели
     * @param dragPanel - прозрачная панель (SVGPath)
     */
    private void buildDragPanelHandlers(Node dragPanel){
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
     * Обработчики перемещения элемента
     */
    public void buildNodeHandlers() {
        mousePressed =  e->{ offsetX = e.getX(); offsetY = e.getY(); };
        mouseDragged = e->{ relocate(getLayoutX() + (e.getX()-offsetX), getLayoutY() + (e.getY()-offsetY)); };
    }

    /**
     * Обработчики соединений
     */
    private void buildLinkHandlers() {
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
    }


    @Override
    public void relocate(double x, double y) { super.relocate(x, y); currentX=x; currentY=y; }

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

