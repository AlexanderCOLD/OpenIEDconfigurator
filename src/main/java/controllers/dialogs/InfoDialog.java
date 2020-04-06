package controllers.dialogs;

import application.GUI;
import application.Main;
import controllers.ResizeController;
import controllers.object.SVG.FXSVGLoader;
import controllers.object.SVG.SVG;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Информационная панель
 */

public class InfoDialog extends AnchorPane {

    @FXML private Accordion accord;
    @FXML private ToggleButton lock;
    @FXML private TableView tableView;

    private static InfoDialog self;
    private boolean draggable = false;
    private double dragOffsetX, dragOffsetY; // поправка при перетаскивании на позицию мышки
    private double localX, localY; // координаты относительно главного окна
    private EventHandler<? super MouseEvent> mouseDragged, mousePressed;
    private ChangeListener<? super Number> xListener, yListener;
    private Stage stage = new Stage();
    private Scene scene = new Scene(this);
    private TableColumn<TableTest, String> name, value;

    public InfoDialog() { self = this; initializeDialog(); }

    void initializeDialog(){
        xListener = (e, ov, nv)->{ stage.setX(nv.doubleValue() + GUI.getStage().getWidth() + localX); };
        yListener = (e, ov, nv)->{ stage.setY(nv.doubleValue() + GUI.getStage().getHeight() + localY); };
        mousePressed = e->{
            dragOffsetX = e.getScreenX() - stage.getX(); dragOffsetY = e.getScreenY() - stage.getY();
            if(dragOffsetY<7 || dragOffsetY>25 || dragOffsetX<7 || dragOffsetX>(stage.getWidth()-7)) draggable = false; else draggable = true; // Границы перетаскивания
        };
        mouseDragged = e->{
            if(!draggable) return;
            stage.setX(e.getScreenX() - this.dragOffsetX); stage.setY(e.getScreenY() - this.dragOffsetY);
        };

        FXMLLoader fxmlLoader = new FXMLLoader(	getClass().getResource("/view/FXML/InfoDialog.fxml") );
        fxmlLoader.setRoot(this); fxmlLoader.setController(this);
        try { fxmlLoader.load(); } catch (IOException exception) { throw new RuntimeException(exception); }
        ImageView icon = new ImageView(new Image(Main.class.getResource("/view/image/Icon.png").toString())); icon.setFitWidth(20); icon.setFitHeight(20); getChildren().add(icon); icon.setLayoutX(4); icon.setLayoutY(5);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.initOwner(GUI.getStage());
        scene.getStylesheets().add("view/CSS/stylesheet.css");
        ResizeController.addStage(stage);
        initializeTable();
    }

    private void initializeTable(){
        name = new TableColumn<TableTest, String>("Название");
        value = new TableColumn<TableTest, String>("Значение");

        tableView.getColumns().addAll(name, value);

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        value.setCellValueFactory(new PropertyValueFactory<>("value"));

        TableTest tt1 = new TableTest(); tt1.name = "Test1"; tt1.value = "Test2";
        TableTest tt2 = new TableTest(); tt2.name = "Test3"; tt2.value = "Test4";

        tableView.getItems().addAll(tt1, tt2);
    }

    @FXML
    private void initialize() {
        accord.setExpandedPane(accord.getPanes().get(0));
        stage.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressed);
        lock.setOnMouseClicked(e-> setLock(lock.isSelected()) );


        setLock(false);
    }

    @FXML private void close(){ setShowing(false); }
    public static boolean isLock(){
        if(self==null) self = new InfoDialog();
        return self.lock.isSelected();
    }
    public static void setLock(boolean state){
        if(self==null) self = new InfoDialog();
        self.lock.setSelected(state);
        if(state) {
            self.lock.setStyle("-fx-background-color: transparent; -fx-text-fill: RED; ");
            self.localX =  self.stage.getX() - GUI.getStage().getX() - GUI.getStage().getWidth();
            self.localY =  self.stage.getY() - GUI.getStage().getY() - GUI.getStage().getHeight();
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

    public static void setLayout(double x, double y){
        if(self==null) self = new InfoDialog();
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        if(x<screenBounds.getMaxX() && y<screenBounds.getMaxY()){ self.stage.setX(x); self.stage.setY(y); }
    }
    public static double[] getLayout(){
        if(self==null) self = new InfoDialog();
        return new double[] { self.stage.getX(), self.stage.getY() };
    }

    public static void setResolution(double x, double y){
        if(self==null) self = new InfoDialog();
        self.stage.setWidth(x);
        self.stage.setHeight(y);
    }
    public static double[] getResolution(){
        if(self==null) self = new InfoDialog();
        return new double[] { self.stage.getWidth(), self.stage.getHeight() };
    }

    public static boolean isShowing(){
        if(self==null) self = new InfoDialog();
        return self.stage.isShowing();
    }
    public static void setShowing(boolean state){
        if(self==null) self = new InfoDialog();
        if(!state) self.stage.hide(); else self.stage.show();
    }
    public static void switchVisibility(){
        if(self==null) self = new InfoDialog();
        if(self.stage.isShowing()) self.stage.hide(); else self.stage.show();
    }

}
