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

public class ContextMenuController {

    private static ContextMenu mainContextMenu;
    private static ContextMenu currentShownContextMenu;
    private static long dtCM = 0; // Выдержка по времени (Контекстные меню смежных элементов обрабатываются одновременно, wtf)


    public static void initializeContextMenu(){
        mainContextMenu = new ContextMenu();
        GUI.get().addEventFilter(MouseEvent.MOUSE_PRESSED, e -> hideContextMenu());
        MenuItem paramMain = new MenuItem("Параметры");
        paramMain.setOnAction(e ->{	System.out.println("Выполнение..."); });
        mainContextMenu.getItems().addAll(paramMain);
    }

    public static void showContextMenu(ContextMenu contextMenu, ContextMenuEvent event) {
        long time = System.currentTimeMillis();
        dtCM = time - dtCM;
        if(dtCM>50) {
            hideContextMenu();
            contextMenu.show(GUI.get(),event.getScreenX(), event.getScreenY());
            currentShownContextMenu = contextMenu;
        }
        dtCM = time;
    }

    public static void hideContextMenu() { if(currentShownContextMenu !=null) { currentShownContextMenu.hide(); currentShownContextMenu =null; } }
    public static ContextMenu getMainContextMenu() { return mainContextMenu; }
}
