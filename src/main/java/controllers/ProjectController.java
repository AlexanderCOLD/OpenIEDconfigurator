package controllers;

import application.Main;
import controllers.elements.GraphicNode;
import controllers.elements.Link;
import iec61850.*;
import iec61850.objects.SCL;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import tools.BiHashMap;
import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Основной класс управления проектом
 */

public class ProjectController {
    private static TreeItem<Object> root = new TreeItem<>("Project");
    private static Image iedIcon, ldIcon, lnIcon, dsIcon, doIcon;

    private static SCL scl; // Текущий scl
    private static ArrayList<IED> iedList; // Текущий лист
    private static GraphicNode selectedNode; // Выделенный узел

    private static BiHashMap<TreeItem<Object>, Object> project = new BiHashMap<>();    // Ветки и их объекты
    private static ArrayList<GraphicNode> graphicNodes = new ArrayList<>(); // Граф. элементы
    private static ArrayList<Link> connections = new ArrayList<>(); // Соединения

    private static void initialize(){
        iedIcon = new Image(Main.class.getResource("/view/image/IEDIcon.png").toString());
        ldIcon = new Image(Main.class.getResource("/view/image/LDIcon.png").toString());
        lnIcon = new Image(Main.class.getResource("/view/image/LNIcon.png").toString());
        dsIcon = new Image(Main.class.getResource("/view/image/DSIcon.png").toString());
        doIcon = new Image(Main.class.getResource("/view/image/DOIcon.png").toString());
    }

    /**
     * Листнер который вызывается при добавлении/удалении графических элементов
     */
    private static ListChangeListener<Node> listener = c ->{
        c.next();
        if(c.wasAdded()) for(Node node:c.getAddedSubList()){ if(node.getClass()== GraphicNode.class) graphicNodes.add((GraphicNode) node); else if(node.getClass()== Link.class) connections.add((Link)node); }
        if(c.wasRemoved()) for(Node node:c.getRemoved()){ if(node.getClass()== GraphicNode.class) graphicNodes.remove(node); else if(node.getClass()==Link.class) connections.remove(node); }
    };

    public static void setSCL(SCL scl) {
        ArrayList<IED> project = IEDExtractor.extractIEDList(scl);
        if(project.size()>0){ iedList = project; ProjectController.scl = scl; updateTree(project);  }
    }


    public static void updateTree(ArrayList<IED> ieds){
        clearProject();

        for(IED ied:ieds){
            TreeItem<Object> iedItem = createTreeItem(ied); root.getChildren().add(iedItem);

            for(LD ld:ied.getLogicalDeviceList()){
                TreeItem<Object> ldItem = createTreeItem(ld); iedItem.getChildren().add(ldItem);

                for(LN ln:ld.getLogicalNodeList()){ TreeItem<Object> lnItem = createTreeItem(ln); ldItem.getChildren().add(lnItem); }

                for(DS ds:ld.getGooseOutputDS()){ TreeItem<Object> dsItem = createTreeItem(ds); ldItem.getChildren().add(dsItem);  dsItem.setExpanded(false); for(DO dobj:ds.getDataObject()){ TreeItem<Object> doItem = createTreeItem(dobj); dsItem.getChildren().add(doItem);  } }
                for(DS ds:ld.getGooseInputDS()){ TreeItem<Object> dsItem = createTreeItem(ds); ldItem.getChildren().add(dsItem);  dsItem.setExpanded(false); for(DO dobj:ds.getDataObject()){ TreeItem<Object> doItem = createTreeItem(dobj); dsItem.getChildren().add(doItem);  }}
                for(DS ds:ld.getMmsOutputDS()){ TreeItem<Object> dsItem = createTreeItem(ds); ldItem.getChildren().add(dsItem);  dsItem.setExpanded(false); for(DO dobj:ds.getDataObject()){ TreeItem<Object> doItem = createTreeItem(dobj); dsItem.getChildren().add(doItem);  } }
            }
        }
    }

    private static void clearProject(){
        root.getChildren().clear(); project.clear();
    }

    public static void setTree(TreeView tree) {
        tree.setRoot(root);
        root.setExpanded(true);
        ImageView rootIcon = new ImageView(new Image(Main.class.getResource("/view/image/Icon.png").toString())); rootIcon.setFitWidth(20); rootIcon.setFitHeight(20);
        root.setGraphic(new Label("Project", rootIcon));
        if(iedIcon==null) initialize();

        tree.getSelectionModel().selectedItemProperty().addListener(e-> { TreeItem item = (TreeItem) ((ReadOnlyObjectProperty)e).get(); System.out.println(item); });
        tree.addEventFilter(MouseEvent.MOUSE_DRAGGED, e->{ System.out.println("pressed:      "+ e); });
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
        project.put(item, object);
        return item;
    }


    /**
     * Выделенный граф.элемент
     */
    public static void setSelectedNode(GraphicNode selectedNode) {
        if(ProjectController.selectedNode!=selectedNode){
            if(ProjectController.selectedNode!=null) ProjectController.selectedNode.setSelected(false);
            ProjectController.selectedNode = selectedNode;
            if(ProjectController.selectedNode!=null) ProjectController.selectedNode.setSelected(true);
        }
    }
    public static GraphicNode getSelectedNode() { return selectedNode; }

    public static ArrayList<GraphicNode> getGraphicNodes() { return graphicNodes; }
    public static ArrayList<Link> getConnections() { return connections; }

    /**
     * Добавляет в панель листнер изенения графических элементов
     * @param pane - Панель во вкладках
     */
    public static void addPaneToListening(AnchorPane pane) { pane.getChildren().addListener(listener);	}
}
