package controllers;

import controllers.dialogs.InfoDialog;
import controllers.graphicNode.GraphicNodeController;
import controllers.tree.TreeController;
import iec61850.CLD;
import iec61850.IED;
import iec61850.IEDExtractor;
import iec61850.LN;
import iec61850.objects.SCL;
import tools.Settings;
import java.io.File;
import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 04/2020
 * @project OpenIEDconfigurator
 * @description - Контроллер для управления всем проектом
 */

public class ProjectController {

    private static File fileCID, fileCLD;
    private static CLD cld;
    private static SCL scl;

    private static Object selectedObject;

    /**
     * Добавить новый элемент при добавлении граф. элемента из библиотеки
     * @param object
     */
    public static void addObjectToProject(Object object){
        if(object.getClass()== LN.class){

        }
    }

    /**
     * Задает активный элемент
     * @param object
     */
    public static void setSelectedObject(Object object){
        if(object != null) InfoDialog.setObject(object);                        // Отображаем параметры элемента
        if(object != null && object != selectedObject){
            selectedObject = object;

            GraphicNodeController.setSelectedObject(object);                    // Выделяем графический элемент
            TreeController.setSelectedObject(object);                           // Выделяем ветку дерева
            PanelsController.setSelectedObject(TreeController.getSelectedLD()); // Переходим на нужную вкладку
        }
    }


    public static File getFileCID() { return fileCID; }
    public static void setFileCID(File fileCID) { ProjectController.fileCID = fileCID; Settings.lastCIDPath = fileCID.getPath(); }

    public static File getFileCLD() { return fileCLD; }
    public static void setFileCLD(File fileCLD) { ProjectController.fileCLD = fileCLD; Settings.lastCLDPath = fileCLD.getPath(); }

    public static CLD getCld() { return cld; }
    public static void setCld(CLD cld) { ProjectController.cld = cld; }

    public static SCL getScl() { return scl; }
    public static void setScl(SCL scl) { ProjectController.scl = scl; }
}
