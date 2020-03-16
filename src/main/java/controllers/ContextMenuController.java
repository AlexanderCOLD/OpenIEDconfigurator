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
 * @description
 */
public class ContextMenuController {

    private static ContextMenu cmMain;
    private static ContextMenu context;

    public static void initializeContextMenu(){
        cmMain = new ContextMenu();
        GUI.get().addEventFilter(MouseEvent.MOUSE_PRESSED, e -> hideContextMenu());
        MenuItem paramMain = new MenuItem("Параметры");
        paramMain.setOnAction(e ->{	System.out.println("Выполнение..."); });
        cmMain.getItems().addAll(paramMain);
    }

    private static long dtCM = 0;
    public static void showContextMenu(ContextMenu cm, ContextMenuEvent e) {
        long time = System.currentTimeMillis();
        dtCM = time - dtCM;
        if(dtCM>50) {
            if(context!=null) hideContextMenu();
            cm.show(GUI.get(),e.getScreenX(), e.getScreenY());
            context = cm;
        }
        dtCM = time;
    }

    public static void hideContextMenu() { if(context!=null) { context.hide(); context=null; } }
    public static ContextMenu getMainContextMenu() { return cmMain; }

}
