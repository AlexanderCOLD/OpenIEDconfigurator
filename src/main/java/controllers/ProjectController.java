package controllers;

import controllers.dialogs.InfoDialog;
import controllers.graphicNode.GraphicNodeController;
import controllers.tree.TreeController;
import iec61850.CLD;
import iec61850.IED;
import iec61850.IEDExtractor;
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

    private static void createNewProject(SCL scl){
        CLD cld = new CLD();
        ArrayList<IED> iedList = IEDExtractor.extractIEDList(scl);
        cld.setIedList(iedList);
        ProjectController.cld = cld;

        TreeController.updateTreeObjects(cld.getIedList()); // Строим дерево
        PanelsController.updateTabObjects(cld.getIedList()); // Создаем вкладки
        GraphicNodeController.updateNodeObjects(cld.getIedList()); // Создаем граф. элементы
    }


    /**
     * Задает активный элемент
     * @param object
     */
    public static void setSelectedObject(Object object){
        if(object != null && object != selectedObject){
            selectedObject = object;

            GraphicNodeController.setSelectedObject(object);                    // Выделяем графический элемент
            TreeController.setSelectedObject(object);                           // Выделяем ветку дерева
            PanelsController.setSelectedObject(TreeController.getSelectedLD()); // Переходим на нужную вкладку
            InfoDialog.setObject(object);                                       // Отображаем параметры элемента
        }
    }


    public static File getFileCID() { return fileCID; }
    public static void setFileCID(File fileCID) { ProjectController.fileCID = fileCID; Settings.lastCIDPath = fileCID.getPath(); }

    public static File getFileCLD() { return fileCLD; }
    public static void setFileCLD(File fileCLD) { ProjectController.fileCLD = fileCLD; Settings.lastCLDPath = fileCLD.getPath(); }

    public static CLD getCld() { return cld; }
    public static void setCld(CLD cld) { ProjectController.cld = cld; }

    public static SCL getScl() { return scl; }
    public static void setScl(SCL scl) { ProjectController.scl = scl; if(scl!=null) createNewProject(scl); }
}
