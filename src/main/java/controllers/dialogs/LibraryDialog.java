package controllers.dialogs;

import application.GUI;
import application.Main;
import controllers.ResizeController;
import controllers.elements.GraphicNode;
import controllers.DragController;
import iec61850.LN;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tools.saveload.SaveLoadObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Библиотека (окно)
 */

public class LibraryDialog extends AnchorPane {

    @FXML private Accordion accord;
    @FXML private ToggleButton lock;
    @FXML private FlowPane libraryPane;

    private static LibraryDialog self;
    private boolean draggable = false;
    private double dragOffsetX, dragOffsetY; // поправка при перетаскивании на позицию мышки
    private double localX, localY; // координаты относительно главного окна
    private EventHandler<? super MouseEvent> mouseDragged, mousePressed;
    private ChangeListener<? super Number> xListener, yListener;
    private Stage stage = new Stage();
    private Scene scene = new Scene(this);

    public LibraryDialog() { self = this; initializeDialog(); }

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

        FXMLLoader fxmlLoader = new FXMLLoader(	getClass().getResource("/view/FXML/LibraryDialog.fxml") );
        fxmlLoader.setRoot(this); fxmlLoader.setController(this);
        try { fxmlLoader.load(); } catch (IOException exception) { throw new RuntimeException(exception); }
        ImageView icon = new ImageView(new Image(Main.class.getResource("/view/image/Icon.png").toString())); icon.setFitWidth(20); icon.setFitHeight(20); getChildren().add(icon); icon.setLayoutX(4); icon.setLayoutY(5);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.initOwner(GUI.getStage());
        scene.getStylesheets().add("view/CSS/stylesheet.css");
        ResizeController.addStage(stage);
    }

    @FXML
    private void initialize() {
        accord.setExpandedPane(accord.getPanes().get(0));
        stage.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressed);
        lock.setOnMouseClicked(e-> setLock(lock.isSelected()) );
        setLock(false);
        libraryPane.setStyle("-fx-border-color: -fx-first-color; -fx-hgap: 10; -fx-vgap:10; -fx-padding: 10 10 10 10; -fx-background-color: -fx-fourth-color,\n" +
                "        linear-gradient(from 0.1px 0.0px to 15.1px  0.0px, repeat, rgba(119,119,119,0.15) 3%, transparent 0%),\n" +
                "        linear-gradient(from 0.0px 0.1px to  0.0px 15.1px, repeat, rgba(119,119,119,0.15) 3%, transparent 0%);");

        File library = new File("library/");
        if(library.exists()){
            for(File lib:library.listFiles()){
                GraphicNode graphicNode = new GraphicNode();
                LN ln = SaveLoadObject.load(LN.class, lib);
                graphicNode.setUserData(ln);
                graphicNode.setDraggable(false);
                libraryPane.getChildren().add(graphicNode);
                DragController.addToController(graphicNode);
            }
        }

    }

    @FXML private void close(){ setShowing(false); }
    public static boolean isLock(){
        if(self==null) self = new LibraryDialog();
        return self.lock.isSelected();
    }
    public static void setLock(boolean state){
        if(self==null) self = new LibraryDialog();
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
        if(self==null) self = new LibraryDialog();
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        if(x<screenBounds.getMaxX() && y<screenBounds.getMaxY()){ self.stage.setX(x); self.stage.setY(y); }
    }
    public static double[] getLayout(){
        if(self==null) self = new LibraryDialog();
        return new double[] { self.stage.getX(), self.stage.getY() };
    }

    public static void setResolution(double x, double y){
        if(self==null) self = new LibraryDialog();
        self.stage.setWidth(x);
        self.stage.setHeight(y);
    }
    public static double[] getResolution(){
        if(self==null) self = new LibraryDialog();
        return new double[] { self.stage.getWidth(), self.stage.getHeight() };
    }

    public static boolean isShowing(){
        if(self==null) self = new LibraryDialog();
        return self.stage.isShowing();
    }
    public static void setShowing(boolean state){
        if(self==null) self = new LibraryDialog();
        if(!state) self.stage.hide(); else self.stage.show();
    }
    public static void switchVisibility(){
        if(self==null) self = new LibraryDialog();
        if(self.stage.isShowing()) self.stage.hide(); else self.stage.show();
    }

    public static FlowPane getLibraryPane(){
        if(self==null) self = new LibraryDialog();
        return self.libraryPane;
    }
    public static AnchorPane get(){
        if(self==null) self = new LibraryDialog();
        return self;
    }
}
