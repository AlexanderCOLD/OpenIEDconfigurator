package controllers.dialogs.library;

import application.GUI;
import application.Main;
import controllers.ResizeController;
import controllers.graphicNode.GraphicNode;
import iec61850.DS;
import iec61850.LN;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Accordion;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tools.SaveLoadObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Библиотека (окно)
 */

public class LibraryDialog extends AnchorPane {

    @FXML private Accordion accord;
    @FXML private ToggleButton lock;
    @FXML private FlowPane libraryPane, addLibraryPane, convPane;

    private static LibraryDialog self = new LibraryDialog();
    private boolean draggable = false;
    private double dragOffsetX, dragOffsetY; // поправка при перетаскивании на позицию мышки
    private double localX, localY; // координаты относительно главного окна
    private final EventHandler<? super MouseEvent> mouseDragged, mousePressed;
    private final ChangeListener<? super Number> xListener, yListener;
    private final Stage stage = new Stage();

    public LibraryDialog() {
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

        FXMLLoader fxmlLoader = new FXMLLoader(	getClass().getResource("/view/FXML/LibraryDialog.fxml") );
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
        addEventFilter(MouseEvent.MOUSE_ENTERED, e-> { stage.requestFocus(); });
        lock.setOnMouseClicked(e-> setLock(lock.isSelected()) );
        setLock(false);

        libraryPane.setStyle("-fx-border-color: -fx-first-color; -fx-hgap: 10; -fx-vgap:10; -fx-padding: 10 10 10 10; -fx-background-color: -fx-fourth-color,\n" +
                "        linear-gradient(from 0.1px 0.0px to 15.1px  0.0px, repeat, rgba(119,119,119,0.15) 3%, transparent 0%),\n" +
                "        linear-gradient(from 0.0px 0.1px to  0.0px 15.1px, repeat, rgba(119,119,119,0.15) 3%, transparent 0%);");

        addLibraryPane.setStyle("-fx-border-color: -fx-first-color; -fx-hgap: 10; -fx-vgap:10; -fx-padding: 10 10 10 10; -fx-background-color: -fx-fourth-color,\n" +
                "        linear-gradient(from 0.1px 0.0px to 15.1px  0.0px, repeat, rgba(119,119,119,0.15) 3%, transparent 0%),\n" +
                "        linear-gradient(from 0.0px 0.1px to  0.0px 15.1px, repeat, rgba(119,119,119,0.15) 3%, transparent 0%);");

        convPane.setStyle("-fx-border-color: -fx-first-color; -fx-hgap: 10; -fx-vgap:10; -fx-padding: 10 10 10 10; -fx-background-color: -fx-fourth-color,\n" +
                "        linear-gradient(from 0.1px 0.0px to 15.1px  0.0px, repeat, rgba(119,119,119,0.15) 3%, transparent 0%),\n" +
                "        linear-gradient(from 0.0px 0.1px to  0.0px 15.1px, repeat, rgba(119,119,119,0.15) 3%, transparent 0%);");

        File library = new File("library/DS/");
        if(library.exists()){
            for(File lib: Objects.requireNonNull(library.listFiles())){
                if(lib.isDirectory()) continue;
                DS ds = SaveLoadObject.load(DS.class, lib);
                if(ds == null) continue;
                GraphicNode graphicNode = new GraphicNode();
                graphicNode.setIecSimple(ds);
                addLibraryPane.getChildren().add(graphicNode);
                DragLibController.addToController(graphicNode);
                graphicNode.setCache(true);
                graphicNode.setCacheHint(CacheHint.SPEED);
            }
        }

        library = new File("library/ALN/");
        if(library.exists()){
            for(File lib: Objects.requireNonNull(library.listFiles())){
                if(lib.isDirectory()) continue;
                LN ln = SaveLoadObject.load(LN.class, lib);
                if(ln == null) continue;
                GraphicNode graphicNode = new GraphicNode();
                graphicNode.setIecSimple(ln);
                addLibraryPane.getChildren().add(graphicNode);
                DragLibController.addToController(graphicNode);
                graphicNode.setCache(true);
                graphicNode.setCacheHint(CacheHint.SPEED);
            }
        }

        library = new File("library/CLN/");
        if(library.exists()){
            for(File lib: Objects.requireNonNull(library.listFiles())){
                if(lib.isDirectory()) continue;
                LN ln = SaveLoadObject.load(LN.class, lib);
                if(ln == null) continue;
                GraphicNode graphicNode = new GraphicNode();
                graphicNode.setIecSimple(ln);
                convPane.getChildren().add(graphicNode);
                DragLibController.addToController(graphicNode);
                graphicNode.setCache(true);
                graphicNode.setCacheHint(CacheHint.SPEED);
            }
        }

        library = new File("library/LN/");
        if(library.exists()){
            for(File lib: Objects.requireNonNull(library.listFiles())){
                if(lib.isDirectory()) continue;
                LN ln = SaveLoadObject.load(LN.class, lib);
                if(ln == null) continue;
                GraphicNode graphicNode = new GraphicNode();
                graphicNode.setIecSimple(ln);
                libraryPane.getChildren().add(graphicNode);
                DragLibController.addToController(graphicNode);
                graphicNode.setCache(true);
                graphicNode.setCacheHint(CacheHint.SPEED);
            }
        }

//        Platform.runLater(this::changeScale);
    }

    private void changeScale(){
        for(Node node: new ArrayList<>(addLibraryPane.getChildren())){
            if(node.getClass()!=GraphicNode.class) return;

            GraphicNode graphicNode = (GraphicNode) node;

            double scaleFactor = 0.5;
            Scale scale = new Scale(scaleFactor, scaleFactor, 0.0, 0.0);
            graphicNode.getTransforms().add(scale);

            StackPane extPanel = new StackPane(graphicNode);
            extPanel.setPadding(Insets.EMPTY);
            extPanel.setStyle("-fx-border-width: 2; -fx-border-color: RED");
            addLibraryPane.getChildren().add(extPanel);

            System.out.println(1);
        }
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

    public static FlowPane getLibraryPane(){ return self.libraryPane; }
    public static AnchorPane get(){ return self; }
    public static Stage getStage() { return self.stage; }
}
