package controllers.graphicNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import controllers.ContextMenuController;
import controllers.link.Link;
import iec61850.DO;
import iec61850.DS;
import iec61850.DSType;
import iec61850.LN;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
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

    private static final String selectedStyle = "-fx-background-color: -fx-second-color; -fx-border-color: #dc4b48; -fx-border-radius: 7; -fx-background-radius: 7; -fx-border-width: 2";
    private static final String unselectedStyle = "-fx-background-color: -fx-second-color; -fx-border-color: -fx-first-color; -fx-border-radius: 7; -fx-background-radius: 7; -fx-border-width: 1.5";

    private final ContextMenu contextMenu = new ContextMenu();

    private final StringProperty name = new SimpleStringProperty();
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    private final ArrayList<Connector> connectors = new ArrayList<>(); // Все коннекторы
    private final ArrayList<Link> connections = new ArrayList<>(); // Зарегистрированные соединения
    private final ArrayList<DO> settings = new ArrayList<>(); // DO содержащие настройки (уставки)

    public GraphicNode() { loadGraphic(); }
    public GraphicNode(Object userData) { loadGraphic(); setUserData(userData); }

    /**
     * Загрузка графического шаблона
     */
    private void loadGraphic(){
        String path = "/view/FXML/GraphicNode.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try { fxmlLoader.load(); } catch (IOException exception) { throw new RuntimeException(exception); }
        setId(UUID.randomUUID().toString());
        setStyle(unselectedStyle);
    }

    /**
     * Устанавливанет Model (LN, Dataset ...)
     * @param value - объект
     */
    @Override
    public void setUserData(Object value) {
        if(getUserData()!=null){ System.err.println("User data is already exists"); return; }
        super.setUserData(value);
        setMaxHeight(100);
        mainPanel.getChildren().clear();
        mainPanel.setStyle("-fx-background-color: -fx-third-color");


        if(value.getClass()==LN.class){
            LN ln = (LN) value;

            ArrayList<DO> inputDOList = new ArrayList<>();
            ArrayList<DO> outputDOList = new ArrayList<>(ln.getDataSetOutput().getDataObject());
            for(DO inputDO: ln.getDataSetInput().getDataObject()) if(!inputDO.getCppAttributeName().contains("set_")) inputDOList.add(inputDO); else settings.add(inputDO); // Отфильтровываем ненужное, выделяем уставки

            int size = Math.max(inputDOList.size(), outputDOList.size());

            for(int i=0; i<size; i++) {
                BorderPane borderPane = new BorderPane();
                mainPanel.getChildren().add(borderPane);
                AnchorPane.setLeftAnchor(borderPane,0.0);
                AnchorPane.setRightAnchor(borderPane,0.0);
                AnchorPane.setTopAnchor(borderPane,15.0*i);
//                borderPane.setStyle("-fx-border-color: RED; -fx-border-width: 1");

                if(inputDOList.size()>0){
                    DO inputDO = inputDOList.get(0); inputDOList.remove(0);
                    borderPane.setLeft(createLNConnector(ln.getDataSetInput(), inputDO, ConnectorType.inputConnector, ConnectorType.inputConnector));
                }
                if(outputDOList.size()>0){
                    DO outputDO = outputDOList.get(0); outputDOList.remove(0);
                    borderPane.setRight(createLNConnector(ln.getDataSetOutput(), outputDO, ConnectorType.outputConnector, ConnectorType.outputConnector));
                }

            }
            title_bar.setText(ln.getName());
        }
        else if(value.getClass()==DS.class){
            DS ds = (DS) value;

            for(int i=0; i<ds.getDataObject().size(); i++) {
                BorderPane borderPane = new BorderPane();
                mainPanel.getChildren().add(borderPane);

                AnchorPane.setLeftAnchor(borderPane,0.0);
                AnchorPane.setRightAnchor(borderPane,0.0);
                AnchorPane.setTopAnchor(borderPane,15.0*i);
//                borderPane.setStyle("-fx-border-color: RED; -fx-border-width: 1");

                if(ds.getType() == DSType.GOOSE_Output || ds.getType() == DSType.MMS_Output) {
                    borderPane.setLeft(createLNConnector(ds, ds.getDataObject().get(i), ConnectorType.outputConnector, ConnectorType.inputConnector));
                }
                else if(ds.getType() == DSType.GOOSE_Input) {
                    borderPane.setRight(createLNConnector(ds, ds.getDataObject().get(i), ConnectorType.inputConnector, ConnectorType.outputConnector));
                }
            }
            title_bar.setText(ds.getName());
        }
    }

    /**
     * Создает панель с коннектором и лейблом
     * @param dataSet - датасет
     * @param connectorType - тип датаСета (входной, выходной)
     * @param position - позиция (нужна т.к. у DS коннекторы с противоположной стороны)
     */
    private AnchorPane createLNConnector(DS dataSet, DO dataObject, ConnectorType connectorType, ConnectorType position){
        AnchorPane connectorPane = new AnchorPane(); connectorPane.setStyle("-fx-background-color: transparent");

        Connector connector = new Connector(this, dataObject, dataSet, connectorType);
        connectorPane.getChildren().add(connector);

        Label label = new Label(String.format("%s (%s)", dataObject.getDataAttributeName(), dataObject.getDataObjectName())); label.setTextFill(Color.WHITE); /*label.setFont(Font.font(10.0));*/
        connectorPane.getChildren().add(label);
        AnchorPane.setTopAnchor(label, 3.0); AnchorPane.setLeftAnchor(label, 7.0); AnchorPane.setRightAnchor(label, 7.0);

        AnchorPane.setTopAnchor(connector, 8.0);
        if(position==ConnectorType.inputConnector){ AnchorPane.setLeftAnchor(connector, -5.0); label.setAlignment(Pos.CENTER_LEFT); }
        else{ AnchorPane.setRightAnchor(connector, -5.0); label.setAlignment(Pos.CENTER_RIGHT); }

        connectors.add(connector);
        return connectorPane;
    }

    @FXML
    private void initialize() {
        selected.addListener((o, ov, nv) ->{ if(nv) setStyle(selectedStyle); else setStyle(unselectedStyle); });

//        node.addEventFilter(MouseEvent.MOUSE_PRESSED, e ->{ if(e.getButton() == MouseButton.PRIMARY) ProjectController.setSelectedNode(this); /*if(e.getClickCount()==2) openEditor();*/ });
//        node.setCursor(Cursor.HAND);

//        MenuItem parameter = new MenuItem("Параметры");
        MenuItem remove = new MenuItem("Удалить");
        contextMenu.getItems().addAll( remove);
        remove.setOnAction(e -> remove());

       setOnContextMenuRequested(e -> ContextMenuController.showContextMenu(contextMenu, e));
    }

    /**
     * Удалить данный элемент из проекта
     */
    public void remove(){ ArrayList<Link> temp = new ArrayList<>(connections); for(Link l:temp) l .remove(); if(getParent()!=null) ((Pane)getParent()).getChildren().remove(this); }

    /**
     * Округлить координаты
     */
    public void updateGrid(){ int grid = 30; relocate(Math.round(getLayoutX()/ grid)* grid, Math.round(getLayoutY()/ grid)* grid); }

    public ArrayList<Link> getConnections() { return connections; }
    public ArrayList<Connector> getConnectors() { return connectors; }
    public ArrayList<DO> getSettings() { return settings; }

    public boolean isSelected() { return selected.get(); }
    public BooleanProperty selectedProperty() { return selected; }
    public void setSelected(boolean selected) { this.selected.set(selected); }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public void setName(String name) { this.name.set(name); }
}

