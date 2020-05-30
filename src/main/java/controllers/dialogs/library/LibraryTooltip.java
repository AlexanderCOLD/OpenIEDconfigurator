package controllers.dialogs.library;

import application.GUI;
import controllers.graphicNode.GraphicNode;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Александр Холодов
 * @created 05/2020
 * @project OpenIEDconfigurator
 * @description - Tooltip для библиотеки элементов
 */

public class LibraryTooltip extends FlowPane {

    private static final LibraryTooltip self = new LibraryTooltip();
    private static final Stage stage = new Stage(StageStyle.UNDECORATED);

    static {
        Scene scene = new Scene(self);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add("view/CSS/" + GUI.colorStyle + ".css");
        scene.getStylesheets().add("view/CSS/stylesheet.css");
        self.setStyle("-fx-background-color: transparent; -fx-border-width: 0.0;");
        self.setHgap(10); self.setVgap(10);
        self.setPadding(new Insets(5,5,5,5));
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(LibraryDialog.getStage());
    }

    /** Добавить тултип для узла */
    public static void addGraphicNode(GraphicNode node){
        GraphicNode shadowNode = new GraphicNode(node.getIecObject());
        node.addEventFilter(MouseEvent.MOUSE_ENTERED, e->{ showTooltipNode(e, shadowNode); });
        node.addEventFilter(MouseEvent.MOUSE_EXITED, e->{ hideTooltipNode(e, shadowNode); });
    }

    /** Показать граф. иконку */
    private static void showTooltipNode(MouseEvent e, GraphicNode node) {
        if(!self.getChildren().contains(node)) self.getChildren().add(node);
        stage.setX(e.getScreenX() - 80);
        stage.setY(e.getScreenY() + 10);
        stage.show();
    }

    /** Скрыть граф. иконку */
    private static void hideTooltipNode(MouseEvent e, GraphicNode node){
        self.getChildren().remove(node);
        stage.hide();
    }

}
