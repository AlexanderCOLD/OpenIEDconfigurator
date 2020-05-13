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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
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
    private static final String style = "-fx-background-color: -fx-second-color; -fx-border-color: -fx-first-color; -fx-border-radius: 7; -fx-background-radius: 7; -fx-border-width: 1.5";
    private static final DropShadow selectedShadow = new DropShadow( BlurType.ONE_PASS_BOX, Color.RED, 10, 0.0, 0, 0);
    private static final DropShadow shadow = new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(0,0,0,0.8), 15, 0.0, 0, 0);

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
        setStyle(style);
        setEffect(shadow);
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
            for(DO inputDO: ln.getDataSetInput().getDataObject()) if(!inputDO.getDataAttributeName().contains("set_")) inputDOList.add(inputDO); else settings.add(inputDO); // Отфильтровываем ненужное, выделяем уставки

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
                    Connector connector = new Connector(this, inputDO, ln.getDataSetInput(), ConnectorType.inputConnector, ConnectorPosition.left);
                    borderPane.setLeft(connector.getConnectorPane());
                    connectors.add(connector);
                }
                if(outputDOList.size()>0){
                    DO outputDO = outputDOList.get(0); outputDOList.remove(0);
                    Connector connector = new Connector(this, outputDO, ln.getDataSetOutput(), ConnectorType.outputConnector, ConnectorPosition.right);
                    borderPane.setRight(connector.getConnectorPane());
                    connectors.add(connector);
                }

            }
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
                    Connector connector = new Connector(this, ds.getDataObject().get(i), ds, ConnectorType.inputConnector, ConnectorPosition.left);
                    borderPane.setLeft(connector.getConnectorPane());
                    connectors.add(connector);
                }
                else if(ds.getType() == DSType.GOOSE_Input) {
                    Connector connector = new Connector(this, ds.getDataObject().get(i), ds, ConnectorType.outputConnector, ConnectorPosition.right);
                    borderPane.setRight(connector.getConnectorPane());
                    connectors.add(connector);
                }
            }
        }
        title_bar.setText(value.toString());
    }

    @FXML
    private void initialize() {
        selected.addListener((o, ov, nv) ->{ if(nv) { setEffect(selectedShadow); setStyle(selectedStyle); } else { setEffect(shadow); setStyle(style); } });

        MenuItem remove = new MenuItem("Удалить");
        contextMenu.getItems().addAll( remove);
        remove.setOnAction(e -> remove());

       setOnContextMenuRequested(e -> ContextMenuController.showContextMenu(contextMenu, e));
    }

    /**
     * Удалить данный элемент из проекта
     */
    public void remove(){
        ArrayList<Link> temp = new ArrayList<>(connections);
        for(Link l:temp) l .remove();
        if(getParent()!=null) ((Pane)getParent()).getChildren().remove(this);
        writeLayout2UserData(-1.0, -1.0);
    }

    /**
     * Округлить координаты
     */
    public void updateGrid(){
        int grid = 30;
        relocate(Math.round(getLayoutX()/ grid)* grid, Math.round(getLayoutY()/ grid)* grid);
        writeLayout2UserData(getLayoutX(), getLayoutY());
    }

    /**
     * Обновить координаты
     */
    public void updatePosition() {
        relocate(getLayoutX() + 1, getLayoutY() + 1);
        updateGrid();
    }

    /**
     * Записать текущие координаты в объект
     * @param layoutX - координата X
     * @param layoutY - координата Y
     */
    private void writeLayout2UserData(double layoutX, double layoutY){
        if(getUserData().getClass()==LN.class) { LN ln = (LN) getUserData(); ln.setLayoutX(layoutX); ln.setLayoutY(layoutY); }
        else if (getUserData().getClass()==DS.class) { DS ds = (DS) getUserData(); ds.setLayoutX(layoutX); ds.setLayoutY(layoutY); }
        else System.err.println("Layout is not updated");
    }

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

