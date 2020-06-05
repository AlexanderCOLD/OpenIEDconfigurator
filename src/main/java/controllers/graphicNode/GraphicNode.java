package controllers.graphicNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import application.GUI;
import application.Main;
import controllers.dialogs.AssistantDialog;
import controllers.dialogs.ConnectorDialog;
import controllers.dialogs.EditorDialog;
import controllers.dialogs.OscillDialog;
import controllers.link.Link;
import controllers.tree.TreeController;
import iec61850.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Графический элемент
 */
public class GraphicNode extends AnchorPane {

    @FXML private Label title_bar;
    @FXML private AnchorPane mainPanel;
    private static final Image nodeIcon = new Image(Main .class.getResource("/view/image/node.png").toString());
    private static final String selectedStyle = "-fx-background-color: -fx-second-color; -fx-border-color: #cd0733; -fx-border-radius: 7; -fx-background-radius: 7; -fx-border-width: 2";
    private static final DropShadow selectedShadow = new DropShadow( BlurType.ONE_PASS_BOX, Color.RED, 15, 0.0, 0, 0);

    private static final String selectedAddStyle = "-fx-background-color: -fx-second-color; -fx-border-color: #24dc26; -fx-border-radius: 7; -fx-background-radius: 7; -fx-border-width: 2";
    private static final DropShadow selectedAddShadow = new DropShadow( BlurType.ONE_PASS_BOX, Color.GREEN, 15, 0.0, 0, 0);

    private static final String style = "-fx-background-color: -fx-second-color; -fx-border-color: -fx-first-color; -fx-border-radius: 7; -fx-background-radius: 7; -fx-border-width: 1.5";
    private static final DropShadow shadow = new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(0,0,0,0.8), 15, 0.0, 0, 0);

    private final Tooltip tooltip = new Tooltip(){{ setFont(Font.font(14)); }};

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
        setMaxHeight(30);
        mainPanel.setStyle("-fx-background-color: -fx-third-color");
    }

    /**
     * Устанавливанет Model (LN, Dataset ...)
     * Загрузка без коннекторов
     * @param value - объект
     */
    public void setIecSimple(IECObject value){
        this.iecObject = value;
        setId(value.getUID());
        title_bar.setText(value.getType());
        ImageView img = new ImageView(nodeIcon);
        getChildren().add(img); img.relocate(15,22);
        setMinSize(50,60);
        setMaxSize(50,60);
        connectors.clear();
        mainPanel.getChildren().clear();
    }

    /**
     * Устанавливанет Model (LN, Dataset ...)
     * @param value - объект
     */
    public void setIecObject(IECObject value) {
        if(iecObject!=null){ System.err.println("IEC object is already exists"); return; }
        this.iecObject = value;

        tooltip.setOnShowing(e -> { tooltip.setText(iecObject!=null? iecObject.getDescription() : ""); });
        tooltip.setStyle("-fx-background-color: transparent;");
        Tooltip.install(this, tooltip);

        setId(value.getUID());
        title_bar.setText(value.toString());

        connectors.clear();
        mainPanel.getChildren().clear();

        /* Далее добавляются коннекторы */
        if(value.getClass()==LN.class){
            LN ln = (LN) value;

            /* Фильруем входные структуры */
            ArrayList<IECObject> inputDOList = ln.getDataObjects().stream()
                    .filter(aDo -> aDo.getCppName().contains("in_"))
                    .collect(Collectors.toCollection(ArrayList::new));
            ArrayList<IECObject> inputDAList = ln.getDataAttributes().stream()
                    .filter(aDo -> aDo.getCppName().contains("in_"))
                    .collect(Collectors.toCollection(ArrayList::new));

            /* Фильруем выходные структуры */
            ArrayList<IECObject> outputDOList = ln.getDataObjects().stream()
                    .filter(aDo -> aDo.getCppName().contains("out_"))
                    .collect(Collectors.toCollection(ArrayList::new));
            ArrayList<IECObject> outputDAList = ln.getDataAttributes().stream()
                    .filter(aDo -> aDo.getCppName().contains("out_"))
                    .collect(Collectors.toCollection(ArrayList::new));

            /* Наличие уставок / настроек */
            settings:{
                ln.getDataObjects().stream()
                        .filter(aDo -> aDo.getCppName()!=null && aDo.getCppName().contains("set_"))
                        .findFirst().ifPresent(setDO -> hasSettins = true);
                if(hasSettins) break settings;
                ln.getDataAttributes().stream()
                        .filter(aDo -> aDo.getCppName()!=null && aDo.getCppName().contains("set_"))
                        .findFirst().ifPresent(setDA -> hasSettins = true);
            }

            /* Добавляем входящие коннекторы */
            for(IECObject iecObject: inputDOList) appendConnector(iecObject, ConnectorType.inputConnector, ConnectorPosition.left);
            for(IECObject iecObject: inputDAList) appendConnector(iecObject, ConnectorType.inputConnector, ConnectorPosition.left);

            /* Добавляем исходящие коннекторы */
            for(IECObject iecObject: outputDOList) appendConnector(iecObject, ConnectorType.outputConnector, ConnectorPosition.right);
            for(IECObject iecObject: outputDAList) appendConnector(iecObject, ConnectorType.outputConnector, ConnectorPosition.right);
        }
        else if(value.getClass()==DS.class){
            DS ds = (DS) value;

            /* Фильруем входные структуры */
            ArrayList<IECObject> doList = ds.getDataObjects().stream()
                    .filter(aDo -> !(aDo.getCppName()!=null && aDo.getCppName().contains("set_")))
                    .collect(Collectors.toCollection(ArrayList::new));
            ArrayList<IECObject> daList = ds.getDataAttributes().stream()
                    .filter(aDo -> !(aDo.getCppName()!=null && aDo.getCppName().contains("set_")))
                    .collect(Collectors.toCollection(ArrayList::new));

            /* Наличие уставок / настроек */
            settings:{
                ds.getDataObjects().stream()
                        .filter(aDo -> aDo.getCppName()!=null && aDo.getCppName().contains("set_"))
                        .findFirst().ifPresent(setDO -> hasSettins = true);
                if(hasSettins) break settings;
                ds.getDataAttributes().stream()
                        .filter(aDo -> aDo.getCppName()!=null && aDo.getCppName().contains("set_"))
                        .findFirst().ifPresent(setDA -> hasSettins = true);
            }

            /* Если GOOSE_Output или MMS_Output -> входящие и слева */
            ConnectorType type = ConnectorType.inputConnector;
            ConnectorPosition pos = ConnectorPosition.left;

            /* Если GOOSE_Input -> исходящие и справа */
            if(DSType.GOOSE_IN.toString().equals(ds.getType()) || DSType.SVCB.toString().equals(ds.getType())){
                type = ConnectorType.outputConnector; pos = ConnectorPosition.right;
            }

            /* Добавляем коннекторы */
            for(IECObject object: doList) appendConnector(object, type, pos);
            for(IECObject object: daList) appendConnector(object, type, pos);
        }
    }

    /**
     * Добавить в данный графической элемент новйы коннектор
     * @param object - объект данных - DO / DA
     * @param type - тип входящий / исходящий
     * @param position - слева / справа
     */
    private void appendConnector(IECObject object, ConnectorType type, ConnectorPosition position){
        BorderPane borderPane = takeBorder(position);
        /* Создаем коннектор и размещаем */
        Connector c = new Connector(this, object, type, position); c.toFront(); connectors.add(c);
        if(object.getLayoutX()==null || object.getLayoutY()==null) return; // Если нет координат, не отрисовываем
        if(position==ConnectorPosition.left) borderPane.setLeft(c.getConnectorPane()); else borderPane.setRight(c.getConnectorPane());
    }

    /** Поиск свободной панели, на которой можно разместить коннектор */
    private BorderPane takeBorder(ConnectorPosition position){
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
            AnchorPane.setTopAnchor(borderPane,12.0 * (mainPanel.getChildren().size()-1) + 10);
        }
//      borderPane.setStyle("-fx-border-color: RED; -fx-border-width: 1");
        return borderPane;
    }

    /** Убирает коннекторы у которых отсутствуют координаты*/
    public void refreshConnectors(){
        /* Сортировка коннекторов, очищение borderPanes */
        ArrayList<Connector> leftConnectors = connectors.stream().filter(c -> c.getPosition()==ConnectorPosition.left).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Connector> rightConnectors = connectors.stream().filter(c -> c.getPosition()==ConnectorPosition.right).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<BorderPane> borders = mainPanel.getChildren().stream().filter(n -> n.getClass()==BorderPane.class).map(BorderPane.class::cast).collect(Collectors.toCollection(ArrayList::new));
        borders.forEach(b -> { b.setLeft(null); b.setRight(null); });

        /* Заполнение свободных BorderPane коннекторами */
        for(Connector c: leftConnectors){
            if(c.getIecObject().getLayoutX()!=null) takeBorder(ConnectorPosition.left).setLeft(c.getConnectorPane());
            else for(Link link: new ArrayList<>(c.getConnections())) link.remove();
        }
        for(Connector c: rightConnectors){
            if(c.getIecObject().getLayoutX()!=null) takeBorder(ConnectorPosition.right).setRight(c.getConnectorPane());
            else for(Link link: new ArrayList<>(c.getConnections())) link.remove();
        }
        /* Если остались пустые BorderPane, удалить их */
        borders.forEach(b-> { if(b.getLeft()==null && b.getRight()==null) mainPanel.getChildren().remove(b);  });

        /* Перерисовать соединения */
        Platform.runLater(() -> connectors.forEach(c-> c.getConnections().forEach(Link::updatePosition)));
    }

    @FXML
    private void initialize() {
        setCursor(Cursor.HAND);
        addEventFilter(MouseEvent.MOUSE_CLICKED, e->{ if(e.getClickCount()==2 && hasSettins) EditorDialog.show(iecObject); });

        selected.addListener((o, ov, nv) ->{
            if(nv) {
                if(!iecObject.getTags().contains("additional")) { setEffect(selectedShadow); setStyle(selectedStyle); }
                else { setEffect(selectedAddShadow); setStyle(selectedAddStyle); }
            }
            else { setEffect(shadow); setStyle(style); }
        });

        MenuItem settings = new MenuItem("Параметры");
        MenuItem connectors = new MenuItem("Коннекторы");
        MenuItem oscill = new MenuItem("Сигналы");
        MenuItem rename = new MenuItem("Переименовать");
        MenuItem remove = new MenuItem("Удалить");

        contextMenu.getItems().addAll(settings, rename, remove);

        contextMenu.setOnShowing(e -> {
            contextMenu.getItems().clear();

            if(hasSettins) contextMenu.getItems().add(settings);
            if(iecObject.getType().equals("RDRE")) contextMenu.getItems().add(oscill);
            if(iecObject.getTags().contains("additional")) contextMenu.getItems().add(rename);
            contextMenu.getItems().addAll(connectors, remove);
        });

        settings.setOnAction(e -> { EditorDialog.show(iecObject); });
        oscill.setOnAction(e -> { OscillDialog.show(iecObject); });
        connectors.setOnAction(e ->{ ConnectorDialog.show(this); });

        rename.setOnAction(e->{
            String initName = iecObject.getType().toLowerCase(); long instance = iecObject.getInstance()!=null ? iecObject.getInstance() : 1;
            String name = AssistantDialog.requestText("Введите название", "Введите номер экземпляра", initName+"_"+instance);
            if(name!=null){
                name = name.replaceAll(" ",""); if(name.equals(initName+"_"+instance)) return;
                String[] nameSplit = name.split("_"); if(nameSplit.length!=2) return;
                if(!nameSplit[0].equals(initName)) return;
                try { instance = Long.parseLong(nameSplit[1]); } catch (Exception exc) { return; }
            } else return;
            iecObject.setName(name); iecObject.setInstance(instance);
            title_bar.setText(iecObject.toString()); TreeController.refresh();
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

        LD currentLD = CLDUtils.parentOf(LD.class, iecObject);

        /* Если это дополнительный элемент, удаляем из проекта */
        if(iecObject.getTags().contains("additional")){
            LD ld = CLDUtils.parentOf(LD.class, iecObject);
            if(iecObject.getClass()==LN.class) ld.getLogicalNodeList().remove((LN) iecObject);
            else if(iecObject.getClass()==DS.class) ld.getDataSets().remove((DS) iecObject);
            GraphicNodeController.getProjectNodeList().remove(iecObject.getUID());
        }

        /* Если RDRE и последний - удалить теги "oscillogram" */
        if(iecObject.getType().equals("RDRE")){
            ObservableList<IECObject> ldList = CLDUtils.objectListOf(currentLD);
            boolean hasRdre = ldList.stream()
                    .filter(o-> o.getClass()==LN.class)
                    .anyMatch(o-> o.getType().equals("RDRE"));
            if(!hasRdre) ldList.stream().filter(o-> o.getClass()==DA.class).forEach(o-> o.getTags().remove("oscillogram"));
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

