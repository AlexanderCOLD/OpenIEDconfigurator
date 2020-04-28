package controllers.dialogs;

import application.GUI;
import application.Main;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - About program
 */

public class AboutProgramDialog extends AnchorPane {

    private static AboutProgramDialog self;
    private final Stage stage = new Stage();

    public AboutProgramDialog() {
        self = this;

        Scene scene = new Scene(this);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.addEventFilter(KeyEvent.KEY_PRESSED, e-> stage.hide());
        stage.addEventFilter(MouseEvent.MOUSE_CLICKED, e-> stage.hide());
        stage.initOwner(GUI.getStage());

        setPrefSize(500, 150);
        getStylesheets().add("view/CSS/" + GUI.colorStyle + ".css");
        setStyle("-fx-background-color: -fx-fourth-color; -fx-border-color: BLACK");
        ImageView icon = new ImageView(new Image(Main.class.getResource("/view/image/Icon.png").toString()));
        icon.setFitWidth(100); icon.setFitHeight(100); getChildren().add(icon); icon.setLayoutX(25); icon.setLayoutY(25);

        Label programName = new Label("Open IED Configurator"){{ setTextFill(Color.WHITE); setFont(Font.font("System", 32)); }};
        getChildren().add(programName);
        AnchorPane.setTopAnchor(programName, 20.0); AnchorPane.setLeftAnchor(programName, 150.0); AnchorPane.setRightAnchor(programName, 25.0);

        Label programDescription = new Label("Program for making RPA based on IEC 61850"){{ setTextFill(Color.WHITE); setFont(Font.font("System", 14)); }};
        getChildren().add(programDescription);
        AnchorPane.setTopAnchor(programDescription, 65.0); AnchorPane.setLeftAnchor(programDescription, 150.0);

        Label programAuthor = new Label("Developed by: Aleksandr Kholodov"){{ setTextFill(Color.WHITE); setFont(Font.font("System", 14)); }};
        getChildren().add(programAuthor);
        AnchorPane.setTopAnchor(programAuthor, 90.0); AnchorPane.setLeftAnchor(programAuthor, 150.0);

        Label poweredBy = new Label("Powered by: open-source software"){{ setTextFill(Color.WHITE); setFont(Font.font("System", 14)); }};
        getChildren().add(poweredBy);
        AnchorPane.setTopAnchor(poweredBy, 105.0); AnchorPane.setLeftAnchor(poweredBy, 150.0);
    }

    /**
     * Показать окно About Program
     */
    public static void show(){
        if(self==null) new AboutProgramDialog();

        Platform.runLater(() -> {
            self.stage.setX(GUI.getStage().getX() + GUI.getStage().getWidth()/2 - self.stage.getWidth()/2);
            self.stage.setY(GUI.getStage().getY() + GUI.getStage().getHeight()/2 - self.stage.getHeight()/2);
        });
        self.stage.show();
    }

}
