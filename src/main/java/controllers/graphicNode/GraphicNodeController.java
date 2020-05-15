package controllers.graphicNode;

import application.GUI;
import controllers.CLDVersionControl;
import controllers.ProjectController;
import controllers.link.LinkController;
import controllers.object.DragContainer;
import iec61850.*;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import tools.ArrayMap;
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

    private static final ArrayMap<Object, GraphicNode> projectNodeList = new ArrayMap<>(); // Граф. элементы созданные при загрузке CID (Не важно брошены или нет)
    private static final ArrayMap<Object, GraphicNode> activeNodeList = new ArrayMap<>(); // Лист с текущими графическими элементами (Во всех вкладках, брошенные в проект)

    private static GraphicNode selectedGraphicNode; // Активный граф. элемент

    private static final ArrayList<Node> nodeList = new ArrayList<>();
    private static EventHandler<MouseEvent> dragDetected;
    private static EventHandler <DragEvent> dragEvent, dragDone;
    private static EventHandler<MouseEvent> mouseClicked;

    private static double offsetX, offsetY; // для обработчиков
    private static GraphicNode graphicNode; // для обработчиков
    private static final ClipboardContent content = new ClipboardContent(){{ put(new DataFormat(), new DragContainer()); }}; // для обработчиков

    private static final File[] templateList = new File("library/"){{ if(!exists()) mkdirs(); }}.listFiles(); // Лист шаблонов библиотеки

    /**
     * Создает графический элемент, добавляет обработчики (Drag/mouse)
     *
     * @param userData - объект
     * @return - графический элемент
     */
    public static GraphicNode createGraphicNode(Object userData){
        GraphicNode node = new GraphicNode(userData);

        GraphicNodeController.addHandlers(node);
        for (Connector connector:node.getConnectors()) LinkController.addConnectorHandlers(connector);

        return node;
    }

    /**
     * Задать новый проект
     * @param iedList
     */
    public static void updateNodeObjects(ArrayList<IED> iedList) {
        if(dragDetected==null) initialize();
        for (GraphicNode node:activeNodeList.values()) { removeHandlers(node); for(Connector connector:node.getConnectors()) LinkController.removeConnectorHandlers(connector); }
        for (GraphicNode node:projectNodeList.values()) { removeHandlers(node); for(Connector connector:node.getConnectors()) LinkController.removeConnectorHandlers(connector); }
        activeNodeList.clear();
        projectNodeList.clear();

        for (IED ied:iedList) for(LD ld:ied.getLogicalDeviceList()){
            for(DS ds:ld.getGooseOutputDS()) projectNodeList.put(ds, createGraphicNode(ds));
            for(DS ds:ld.getGooseInputDS()) projectNodeList.put(ds, createGraphicNode(ds));
            for(DS ds:ld.getMmsOutputDS()) projectNodeList.put(ds, createGraphicNode(ds));
            for(LN ln:ld.getLogicalNodeList()) { fillByTemplate(ln); projectNodeList.put(ln, createGraphicNode(ln)); }
        }
    }

    /**
     * Дополнить LN списком входящих и выходящих сигналов
     * @param ln
     */
    private static void fillByTemplate(LN ln){
        for(File template:templateList){
            String templateName = FilenameUtils.removeExtension(template.getName());
            if(templateName.equals(ln.getClassType())){
                LN temp = SaveLoadObject.load(LN.class, template);

                /* Внесение DS из шаблона */
                ln.setDataSetInput(temp.getDataSetInput());
                ln.setDataSetOutput(temp.getDataSetOutput());

                /* Установка значений parentDO */
                CLDVersionControl.findParentsDO(ln.getDataSetInput().getDataObject());
                CLDVersionControl.findParentsDO(ln.getDataSetOutput().getDataObject());

                return;
            }
        }
        GUI.writeErrMessage("Реализация для узла " + ln.getClassType() + " не найдена");
    }

    /**
     * /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     * /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     * 									     Обработчики граф. элементов
     */
    private static void initialize() {

        /* Выделить активный элемент в проекте при нажатии мышкой */
        mouseClicked = e-> { ((GraphicNode) e.getSource()).toFront(); ProjectController.setSelectedObject(((GraphicNode) e.getSource()).getUserData()); };

        /* Начало перемещения элемента */
        dragDetected = e->{
            offsetX = e.getX(); offsetY = e.getY();
            graphicNode = (GraphicNode) e.getSource();
            graphicNode.toFront();
            ProjectController.setSelectedObject(graphicNode.getUserData());

            if(offsetY<20 && !e.isControlDown()){
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
            ProjectController.setSelectedObject(node.getUserData());
            e.consume();
        };
    }

    public static void addHandlers(GraphicNode node){
        if(dragDetected==null) initialize();
        if(!nodeList.contains(node)){
            nodeList.add(node);
            node.addEventHandler(MouseEvent.DRAG_DETECTED, dragDetected);
            node.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClicked);
        }
    }

    public static void removeHandlers(GraphicNode node){
        if(dragDetected==null) initialize();
        if(nodeList.contains(node)){
            nodeList.remove(node);
            node.removeEventHandler(MouseEvent.DRAG_DETECTED, dragDetected);
            node.removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClicked);
        }
    }

    public static void setSelectedObject(Object object){
        if(object == null && selectedGraphicNode != null) { selectedGraphicNode.setSelected(false); selectedGraphicNode = null; return; }
        GraphicNode selection = projectNodeList.getValue(object);
        if(selection == null) return;
        if(selection != selectedGraphicNode){
            if(selectedGraphicNode != null) selectedGraphicNode.setSelected(false);
            selection.setSelected(true);
            selectedGraphicNode = selection;
        }
    }

    public static ArrayMap<Object, GraphicNode> getActiveNodeList() { return activeNodeList; }
    public static ArrayMap<Object, GraphicNode> getProjectNodeList() { return projectNodeList; }
}
