package controllers;

import application.GUI;
import application.Main;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

/**
 * @author Александр Холодов
 * @created 05/2020
 * @project OpenIEDconfigurator
 * @description - Контроллер кнопок главного меню
 */
public class ButtonsController {

    private static FlowPane buttonsPane;
    private static final double btnSize = 24;

    /** Инициализация кнопок */
    public static void initialize(FlowPane buttonsPane){
        ButtonsController.buttonsPane = buttonsPane;
        buttonsPane.setPadding(new Insets(0.0, 0.0, 0.0, 1.0));
        buttonsPane.setHgap(1.0);
        buttonsPane.setMinHeight(btnSize);

        appendButton("/view/image/buttons/openCID.png", "Открыть CID", GUI.get()::handleOpen);
        appendButton("/view/image/buttons/importCLD.png", "Импорт CLD", GUI.get()::handleImportCLD);
        appendButton("/view/image/buttons/save.png", "Сохранить проект", GUI.get()::handleSave);
        appendSeparator();
        appendButton("/view/image/buttons/library.png", "Библиотека", GUI.get()::switchLibrary);
        appendButton("/view/image/buttons/inform.png", "Свойства объекта", GUI.get()::switchInfo);
        appendSeparator();
        appendButton("/view/image/buttons/compile.png", "Собрать проект", GUI.get()::compilePRJ);
    }

    /** Добавить кпопку */
    private static Button appendButton(String iconPath, String desc, Runnable action){
        Button button =  new Button(){{ setMinSize(btnSize,btnSize); setStyle("-fx-border-width: 1; /*-fx-border-color: -fx-first-color;*/ "); }};
        ImageView icon = new ImageView(new Image(Main.class.getResource(iconPath).toString())){{ setFitWidth(btnSize-5); setFitHeight(btnSize-5); }};
        button.setGraphic(icon);
        Tooltip tooltip = new Tooltip(){{ setStyle("-fx-background-color: transparent;"); setText(desc);setFont(Font.font(14)); }};
        button.setTooltip(tooltip);
        if(action!=null) button.setOnAction(event -> action.run());
        buttonsPane.getChildren().add(button);
        return button;
    }

    /** Разделительная линия */
    private static void appendSeparator(){
        Pane sepPane = new Pane(){{ setMinSize(btnSize/2, btnSize); setStyle("-fx-background-color: transparent; "); }};
        Line line = new Line(){{ setStartX(btnSize/4); setStartY(3); setEndX(btnSize/4); setEndY(btnSize-3); setStyle("-fx-stroke: #506c86;"); }};
        sepPane.getChildren().add(line);
        buttonsPane.getChildren().add(sepPane);
    }

}
