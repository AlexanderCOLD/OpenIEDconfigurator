package controllers.graphicNode;

import application.GUI;
import controllers.ProjectController;
import controllers.link.LinkController;
import controllers.object.DragContainer;
import iec61850.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import tools.SaveLoadObject;

/**
 * @author Александр Холодов
 * @created 04/2020
 * @project OpenIEDconfigurator
 * @description - Контроллер с обработчиками графического элемента
 */
public class GraphicNodeController {

    /** Элементы которые содержатся в дереве */
    private static final ObservableMap<String, GraphicNode> projectNodeList = FXCollections.observableHashMap();

    /** Элементы которые перетащили в панель */
    private static final ObservableMap<String, GraphicNode> activeNodeList = FXCollections.observableHashMap();

    /** Активный граф. элемент */
    private static GraphicNode selectedGraphicNode;

    /** Лист шаблонов библиотеки */
    private static final ArrayList<File> templateList = new ArrayList<File>(){{
        File lnLib = new File("library/LN/"); if(lnLib.exists()) for(File file:lnLib.listFiles()) add(file);
        File addLnLib = new File("library/AddLN/"); if(addLnLib.exists()) for(File file:addLnLib.listFiles()) add(file);
        File dsLnLib = new File("library/DS/"); if(addLnLib.exists()) for(File file:dsLnLib.listFiles()) add(file);
    }};


    /**
     * Создает графический элемент, добавляет обработчики (Drag/mouse)
     * @param iecObject - объект МЭК 61850
     * @return - графический элемент
     */
    public static GraphicNode createGraphicNode(IECObject iecObject){
        GraphicNode node = new GraphicNode(iecObject);
        GraphicNodeController.addHandlers(node);
        for (Connector connector:node.getConnectors())
            LinkController.addConnectorHandlers(connector);
        return node;
    }

    /**
     * Задать новый проект
     * @param cld - новый CLD
     */
    public static void updateNodeObjects(CLD cld) {
        /* Удаляем обработчики у текущих элементов */
        for (GraphicNode node:activeNodeList.values()) { removeHandlers(node); for(Connector connector:node.getConnectors()) LinkController.removeConnectorHandlers(connector); }
        for (GraphicNode node:projectNodeList.values()) { removeHandlers(node); for(Connector connector:node.getConnectors()) LinkController.removeConnectorHandlers(connector); }

        activeNodeList.clear();
        projectNodeList.clear();

        /* Элемнеты которые будут строиться (DS и LN) */
        ArrayList<IECObject> iecObjects = new ArrayList<>();
        for(IED ied:cld.getIedList()) for(LD ld:ied.getLogicalDeviceList()) iecObjects.addAll(ld.getChildren());

        /* Создаем графические элементы */
        for(IECObject iecObject:iecObjects){
            if(iecObject.getClass()==DS.class){ projectNodeList.put(iecObject.getUID(), createGraphicNode(iecObject)); }
            if(iecObject.getClass()==LN.class){ fillByTemplate((LN) iecObject); projectNodeList.put(iecObject.getUID(), createGraphicNode(iecObject)); }
        }
    }

    /**
     * Дополнить LN списком входящих и выходящих сигналов
     * @param ln
     */
    public static void fillByTemplate(LN ln){
        for(File template:templateList){
            String templateName = FilenameUtils.removeExtension(template.getName());
            if(templateName.equals(ln.getType())){
                LN temp = SaveLoadObject.load(LN.class, template);

                /* Внесение DS из шаблона */
                ln.getDataObjects().clear(); for(DO dos:temp.getDataObjects()) ln.getDataObjects().add(dos);
                ln.getDataAttributes().clear(); for(DA das:temp.getDataAttributes()) ln.getDataAttributes().add(das);

                return;
            }
        }
        GUI.writeErrMessage("Реализация для узла " + ln.getType() + " не найдена");
    }

    /**
     * /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     * /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     * 									     Обработчики граф. элементов
     */
    private static final ArrayList<Node> nodeList = new ArrayList<>();                   // Граф. элементы которым добавили обработчики
    private static final EventHandler<MouseEvent> dragDetected;
    private static EventHandler<DragEvent> dragEvent, dragDone;
    private static final EventHandler<MouseEvent> mouseClicked;

    private static double offsetX, offsetY; // для обработчиков
    private static GraphicNode graphicNode; // для обработчиков
    private static final ClipboardContent content = new ClipboardContent(){{ put(new DataFormat(), new DragContainer()); }}; // для обработчиков

    static {

        /* Выделить активный элемент в проекте при нажатии мышкой */
        mouseClicked = e-> { ((GraphicNode) e.getSource()).toFront(); ProjectController.setSelectedObject(((GraphicNode) e.getSource()).getIecObject()); };

        /* Начало перемещения элемента */
        dragDetected = e->{
            offsetX = e.getX(); offsetY = e.getY();
            graphicNode = (GraphicNode) e.getSource();
            graphicNode.toFront();
            ProjectController.setSelectedObject(graphicNode.getIecObject());

            if(offsetX > 10 && offsetX < graphicNode.getWidth() - 10 && !e.isControlDown()){
                graphicNode.getParent().addEventFilter(DragEvent.DRAG_OVER, dragEvent);
                graphicNode.getParent().addEventFilter(DragEvent.DRAG_DONE, dragDone);
                graphicNode.startDragAndDrop(TransferMode.ANY).setContent(content);
            }
            e.consume();
        };

        /* Перемещение элемента */
        dragEvent = e->{
            graphicNode.relocate(e.getX() - offsetX, e.getY() - offsetY);
            e.acceptTransferModes(TransferMode.ANY);
            e.consume();
        };

        /* Перемещение закончено */
        dragDone = e->{
            Pane pane = (Pane) e.getSource();
            GraphicNode node = (GraphicNode) e.getTarget();
            pane.removeEventFilter(DragEvent.DRAG_DONE, dragDone);
            pane.removeEventFilter(DragEvent.DRAG_OVER, dragEvent);
            node.updateGrid();
            e.consume();
        };
    }

    public static void addHandlers(GraphicNode node){
        if(!nodeList.contains(node)){
            nodeList.add(node);
            node.addEventHandler(MouseEvent.DRAG_DETECTED, dragDetected);
            node.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClicked);
        }
    }

    public static void removeHandlers(GraphicNode node){
        if(nodeList.contains(node)){
            nodeList.remove(node);
            node.removeEventHandler(MouseEvent.DRAG_DETECTED, dragDetected);
            node.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClicked);
        }
    }

    /**
     * Выделить графический элемент
     */
    public static void setSelectedObject(IECObject iecObject){
        if(iecObject == null && selectedGraphicNode != null) { selectedGraphicNode.setSelected(false); selectedGraphicNode = null; }
        if(iecObject == null) return;

        GraphicNode selection = projectNodeList.get(iecObject.getUID());
        if(selection == null) return;
        if(selection != selectedGraphicNode){
            if(selectedGraphicNode != null) selectedGraphicNode.setSelected(false);
            selection.setSelected(true);
            selectedGraphicNode = selection;
        }
    }

    public static ObservableMap<String, GraphicNode> getActiveNodeList() { return activeNodeList; }
    public static ObservableMap<String, GraphicNode> getProjectNodeList() { return projectNodeList; }
}
