package controllers.tree;

import application.Main;
import controllers.ProjectController;
import controllers.graphicNode.GraphicNodeController;
import iec61850.CLDUtils;
import iec61850.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import sun.reflect.generics.tree.Tree;

import java.util.HashMap;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Основной класс управления проектом
 */

public class TreeController {

    private static final TreeItem<IECObject> root = new TreeItem<>();
    private static Image iedIcon, ldIcon, lnIcon, dsIcon, doIcon;
    private static TreeView<IECObject> tree; // Главное дерево

    private static final HashMap<String, TreeItem<IECObject>> branches = new HashMap<>();  // key - UID of IECObject, value - IECObjects

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
     * @param cld - новый CLD
     */
    public static void updateTreeObjects(CLD cld){
        root.setValue(cld);

        root.getChildren().clear(); branches.clear();

        for(IED ied:cld.getIedList()){
            TreeItem<IECObject> iedItem = createTreeItem(ied); root.getChildren().add(iedItem);

            for(LD ld:ied.getLogicalDeviceList()){
                TreeItem<IECObject> ldItem = createTreeItem(ld); iedItem.getChildren().add(ldItem);

                for(LN ln:ld.getLogicalNodeList()){ TreeItem<IECObject> lnItem = createTreeItem(ln); ldItem.getChildren().add(lnItem); }

                for(DS ds:ld.getGooseOutputDS()){ TreeItem<IECObject> dsItem = createTreeItem(ds); ldItem.getChildren().add(dsItem);  dsItem.setExpanded(false); for(DO dobj:ds.getDataObject()){ TreeItem<IECObject> doItem = createTreeItem(dobj); dsItem.getChildren().add(doItem);  } }
                for(DS ds:ld.getGooseInputDS()){ TreeItem<IECObject> dsItem = createTreeItem(ds); ldItem.getChildren().add(dsItem);  dsItem.setExpanded(false); for(DO dobj:ds.getDataObject()){ TreeItem<IECObject> doItem = createTreeItem(dobj); dsItem.getChildren().add(doItem);  } }
                for(DS ds:ld.getMmsOutputDS()){ TreeItem<IECObject> dsItem = createTreeItem(ds); ldItem.getChildren().add(dsItem);  dsItem.setExpanded(false); for(DO dobj:ds.getDataObject()){ TreeItem<IECObject> doItem = createTreeItem(dobj); dsItem.getChildren().add(doItem);  } }
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
        TreeController.tree.setRoot(root);
        root.setExpanded(true);
        ImageView rootIcon = new ImageView(new Image(Main.class.getResource("/view/image/Icon.png").toString())); rootIcon.setFitWidth(20); rootIcon.setFitHeight(20);
        rootIcon.setEffect(new DropShadow( 10, Color.AQUA ));
        root.setGraphic(new Label("Project"){{ setGraphic(rootIcon); }});
        if(iedIcon==null) initialize();

        /* Выделить активный элемент в проекте при нажатии мышкой */
        tree.getSelectionModel().selectedItemProperty().addListener(e-> {
            if(((ReadOnlyObjectProperty)e).get() == null) return;
            Object object = ((TreeItem<Object>) ((ReadOnlyObjectProperty)e).get()).getValue();
            if(object.getClass()==LN.class || object.getClass()==DS.class) ProjectController.setSelectedObject((IECObject) object);
            if(object.getClass()==DO.class) ProjectController.setSelectedObject(CLDUtils.parentOf(DS.class, (IECObject) object));
            selectedItem = object;
        });
        DragTreeController.get().addTreeDragDetection(tree);
    }

    /**
     * Создает новую ветку
     * @param object - Объет помещенный в ветку (должен содержать toString())
     * @return - TreeItem
     */
    private static TreeItem<IECObject> createTreeItem(IECObject object){
        ImageView image = null;
        if(object.getClass()==LN.class) image = new ImageView(lnIcon){{ setEffect(new DropShadow( 10, Color.RED )); }};
        if(object.getClass()==DS.class) image = new ImageView(dsIcon){{ setEffect(new DropShadow( 10, Color.RED )); }};
        if(object.getClass()==LD.class) image = new ImageView(ldIcon){{ setEffect(new DropShadow( 10, Color.AQUA )); }};
        if(object.getClass()==IED.class) image = new ImageView(iedIcon){{ setEffect(new DropShadow( 10, Color.AQUA )); }};
        if(object.getClass()==DO.class) image = new ImageView(doIcon);
        image.setFitWidth(20); image.setFitHeight(20);
        TreeItem<IECObject> item = new TreeItem(object, image); item.setExpanded(true);
        branches.put(object.getUID(), item);
        return item;
    }

    /**
     * Выделенный граф.элемент
     */
    public static void setSelectedObject(IECObject selectedObject) {
        if(selectedObject!=null && selectedObject != TreeController.selectedObject){

            TreeController.selectedObject = selectedObject;
            TreeController.selectedIED = CLDUtils.parentOf(IED.class, selectedObject);
            TreeController.selectedLD = CLDUtils.parentOf(LD.class, selectedObject);
            TreeController.selectedLN = CLDUtils.parentOf(LN.class, selectedObject);
            TreeController.selectedDS = CLDUtils.parentOf(DS.class, selectedObject);

            Platform.runLater(() -> tree.getSelectionModel().select(branches.get(selectedObject.getUID())));

//            System.out.println("");
//            System.out.println(" selectedObject: " + selectedObject);
//            System.out.println(" selectedIED: " + selectedIED);
//            System.out.println(" selectedLD: " + selectedLD);
//            System.out.println(" selectedLN: " + selectedLN);
//            System.out.println(" selectedDS: " + selectedDS);
        }
    }

    /**
     * Элемент добавлен на панель
     */
    public static void graphicNodeAdded(IECObject object){
        TreeItem<IECObject> item = branches.get(object.getUID());
        if(item!=null){
            if(!item.getValue().getTags().contains("additional")) item.getGraphic().setEffect(new DropShadow( 12, Color.AQUA ));
            else item.getGraphic().setEffect(new DropShadow( 13, Color.GREENYELLOW ));
        }

        /* Если элемент дополнительный, создаем ветку */
        if(item==null && GraphicNodeController.getProjectNodeList().containsKey(object.getUID())){
            LD ld = CLDUtils.parentOf(LD.class, object);
            TreeItem<IECObject> parentItem = branches.get(ld.getUID());
            TreeItem<IECObject> childItem = createTreeItem(object);
            childItem.getGraphic().setEffect(new DropShadow( 13, Color.GREENYELLOW ));
            parentItem.getChildren().add(childItem);
        }
    }

    /**
     * Элемент удален с панели
     */
    public static void graphicNodeRemoved(IECObject object){
        TreeItem<IECObject> item = branches.get(object.getUID());
        if(item!=null) item.getGraphic().setEffect(new DropShadow( 12, Color.RED ));

        /* Если элемент дополнительный, удаляем ветку */
        if(!GraphicNodeController.getProjectNodeList().containsKey(object.getUID())){
            item.getParent().getChildren().remove(item);
            branches.remove(object.getUID());
        }
    }

    public static Object getSelectedObject() { return selectedObject; }
    public static IED getSelectedIED() { return selectedIED; }
    public static LD getSelectedLD() { return selectedLD; }
    public static LN getSelectedLN() { return selectedLN; }
    public static DS getSelectedDS() { return selectedDS; }
    public static Object getSelectedItem() { return selectedItem; }
}
