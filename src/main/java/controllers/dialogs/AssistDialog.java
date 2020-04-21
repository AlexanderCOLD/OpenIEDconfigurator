package controllers.dialogs;

import application.GUI;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Вспомогательное окошко
 */

public class AssistDialog extends AnchorPane {

    @FXML private Accordion accord;
    @FXML private TextField textField;
    @FXML private Label textInfo, icon;
    @FXML private Button button1, button2;
    @FXML private TextArea textArea;

    private static AssistDialog self;
    private boolean draggable = false;
    private double dragOffsetX, dragOffsetY; // поправка при перетаскивании на позицию мышки
    private EventHandler<? super MouseEvent> mouseDragged, mousePressed;
    private final Stage stage = new Stage();
    private final Scene scene = new Scene(this);
//    private String warning = "⚠";
    private boolean conform;
    private String text;

    public AssistDialog() {
        self = this;
        mousePressed = e->{
            dragOffsetX = e.getScreenX() - stage.getX(); dragOffsetY = e.getScreenY() - stage.getY();
            if(dragOffsetY<7 || dragOffsetY>25 || dragOffsetX<7 || dragOffsetX>(stage.getWidth()-7)) draggable = false; else draggable = true; // Границы перетаскивания
        };
        mouseDragged = e->{
            if(!draggable) return;
            stage.setX(e.getScreenX() - this.dragOffsetX); stage.setY(e.getScreenY() - this.dragOffsetY);
        };

        FXMLLoader fxmlLoader = new FXMLLoader(	getClass().getResource("/view/FXML/AssistDialog.fxml") );
        fxmlLoader.setRoot(this); fxmlLoader.setController(this);
        try { fxmlLoader.load(); } catch (IOException exception) { throw new RuntimeException(exception); }

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        scene.getStylesheets().add("view/CSS/" + GUI.colorStyle + ".css");
        scene.getStylesheets().add("view/CSS/stylesheet.css");
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL); // Остальные окна недоступны пока открыто это
        stage.addEventFilter(KeyEvent.KEY_PRESSED, e->{ if(e.getCode()== KeyCode.ESCAPE) close(); if(e.getCode()==KeyCode.ENTER) ok(); });
        stage.focusedProperty().addListener(e-> Platform.runLater(() -> textField.requestFocus()));
        textArea.setEditable(false);
        textArea.setWrapText(true);
    }

    @FXML
    private void initialize() {
        accord.setExpandedPane(accord.getPanes().get(0));
        stage.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressed);
        stage.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseDragged);
        stage.initOwner(GUI.getStage()); //Одна иконка
    }

    @FXML private void ok(){ conform = true; text = textField.getText().replaceAll(" ",""); if(text.equals("")) text = null; stage.hide();  }
    @FXML private void close(){ conform = false; text = null; stage.hide(); }

    /**
     * Показать окно с ошибкой
     * @param title - заголовок
     * @param info - текст ошибки
     */
    public static void requestError(String title, String info){
        if(self==null) self = new AssistDialog();
        self.icon.setText("❌");
        self.textArea.setVisible(true);
        self.textField.setVisible(false);
        self.textInfo.setVisible(false);
        self.button1.setVisible(false);
        self.button2.setText("OK");
        self.accord.getPanes().get(0).setText(title);
        self.textArea.setText(info);

        Platform.runLater(() -> {
            self.stage.setX(GUI.getStage().getX() + GUI.getStage().getWidth()/2 - self.stage.getWidth()/2);
            self.stage.setY(GUI.getStage().getY() + GUI.getStage().getHeight()/2 - self.stage.getHeight()/2);
        });
        self.stage.showAndWait();
    }

    /**
     * Показать окно с запросом (да/нет)
     * @param title - заголовок
     * @param info - вопрос
     * @return - boolean true/false
     */
    public static boolean requestConfirm(String title, String info){
        if(self==null) self = new AssistDialog();
        self.icon.setText("?");
        self.textArea.setVisible(true);
        self.textField.setVisible(false);
        self.textInfo.setVisible(false);
        self.button1.setVisible(true);
        self.button2.setText("OK");
        self.button2.setText("Cancel");
        self.accord.getPanes().get(0).setText(title);
        self.textArea.setText(info);

        Platform.runLater(() -> {
            self.stage.setX(GUI.getStage().getX() + GUI.getStage().getWidth()/2 - self.stage.getWidth()/2);
            self.stage.setY(GUI.getStage().getY() + GUI.getStage().getHeight()/2 - self.stage.getHeight()/2);
        });
        self.stage.showAndWait();
        return self.conform;
    }

    /**
     * Показать окно ввода текста
     * @param title - заголовок
     * @param info - сообщение о запрошеном тексте
     * @return - String text
     */
    public static String requestText(String title, String info){
        if(self==null) self = new AssistDialog();
        self.icon.setText("");
        self.textArea.setVisible(false);
        self.textField.setVisible(true);
        self.textInfo.setVisible(true);
        self.button1.setVisible(true);
        self.button2.setText("OK");
        self.button2.setText("Cancel");
        self.textField.setText("");
        self.textInfo.setText(info);

        Platform.runLater(() -> {
            self.stage.setX(GUI.getStage().getX() + GUI.getStage().getWidth()/2 - self.stage.getWidth()/2);
            self.stage.setY(GUI.getStage().getY() + GUI.getStage().getHeight()/2 - self.stage.getHeight()/2);
        });
        self.stage.showAndWait();
        return self.text;
    }

    /**
     * Показать окно ввода текста
     * @param title - заголовок
     * @param info - сообщение о запрошеном тексте
     * @param initText - начальное значение
     * @return - String text
     */
    public static String requestText(String title, String info, String initText){
        if(self==null) self = new AssistDialog();
        self.icon.setText("");
        self.textArea.setVisible(false);
        self.textField.setVisible(true);
        self.textInfo.setVisible(true);
        self.button1.setVisible(true);
        self.button2.setText("OK");
        self.button2.setText("Cancel");
        self.textField.setText(initText);
        self.textInfo.setText(info);

        Platform.runLater(() -> {
            self.stage.setX(GUI.getStage().getX() + GUI.getStage().getWidth()/2 - self.stage.getWidth()/2);
            self.stage.setY(GUI.getStage().getY() + GUI.getStage().getHeight()/2 - self.stage.getHeight()/2);
        });
        self.stage.showAndWait();
        return self.text;
    }
}
