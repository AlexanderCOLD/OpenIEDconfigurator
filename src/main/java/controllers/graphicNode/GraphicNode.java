package controllers.graphicNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import application.GUI;
import controllers.dialogs.AssistantDialog;
import controllers.dialogs.TripPointDialog;
import controllers.link.Link;
import controllers.tree.TreeController;
import iec61850.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
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

    private static final String selectedStyle = "-fx-background-color: -fx-second-color; -fx-border-color: #cd0733; -fx-border-radius: 7; -fx-background-radius: 7; -fx-border-width: 2";
    private static final DropShadow selectedShadow = new DropShadow( BlurType.ONE_PASS_BOX, Color.RED, 15, 0.0, 0, 0);

    private static final String selectedAddStyle = "-fx-background-color: -fx-second-color; -fx-border-color: #24dc26; -fx-border-radius: 7; -fx-background-radius: 7; -fx-border-width: 2";
    private static final DropShadow selectedAddShadow = new DropShadow( BlurType.ONE_PASS_BOX, Color.GREEN, 15, 0.0, 0, 0);

    private static final String style = "-fx-background-color: -fx-second-color; -fx-border-color: -fx-first-color; -fx-border-radius: 7; -fx-background-radius: 7; -fx-border-width: 1.5";
    private static final DropShadow shadow = new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(0,0,0,0.8), 15, 0.0, 0, 0);

    private final ContextMenu contextMenu = new ContextMenu();
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    private final ArrayList<Connector> connectors = new ArrayList<>(); // Все коннекторы
    private final ArrayList<Link> connections = new ArrayList<>(); // Зарегистрированные соединения
    private boolean hasSettins = false; // уставки / настройки

    /** Основной объект */
    private IECObject iecObject;

    public GraphicNode() { loadGraphic(); }
    public GraphicNode(IECObject iecObject) { loadGraphic(); setIecObject(iecObject); }

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
        setMaxHeight(100);
        mainPanel.setStyle("-fx-background-color: -fx-third-color");
    }

    /**
     * Устанавливанет Model (LN, Dataset ...)
     * @param value - объект
     */
    public void setIecObject(IECObject value) {
        if(iecObject!=null){ System.err.println("IEC object is already exists"); return; }
        this.iecObject = value;

        setId(value.getUID());
        title_bar.setText(value.toString());

        connectors.clear();
        mainPanel.getChildren().clear();

        /* Далее добавляются коннекторы */
        if(value.getClass()==LN.class){
            LN ln = (LN) value;

            /* Фильруем входные структуры */
            ArrayList<IECObject> inputDOList = ln.getDataObjects().stream().filter(aDo -> aDo.getCppName().contains("in_")).collect(Collectors.toCollection(ArrayList::new));
            ArrayList<IECObject> inputDAList = ln.getDataAttributes().stream().filter(aDo -> aDo.getCppName().contains("in_")).collect(Collectors.toCollection(ArrayList::new));

            /* Фильруем выходные структуры */
            ArrayList<IECObject> outputDOList = ln.getDataObjects().stream().filter(aDo -> aDo.getCppName().contains("out_")).collect(Collectors.toCollection(ArrayList::new));
            ArrayList<IECObject> outputDAList = ln.getDataAttributes().stream().filter(aDo -> aDo.getCppName().contains("out_")).collect(Collectors.toCollection(ArrayList::new));


            IECObject setDO = ln.getDataObjects().stream().filter(aDo -> aDo.getCppName().contains("set_")).findFirst().orElse(null);
            IECObject setDA = ln.getDataAttributes().stream().filter(aDo -> aDo.getCppName().contains("set_")).findFirst().orElse(null);

            /* Наличие уставок / настроек */
            hasSettins = (setDO!=null || setDA!=null);

            /* Добавляем входящие коннекторы */
            for(IECObject iecObject: inputDOList) appendConnector(iecObject, ConnectorType.inputConnector, ConnectorPosition.left);
            for(IECObject iecObject: inputDAList) appendConnector(iecObject, ConnectorType.inputConnector, ConnectorPosition.left);

            /* Добавляем исходящие коннекторы */
            for(IECObject iecObject: outputDOList) appendConnector(iecObject, ConnectorType.outputConnector, ConnectorPosition.right);
            for(IECObject iecObject: outputDAList) appendConnector(iecObject, ConnectorType.outputConnector, ConnectorPosition.right);
        }
        else if(value.getClass()==DS.class){
            DS ds = (DS) value;

            /* Если GOOSE_Output или MMS_Output -> входящие и слева */
            ConnectorType type = ConnectorType.inputConnector;
            ConnectorPosition pos = ConnectorPosition.left;

            /* Если GOOSE_Input -> исходящие и справа */
            if(DSType.GOOSE_IN.toString().equals(ds.getType()) || DSType.SVCB.toString().equals(ds.getType())){
                type = ConnectorType.outputConnector; pos = ConnectorPosition.right;
            }

            /* Добавляем коннекторы */
            for(IECObject object:ds.getDataObjects()) appendConnector(object, type, pos);
            for(IECObject object:ds.getDataAttributes()) appendConnector(object, type, pos);
        }
    }

    /**
     * Добавить в данный графической элемент новйы коннектор
     * @param object - объект данных - DO / DA
     * @param type - тип входящий / исходящий
     * @param position - слева / справа
     */
    private void appendConnector(IECObject object, ConnectorType type, ConnectorPosition position){
        /* Поиск свободной панели, на которой можно разместить коннектор */
        BorderPane borderPane = (BorderPane) mainPanel.getChildren().stream().filter(node -> {
            if(node.getClass()==BorderPane.class) {
                if(position==ConnectorPosition.left && ((BorderPane) node).getLeft()==null) return true;
                if(position==ConnectorPosition.right && ((BorderPane) node).getRight()==null) return true;
            }
            return false;
        }).findFirst().orElse(null);

        /* Если свободной нет, добавить и разместить на главной панели */
        if(borderPane==null){
            borderPane = new BorderPane(); mainPanel.getChildren().add(borderPane);
            AnchorPane.setLeftAnchor(borderPane,0.0); AnchorPane.setRightAnchor(borderPane,0.0);
            AnchorPane.setTopAnchor(borderPane,15.0 * (mainPanel.getChildren().size()-1));
        }
//      borderPane.setStyle("-fx-border-color: RED; -fx-border-width: 1");

        /* Создаем коннектор и размещаем */
        Connector c = new Connector(this, object, type, position);
        if(position==ConnectorPosition.left) borderPane.setLeft(c.getConnectorPane()); else borderPane.setRight(c.getConnectorPane());
        connectors.add(c);
    }

    @FXML
    private void initialize() {
        setCursor(Cursor.HAND);
        addEventFilter(MouseEvent.MOUSE_CLICKED, e->{ if(e.getClickCount()==2 && hasSettins) TripPointDialog.show((LN) iecObject); });

        selected.addListener((o, ov, nv) ->{
            if(nv) {
                if(!iecObject.getTags().contains("additional")) { setEffect(selectedShadow); setStyle(selectedStyle); }
                else { setEffect(selectedAddShadow); setStyle(selectedAddStyle); }
            }
            else { setEffect(shadow); setStyle(style); }
        });

        MenuItem settings = new MenuItem("Параметры");
        MenuItem rename = new MenuItem("Переименовать");
        MenuItem remove = new MenuItem("Удалить");

        contextMenu.getItems().addAll(settings, rename, remove);

        contextMenu.setOnShowing(e -> {
            contextMenu.getItems().clear();

            if(hasSettins) contextMenu.getItems().add(settings);
            if(iecObject.getTags().contains("additional")) contextMenu.getItems().add(rename);
            contextMenu.getItems().add(remove);
        });

        settings.setOnAction(e -> { TripPointDialog.show((LN) iecObject); });

        rename.setOnAction(e->{
            String name = AssistantDialog.requestText("Новое название", "Введите новое название", iecObject.getName());
            if(name!=null) { iecObject.setName(name); title_bar.setText(iecObject.toString()); TreeController.refresh(); }
        });

        remove.setOnAction(e -> { if(AssistantDialog.requestConfirm("Подтверждение", "Удалить элемент " + iecObject.getName())) remove(); });

        setOnContextMenuRequested(e -> {
            if(GUI.getCurrentContextMenu()!=null) GUI.getCurrentContextMenu().hide();
            contextMenu.show(GUI.get(), e.getScreenX(), e.getScreenY());
            GUI.setCurrentContextMenu(contextMenu);
        });
    }

    /**
     * Удалить данный элемент из проекта
     */
    public void remove(){

        /* Если это дополнительный элемент, удаляем из проекта */
        if(iecObject.getTags().contains("additional")){
            LD ld = CLDUtils.parentOf(LD.class, iecObject);
            if(iecObject.getClass()==LN.class) ld.getLogicalNodeList().remove((LN) iecObject);
            else if(iecObject.getClass()==DS.class) ld.getDataSets().remove((DS) iecObject);
            GraphicNodeController.getProjectNodeList().remove(iecObject.getUID());
        }

        /* Удаляем все соединения */
        for(Link link: new ArrayList<>(connections)) link.remove();

        /* Обнуляем координаты */
        iecObject.setLayoutX(null); iecObject.setLayoutY(null);

        /* Непосредсвенно удаляем из графики */
        if(getParent()!=null) ((Pane)getParent()).getChildren().remove(this);
    }

    /**
     * Округлить координаты
     */
    public void updateGrid(){
        int grid = 30;
        relocate(Math.round(getLayoutX()/ grid)* grid, Math.round(getLayoutY()/ grid)* grid);
        iecObject.setLayoutX(getLayoutX()); iecObject.setLayoutY(getLayoutY());
    }

    /**
     * Активные соединения
     */
    public ArrayList<Link> getConnections() { return connections; }

    /**
     * Коннекторы элемента
     */
    public ArrayList<Connector> getConnectors() { return connectors; }

    public boolean isSelected() { return selected.get(); }
    public BooleanProperty selectedProperty() { return selected; }
    public void setSelected(boolean selected) { this.selected.set(selected); }

    /**
     * Объект МЭК 61850
     */
    public IECObject getIecObject() { return iecObject; }
}

