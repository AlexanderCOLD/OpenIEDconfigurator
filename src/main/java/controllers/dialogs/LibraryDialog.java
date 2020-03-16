package controllers.dialogs;

import application.Main;
import application.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description
 */
public class LibraryDialog extends BorderPane {

    private static LibraryDialog self;
    @FXML private TabPane tabPane;
    @FXML private FlowPane libPane;
    private double offsetX, offsetY;

    public LibraryDialog(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/FXML/LibraryDialog.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try { loader.load(); } catch (IOException e) { e.printStackTrace();	}
        tabPane.setOnMousePressed(e->{ offsetX = e.getX(); offsetY = e.getY(); });
        tabPane.setOnMouseDragged(e->{ Point2D localCoords = getParent().sceneToLocal(e.getSceneX(), e.getSceneY()); relocate(localCoords.getX()- offsetX, localCoords.getY()- offsetY);  });

//        tabPane.getStylesheets().add(this.getClass().getResource("/view/CSS/TabPaneStyle.css").toExternalForm());
//        ((ScrollPane)tabPane.getTabs().get(0).getContent()).getStylesheets().add(this.getClass().getResource("/view/CSS/ScrollPaneStyle.css").toExternalForm());
//        libPane.setStyle("-fx-background-color: #8790a5;");
        setLayoutY(500);
        setLayoutX(500);
        setVisible(false);
        GUI.get().getChildren().add(this);
    }

    @FXML
    private void initialize() { }

    @FXML
    private void closeStage(){ hide(); }

    public FlowPane getLibPane() { return libPane; }
    public static void show(){ if(self==null) self = new LibraryDialog(); self.setVisible(true); }
    public static void hide(){ if(self==null) self = new LibraryDialog(); self.setVisible(false); }
    public static LibraryDialog get(){ if(self==null) self = new LibraryDialog(); return self; }
}
