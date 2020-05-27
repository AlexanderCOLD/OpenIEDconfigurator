package controllers.dialogs;

import application.GUI;
import application.Main;
import controllers.ResizeController;
import iec61850.IECObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Информационная панель
 */

public class IECInfoDialog extends AnchorPane {

    @FXML private TreeTableView<Object> treeTableView;
    @FXML private ToggleButton lock;
    @FXML private Accordion accord;
    @FXML private TreeTableColumn<Object,String> nameColumn, descColumn, valueColumn;

    private static final TreeItem<Object> root = new TreeItem<>();

    private static IECInfoDialog self = new IECInfoDialog();
    private static final Image iedIcon, ldIcon, lnIcon, dsIcon, doIcon, daIcon, dpIcon;
    private double dragOffsetX, dragOffsetY; // поправка при перетаскивании на позицию мышки
    private double localX, localY; // координаты относительно главного окна
    private boolean draggable = false;
    private final EventHandler<? super MouseEvent> mouseDragged, mousePressed;
    private final ChangeListener<? super Number> xListener, yListener;
    private final Stage stage = new Stage();

    private static final HashMap<String, ArrayList<TreeItem<Object>>> branchBuffer = new HashMap<>();
    private static HashMap<String, ArrayList<TreeItem<Object>>> branchTemp;

    static {
        iedIcon = new Image(Main.class.getResource("/view/image/IEDIcon.png").toString()); fillBuffer(10, "IED", iedIcon);
        ldIcon = new Image(Main.class.getResource("/view/image/LDIcon.png").toString());   fillBuffer(20, "LD", ldIcon);
        lnIcon = new Image(Main.class.getResource("/view/image/LNIcon.png").toString());   fillBuffer(500, "LN", lnIcon);
        dsIcon = new Image(Main.class.getResource("/view/image/DSIcon.png").toString());   fillBuffer(500, "DS", dsIcon);
        doIcon = new Image(Main.class.getResource("/view/image/DOIcon.png").toString());   fillBuffer(10000, "DO", doIcon);
        daIcon = new Image(Main.class.getResource("/view/image/DAIcon.png").toString());   fillBuffer(10000, "DA", daIcon);
        dpIcon = new Image(Main.class.getResource("/view/image/DPIcon.png").toString());   fillBuffer(10000, "IECProperty", dpIcon);
    }

    /** Создание буффера */
    static void fillBuffer(int numb, String name, Image icon){
        ArrayList<TreeItem<Object>> buffer = new ArrayList<>();
        IntStream.range(0, numb).forEach(v ->{
            ImageView iv = new ImageView(icon); iv.setFitWidth(20); iv.setFitHeight(20);
            TreeItem<Object> item = new TreeItem<>(); item.setGraphic(iv);
            buffer.add(item);
        });
        branchBuffer.put(name, buffer);
    }

    public IECInfoDialog() {
        self = this;

        xListener = (e, ov, nv)->{ stage.setX(nv.doubleValue() + localX); };
        yListener = (e, ov, nv)->{ stage.setY(nv.doubleValue() + localY); };
        mousePressed = e->{
            dragOffsetX = e.getScreenX() - stage.getX(); dragOffsetY = e.getScreenY() - stage.getY();
            if(dragOffsetY<7 || dragOffsetY>25 || dragOffsetX<7 || dragOffsetX>(stage.getWidth()-7)) draggable = false; else draggable = true; // Границы перетаскивания
        };
        mouseDragged = e->{
            if(!draggable) return;
            stage.setX(e.getScreenX() - this.dragOffsetX); stage.setY(e.getScreenY() - this.dragOffsetY);
        };

        FXMLLoader fxmlLoader = new FXMLLoader(	getClass().getResource("/view/FXML/IECInfoDialog.fxml") );
        fxmlLoader.setRoot(this); fxmlLoader.setController(this);
        try { fxmlLoader.load(); } catch (IOException exception) { throw new RuntimeException(exception); }
        ImageView icon = new ImageView(new Image(Main.class.getResource("/view/image/Icon.png").toString())); icon.setFitWidth(20); icon.setFitHeight(20); getChildren().add(icon); icon.setLayoutX(4); icon.setLayoutY(5);
        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(this);
        stage.setScene(scene);
        stage.initOwner(GUI.getStage());
        scene.getStylesheets().add("view/CSS/" + GUI.colorStyle + ".css");
        scene.getStylesheets().add("view/CSS/stylesheet.css");
        ResizeController.addStage(stage);
    }

    @FXML
    private void initialize() {
        accord.setExpandedPane(accord.getPanes().get(0));
        stage.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressed);
        lock.setOnMouseClicked(e-> setLock(lock.isSelected()) );
        setLock(false);

        nameColumn.setCellValueFactory(param -> {
            if(param.getValue().getValue().getClass()==IECProperty.class){
                IECProperty iecProperty = (IECProperty) param.getValue().getValue();
                return new SimpleStringProperty(String.format("%s (%s)", iecProperty.getName(), iecProperty.getType()));
            }
            else{
                try {
                    IECObject iecObject = (IECObject) param.getValue().getValue();
                    return new SimpleStringProperty(String.format("%s (%s)", iecObject.getName(), iecObject.getType()));
                } catch (Exception ignored) { }
                return null;
            }
        });
        descColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        valueColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("value"));

        treeTableView.setRoot(root); treeTableView.setShowRoot(false);
    }

    /** Show data of object */
    public static void setObject(IECObject object){
        self.stage.show();
        if(!self.stage.isShowing()) return;
        /* Буффер ветвей */
        branchTemp = new HashMap<>();
        for(Map.Entry<String, ArrayList<TreeItem<Object>>> entry: branchBuffer.entrySet())
            branchTemp.put(entry.getKey(), new ArrayList<>(entry.getValue()));

        root.getChildren().clear(); root.setExpanded(true);
        root.getChildren().add(takeBranch(new IECProperty("Prop", "Type", "Тип объекта", object.getType())));
        root.getChildren().add(takeBranch(new IECProperty("Prop", "Name", "Название объекта", object.getName())));
        root.getChildren().add(takeBranch(new IECProperty("Prop", "Desc", "Описание объекта", object.getDescription())));
        root.getChildren().add(takeBranch(new IECProperty("Prop", "Parent", "Родительский объект", object.getParent()!=null ? object.getParent().toString() : null)));
        root.getChildren().add(takeBranch(new IECProperty("Prop", "Coord", "Координаты", "["+object.getLayoutX()+ " : " +object.getLayoutY()+"]")));
        root.getChildren().add(takeBranch(new IECProperty("Prop", "Tags", "Тэги объекта", object.getTags().toString())));

        fillTree(root, object.getChildren());
    }

    /** Наполнить дерево объектами */
    private static void fillTree(TreeItem<Object> root, ObservableList<IECObject> children){
        for(IECObject iecObject: children){
            TreeItem<Object> item = takeBranch(iecObject); root.getChildren().add(item); item.setExpanded(true);
            if(!iecObject.getChildren().isEmpty()) fillTree(item, iecObject.getChildren());
        }
    }

    /** Взять ветку из буффера */
    public static TreeItem<Object> takeBranch(Object iecObject){
        ArrayList<TreeItem<Object>> itemList = branchTemp.get(iecObject.getClass().getSimpleName());
        TreeItem<Object> item = itemList.get(0); item.setValue(iecObject); item.getChildren().clear();
        itemList.remove(0);
        return item;
    }

    @FXML private void close(){ setShowing(false); }
    public static boolean isLock(){ return self.lock.isSelected(); }
    public static void setLock(boolean state){
        self.lock.setSelected(state);
        if(state) {
            self.lock.setStyle("-fx-background-color: transparent; -fx-text-fill: RED; ");
            self.localX =  self.stage.getX() - GUI.getStage().getX();
            self.localY =  self.stage.getY() - GUI.getStage().getY();
            self.stage.removeEventFilter(MouseEvent.MOUSE_DRAGGED, self.mouseDragged);
            GUI.getStage().xProperty().addListener(self.xListener);
            GUI.getStage().yProperty().addListener(self.yListener);
        }
        else {
            self.lock.setStyle("-fx-background-color: transparent; -fx-text-fill: WHITE; ");
            self.stage.addEventFilter(MouseEvent.MOUSE_DRAGGED, self.mouseDragged);
            GUI.getStage().xProperty().removeListener(self.xListener);
            GUI.getStage().yProperty().removeListener(self.yListener);
        }
    }

    public static void setLayout(double x, double y){ self.stage.setX(x); self.stage.setY(y); }
    public static double[] getLayout(){ return new double[] { self.stage.getX(), self.stage.getY() }; }

    public static void setResolution(double x, double y){ self.stage.setWidth(x); self.stage.setHeight(y); }
    public static double[] getResolution(){ return new double[] { self.stage.getWidth(), self.stage.getHeight() }; }

    public static boolean isShowing(){ return self.stage.isShowing(); }
    public static void setShowing(boolean state){ if(!state) self.stage.hide(); else self.stage.show(); }
    public static void switchVisibility(){ if(self.stage.isShowing()) self.stage.hide(); else self.stage.show(); }

    public static IECInfoDialog get(){ return self; }

    @Getter @Setter
    public static class IECProperty {
        private String type;
        private String name;
        private String description;
        private String value;
        public IECProperty(String type, String name, String description, String value) { this.type = type; this.name = name; this.description = description; this.value = value; }
        public String toString(){ return String.format("%s (%s)", name, type); }
    }
}

