package controllers.tree;

import application.Main;
import controllers.ProjectController;
import iec61850.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tools.BiHashMap;

import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Основной класс управления проектом
 */

public class TreeController {
    private static TreeView tree; // Главное дерево
    private static TreeItem<Object> root = new TreeItem<>("Project");
    private static Image iedIcon, ldIcon, lnIcon, dsIcon, doIcon;

    private static final BiHashMap<Object, TreeItem<Object>> treeList = new BiHashMap<>(); // Ветки и их объекты

    private static Object selectedItem; // for dragging
    private static Object selectedObject; // selected LN/DS
    private static IED selectedIED; // selected IED
    private static LD selectedLD; // selected LD
    private static LN selectedLN; // selected LN
    private static DS selectedDS; // selected DS

    private static void initialize(){
        iedIcon = new Image(Main.class.getResource("/view/image/IEDIcon.png").toString());
        ldIcon = new Image(Main.class.getResource("/view/image/LDIcon.png").toString());
        lnIcon = new Image(Main.class.getResource("/view/image/LNIcon.png").toString());
        dsIcon = new Image(Main.class.getResource("/view/image/DSIcon.png").toString());
        doIcon = new Image(Main.class.getResource("/view/image/DOIcon.png").toString());
    }

    /**
     * Наполняет дерево элементами
     * @param iedList - лист из IED
     */
    public static void updateTreeObjects(ArrayList<IED> iedList){

        root.getChildren().clear(); treeList.clear();

        for(IED ied:iedList){
            TreeItem<Object> iedItem = createTreeItem(ied); root.getChildren().add(iedItem);

            for(LD ld:ied.getLogicalDeviceList()){
                TreeItem<Object> ldItem = createTreeItem(ld); iedItem.getChildren().add(ldItem);

                for(LN ln:ld.getLogicalNodeList()){ TreeItem<Object> lnItem = createTreeItem(ln); ldItem.getChildren().add(lnItem); }

                for(DS ds:ld.getGooseOutputDS()){ TreeItem<Object> dsItem = createTreeItem(ds); ldItem.getChildren().add(dsItem);  dsItem.setExpanded(false); for(DO dobj:ds.getDataObject()){ TreeItem<Object> doItem = createTreeItem(dobj); dsItem.getChildren().add(doItem);  } }
                for(DS ds:ld.getGooseInputDS()){ TreeItem<Object> dsItem = createTreeItem(ds); ldItem.getChildren().add(dsItem);  dsItem.setExpanded(false); for(DO dobj:ds.getDataObject()){ TreeItem<Object> doItem = createTreeItem(dobj); dsItem.getChildren().add(doItem);  } }
                for(DS ds:ld.getMmsOutputDS()){ TreeItem<Object> dsItem = createTreeItem(ds); ldItem.getChildren().add(dsItem);  dsItem.setExpanded(false); for(DO dobj:ds.getDataObject()){ TreeItem<Object> doItem = createTreeItem(dobj); dsItem.getChildren().add(doItem);  } }
            }
        }
    }

    /**
     * Установка дерева
     * (при запуске программы)
     * @param tree - дерево
     */
    public static void setTree(TreeView tree) {
        TreeController.tree = tree;
        tree.setRoot(root);
        root.setExpanded(true);
        ImageView rootIcon = new ImageView(new Image(Main.class.getResource("/view/image/Icon.png").toString())); rootIcon.setFitWidth(20); rootIcon.setFitHeight(20);
        root.setGraphic(new Label("Project", rootIcon));
        if(iedIcon==null) initialize();

        /* Выделить активный элемент в проекте при нажатии мышкой */
        tree.getSelectionModel().selectedItemProperty().addListener(e-> {
            if(((ReadOnlyObjectProperty)e).get() == null) return;
            Object object = ((TreeItem<Object>) ((ReadOnlyObjectProperty)e).get()).getValue();
            if(object.getClass()==LN.class || object.getClass()==DS.class) ProjectController.setSelectedObject(object);
            if(object.getClass()==DO.class) ProjectController.setSelectedObject(getParentObject(DS.class, object));
            selectedItem = object;
        });
        DragTreeController.get().addTreeDragDetection(tree);
    }

    /**
     * Создает новую ветку
     * @param object - Объет помещенный в ветку (должен содержать toString())
     * @return - TreeItem
     */
    private static TreeItem<Object> createTreeItem(Object object){
        ImageView image = null;
        if(object.getClass()==DO.class) image = new ImageView(doIcon);
        if(object.getClass()==LN.class) image = new ImageView(lnIcon);
        if(object.getClass()==DS.class) image = new ImageView(dsIcon);
        if(object.getClass()==LD.class) image = new ImageView(ldIcon);
        if(object.getClass()==IED.class) image = new ImageView(iedIcon);
        image.setFitWidth(20); image.setFitHeight(20);
        TreeItem<Object> item = new TreeItem(object, image); item.setExpanded(true);
        treeList.put(object, item);
        return item;
    }

    /**
     * Выделенный граф.элемент
     */
    public static void setSelectedObject(Object selectedObject) {
        if(selectedObject!=null && selectedObject != TreeController.selectedObject){
            TreeController.selectedObject = selectedObject;
            TreeController.selectedIED = getParentObject(IED.class, selectedObject);
            TreeController.selectedLD = getParentObject(LD.class, selectedObject);
            TreeController.selectedLN = getParentObject(LN.class, selectedObject);
            TreeController.selectedDS = getParentObject(DS.class, selectedObject);

            Platform.runLater(() -> tree.getSelectionModel().select(treeList.getValue(selectedObject)));

            System.out.println("");
            System.out.println(" selectedObject: " + selectedObject);
            System.out.println(" selectedIED: " + selectedIED);
            System.out.println(" selectedLD: " + selectedLD);
            System.out.println(" selectedLN: " + selectedLN);
            System.out.println(" selectedDS: " + selectedDS);
            System.out.println(" Tree List:   " + treeList + "   SIZE: " + treeList.size());
//            for(Object object:treeList.keySet()){ System.out.println("Last hash of: "+ object + " hash: " + object.hashCode()); }

        }
    }

    /**
     * Возвращает родительский обьект (IED, LD, LN...)
     * @param object
     * @return - IED
     */
    public static <T> T getParentObject(Class<T> classType, Object object){
        TreeItem<Object> treeItem = treeList.getValue(object);

        if(treeItem!=null){
            if(treeItem.getValue().getClass()==classType) return (T) treeItem.getValue();
            for(int depth=0; depth<10; depth++){
                treeItem = treeItem.getParent();
                if(treeItem.getValue().getClass()==classType) return (T) treeItem.getValue();
                if(treeItem.getValue().getClass()==String.class) break;
            }
        }
        return null;
    }

    /**
     * Индекс ветки
     * (ВАЖНО! ExpandedProperty влияет на индекс)
     * @param treeItem - root(корневой элемент)
     * @param object - объект индекс которого нужно найти
     * @return - индекс видимой ветки
     */
    private static int getObjectIndex(TreeItem<Object> treeItem, Object object){
        if(treeItem == root) {
            TreeItem<Object> temp = treeList.getValue(object);
            for(int depth=0; depth<10; depth++){ temp = temp.getParent(); temp.setExpanded(true); if(temp.getValue().getClass()==String.class) break; } // Если это корневой элемент - развернуть все ветви
        }
        int index = 0;
        for(TreeItem<Object> child:treeItem.getChildren()){
            index++;
            if(child.getValue()==object) return index;
            else if(child.isExpanded()) {
                int branchIndex = getObjectIndex(child, object); // если ветка раскрыта, искать внутри нее
                if(branchIndex>0) return index + branchIndex; else index += Math.abs(branchIndex);
            }
        }
        return index * (-1); // "-" - значит еще не найден
    }

    public static Object getSelectedObject() { return selectedObject; }
    public static IED getSelectedIED() { return selectedIED; }
    public static LD getSelectedLD() { return selectedLD; }
    public static LN getSelectedLN() { return selectedLN; }
    public static DS getSelectedDS() { return selectedDS; }
    public static Object getSelectedItem() { return selectedItem; }
}
