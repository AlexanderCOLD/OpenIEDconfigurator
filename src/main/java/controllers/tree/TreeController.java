package controllers.tree;

import application.Main;
import controllers.ProjectController;
import controllers.graphicNode.GraphicNodeController;
import iec61850.CLDUtils;
import iec61850.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.HashMap;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Основной класс управления проектом
 */

public class TreeController {

    private static final TreeItem<IECObject> root = new TreeItem<>();
    private static Image iedIcon, ldIcon, lnIcon, dsIcon, doIcon, daIcon;
    private static TreeView<IECObject> tree; // Главное дерево

    private static final HashMap<String, TreeItem<IECObject>> branches = new HashMap<>();  // key - UID of IECObject, value - IECObjects

    private static Object selectedItem;   // for dragging
    private static Object selectedObject; // selected LN/DS

    private static void initialize(){
        iedIcon = new Image(Main.class.getResource("/view/image/IEDIcon.png").toString());
        ldIcon = new Image(Main.class.getResource("/view/image/LDIcon.png").toString());
        lnIcon = new Image(Main.class.getResource("/view/image/LNIcon.png").toString());
        dsIcon = new Image(Main.class.getResource("/view/image/DSIcon.png").toString());
        doIcon = new Image(Main.class.getResource("/view/image/DOIcon.png").toString());
        daIcon = new Image(Main.class.getResource("/view/image/DAIcon.png").toString());
    }

    /**
     * Наполняет дерево элементами
     * @param cld - новый CLD
     */
    public static void updateTreeObjects(CLD cld){
        root.setValue(cld);
        root.getChildren().clear();
        branches.clear();
        fillTree(root, cld.getChildren());
    }

    /** Наполнить дерево объектами */
    private static void fillTree(TreeItem<IECObject> root, ObservableList<IECObject> children){
        for(IECObject iecObject: children){
            TreeItem<IECObject> item = createTreeItem(iecObject); root.getChildren().add(item);
            item.setExpanded(iecObject.getClass() == IED.class || iecObject.getClass() == LD.class);
            if(!iecObject.getChildren().isEmpty()) fillTree(item, iecObject.getChildren());
        }
    }

    /**
     * Установка дерева
     * (при запуске программы)
     * @param tree - дерево
     */
    public static void initialize(TreeView tree) {
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
            IECObject object = ((TreeItem<IECObject>) ((ReadOnlyObjectProperty)e).get()).getValue();
            ProjectController.setSelectedObject(object);
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
        if(object.getClass()==DO.class) image = new ImageView(doIcon);
        else if(object.getClass()==DA.class) image = new ImageView(daIcon);
        else if(object.getClass()==LN.class) image = new ImageView(lnIcon){{ setEffect(new DropShadow( 10, Color.RED )); }};
        else if(object.getClass()==DS.class) image = new ImageView(dsIcon){{ setEffect(new DropShadow( 10, Color.RED )); }};
        else if(object.getClass()==LD.class) image = new ImageView(ldIcon){{ setEffect(new DropShadow( 10, Color.AQUA )); }};
        else if(object.getClass()==IED.class) image = new ImageView(iedIcon){{ setEffect(new DropShadow( 10, Color.AQUA )); }};
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
            Platform.runLater(() -> tree.getSelectionModel().select(branches.get(selectedObject.getUID())));
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
        else{
            /* Если элемент дополнительный, создаем ветку */
            if(GraphicNodeController.getProjectNodeList().containsKey(object.getUID())){
                LD ld = CLDUtils.parentOf(LD.class, object);
                TreeItem<IECObject> parentItem = branches.get(ld.getUID());
                TreeItem<IECObject> childItem = createTreeItem(object); childItem.setExpanded(false);
                childItem.getGraphic().setEffect(new DropShadow( 13, Color.GREENYELLOW ));
                parentItem.getChildren().add(childItem);
                if(!object.getChildren().isEmpty()) fillTree(childItem, object.getChildren());
            }
        }

    }

    /**
     * Элемент удален с панели
     */
    public static void graphicNodeRemoved(IECObject object){
        TreeItem<IECObject> item = branches.get(object.getUID());
        if(item!=null) {
            item.getGraphic().setEffect(new DropShadow( 12, Color.RED ));

            /* Если элемент дополнительный (не содержится в листе элементов), удаляем ветку */
            if(!GraphicNodeController.getProjectNodeList().containsKey(object.getUID())){
                item.getParent().getChildren().remove(item); branches.remove(object.getUID());
            }
        }
    }

    /** Обновить дерево */
    public static void refresh(){ tree.refresh(); }
    public static Object getSelectedItem() { return selectedItem; }
}
