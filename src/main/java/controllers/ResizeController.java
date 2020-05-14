package controllers;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - контроллер для изменения размера окна
 */
public class ResizeController implements EventHandler<MouseEvent> {

    private Stage stage;
    private Cursor cursorEvent = Cursor.DEFAULT;
    private final int border = 7;
    private double startX = 0, startY = 0;
    private final AnchorPane glassPane = new AnchorPane();

    public static void addStage(Stage stage){ ResizeController listener = new ResizeController(); listener.setStage(stage); }

    private void setStage(Stage stage){
        this.stage = stage;

        stage.getScene().addEventFilter(MouseEvent.MOUSE_MOVED, this);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, this);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
//        stage.getScene().addEventFilter(MouseEvent.MOUSE_EXITED, this);
//        stage.getScene().addEventFilter(MouseEvent.MOUSE_EXITED_TARGET, this);

        ((Pane)stage.getScene().getRoot()).getChildren().add(glassPane);
        AnchorPane.setTopAnchor(glassPane, 0.0);
        AnchorPane.setLeftAnchor(glassPane, 0.0);
        AnchorPane.setRightAnchor(glassPane, 0.0);
        AnchorPane.setBottomAnchor(glassPane, 0.0);

        glassPane.setStyle("-fx-background-color: RED"); glassPane.setOpacity(0.0);
        glassPane.setVisible(false);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        EventType<? extends MouseEvent> mouseEventType = mouseEvent.getEventType();
        Scene scene = stage.getScene();
        double mouseEventX = mouseEvent.getSceneX(), mouseEventY = mouseEvent.getSceneY(), sceneWidth = scene.getWidth(), sceneHeight = scene.getHeight();

        stage.toFront();

        if (MouseEvent.MOUSE_MOVED.equals(mouseEventType)) {
            if (mouseEventX < border && mouseEventY < border)                                  cursorEvent = Cursor.NW_RESIZE;
             else if (mouseEventX < border && mouseEventY > sceneHeight - border)              cursorEvent = Cursor.SW_RESIZE;
             else if (mouseEventX > sceneWidth - border && mouseEventY < border)               cursorEvent = Cursor.NE_RESIZE;
             else if (mouseEventX > sceneWidth - border && mouseEventY > sceneHeight - border) cursorEvent = Cursor.SE_RESIZE;
             else if (mouseEventX < border)                                                    cursorEvent = Cursor.W_RESIZE;
             else if (mouseEventX > sceneWidth - border)                                       cursorEvent = Cursor.E_RESIZE;
             else if (mouseEventY < border)                                                    cursorEvent = Cursor.N_RESIZE;
             else if (mouseEventY > sceneHeight - border)                                      cursorEvent = Cursor.S_RESIZE;
             else                                                                              cursorEvent = Cursor.DEFAULT;
            scene.setCursor(cursorEvent);

        }
//        else if(MouseEvent.MOUSE_EXITED.equals(mouseEventType) || MouseEvent.MOUSE_EXITED_TARGET.equals(mouseEventType)) scene.setCursor(Cursor.DEFAULT);

        if(scene.getCursor()==Cursor.DEFAULT) { glassPane.setVisible(false); return; }
        else{ glassPane.setVisible(true); }

        if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType)) { startX = stage.getWidth() - mouseEventX; startY = stage.getHeight() - mouseEventY; }
        else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType)) {
            if (!Cursor.DEFAULT.equals(cursorEvent)) {
                if (!Cursor.W_RESIZE.equals(cursorEvent) && !Cursor.E_RESIZE.equals(cursorEvent)) {

                    double minHeight = stage.getMinHeight() > (border*2) ? stage.getMinHeight() : (border*2);

                    if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.N_RESIZE.equals(cursorEvent) || Cursor.NE_RESIZE.equals(cursorEvent)) {
                        if (stage.getHeight() > minHeight || mouseEventY < 0) {
                            stage.setHeight(stage.getY() - mouseEvent.getScreenY() + stage.getHeight());
                            stage.setY(mouseEvent.getScreenY());
                        }
                    }
                    else if (stage.getHeight() > minHeight || mouseEventY + startY - stage.getHeight() > 0) stage.setHeight(mouseEventY + startY);
                }
            }

            if (!Cursor.N_RESIZE.equals(cursorEvent) && !Cursor.S_RESIZE.equals(cursorEvent)) {
                double minWidth = stage.getMinWidth() > (border*2) ? stage.getMinWidth() : (border*2);
                if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.W_RESIZE.equals(cursorEvent) || Cursor.SW_RESIZE.equals(cursorEvent)) {
                    if (stage.getWidth() > minWidth || mouseEventX < 0) {
                        stage.setWidth(stage.getX() - mouseEvent.getScreenX() + stage.getWidth());
                        stage.setX(mouseEvent.getScreenX());
                    }
                }
                else if (stage.getWidth() > minWidth || mouseEventX + startX - stage.getWidth() > 0) stage.setWidth(mouseEventX + startX);
            }
        }
    }
}
