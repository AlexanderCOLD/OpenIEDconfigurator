package controllers;

import application.GUI;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Контроллер контестных меню
 */

public class MainContextMenu extends ContextMenu{

    private static final MainContextMenu self = new MainContextMenu();

    static {
        MenuItem paramMain = new MenuItem("Параметры");
        paramMain.setOnAction(e ->{	System.out.println("Выполнение..."); });
        self.getItems().addAll(paramMain);
    }

    public static MainContextMenu get() { return self; }
}
