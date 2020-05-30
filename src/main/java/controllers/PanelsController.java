package controllers;

import application.GUI;
import controllers.graphicNode.GraphicNode;
import controllers.graphicNode.GraphicNodeController;
import controllers.link.Link;
import controllers.link.LinkController;
import controllers.tree.TreeController;
import iec61850.CLD;
import iec61850.IECObject;
import iec61850.IED;
import iec61850.LD;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import java.util.HashMap;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Класс для создания панелей проекта
 */
public class PanelsController {

    private static TabPane tabPane;
    private static Tab selectedTab;

    /* Вкладка и UID объекта (LD) */
    private static final HashMap<String, Tab> tabs = new HashMap<>();

    /* Панель которая в ней лежит и UID объекта (LD) */
    private static final HashMap<String, AnchorPane> panels = new HashMap<>();

    /* Объекты МЭК 61850 (LD) */
    private static final HashMap<String, IECObject> iecObjects = new HashMap<>();


    /**
     * Создать вкладки при загрузке нового проекта
     * @param cld - новый CLD
     */
    public static void updateTabObjects(CLD cld){
        clearTabs();
        for(IED ied:cld.getIedList()) for(LD ld:ied.getLogicalDeviceList()) createTab(ld); // Добавляем вкладки
    }

    /**
     * Создать одну вкладку
     * @param iecObject - объект МЭК 61850 (LD)
     */
    public static void createTab(IECObject iecObject){
        if(tabs.containsKey(iecObject.getUID())) { GUI.writeErrMessage("Вкладка существует"); return; }
        createNewTab(iecObject.toString(), iecObject.getUID());
        iecObjects.put(iecObject.getUID(), iecObject);
    }

    /**
     * Создать новую вкладку
     * @param name - Название
     */
    public static Tab createNewTab(String name, String id){
        AnchorPane pane = new AnchorPane(); pane.setId(id);
        pane.setPrefHeight(2160); pane.setPrefWidth(3840);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        pane.setStyle(" -fx-background-color: #ecf0f1;");
        pane.getChildren().addListener(panelChangeListener);
//        pane.setOnContextMenuRequested(e->{ ContextMenuController.showContextMenu(ContextMenuController.getMainContextMenu(), e); });

        pane.setStyle("-fx-border-color: -fx-first-color; -fx-background-color: -fx-fourth-color,\n" +
                "        linear-gradient(from 0.1px 0.0px to 15.1px  0.0px, repeat, rgba(119,119,119,0.15) 5%, transparent 0%),\n" +
                "        linear-gradient(from 0.0px 0.1px to  0.0px 15.1px, repeat, rgba(119,119,119,0.15) 5%, transparent 0%);");

        Tab tab = new Tab(name); tab.setId(id); tab.setClosable(false);
        panels.put(id, pane); tabs.put(id, tab);
        tabPane.getTabs().add(tab);

        ScrollPane scrollPane  = new ScrollPane();
        scrollPane.setOnDragDetected(e -> { if(e.getButton()== MouseButton.MIDDLE) { scrollPane.setPannable(true); scrollPane.startFullDrag(); } e.consume(); });
        scrollPane.setOnMouseDragReleased(e -> { scrollPane.setPannable(false); e.consume(); });
        tab.setContent(scrollPane);

        StackPane zoomTarget = new StackPane(pane); zoomTarget.setAlignment(Pos.CENTER);
        Group group = new Group(zoomTarget);

        StackPane content = new StackPane(group); content.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null))); scrollPane.setContent(content);
        scrollPane.viewportBoundsProperty().addListener((observable, oldBounds, newBounds) -> {  content.setPrefSize(newBounds.getWidth(), newBounds.getHeight());  });
        content.setStyle("-fx-background-color: linear-gradient(to bottom, -fx-third-color, -fx-fourth-color);");

        content.setOnScroll(evt -> {
            if (evt.isControlDown()) {
                evt.consume();

                final double zoomFactor = evt.getDeltaY() > 0 ? 1.1 : 1 / 1.1;
                if(zoomTarget.getScaleX()<0.2 && evt.getDeltaY()<0) return;
                if(zoomTarget.getScaleX()>4.0 && evt.getDeltaY()>0) return;

                Bounds groupBounds = group.getLayoutBounds();
                Bounds viewportBounds = scrollPane.getViewportBounds();

                double valX = scrollPane.getHvalue() * (groupBounds.getWidth() - viewportBounds.getWidth());
                double valY = scrollPane.getVvalue() * (groupBounds.getHeight() - viewportBounds.getHeight());

                Point2D posInZoomTarget = zoomTarget.parentToLocal(group.parentToLocal(new Point2D(evt.getX(), evt.getY())));
                Point2D adjustment = zoomTarget.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

                zoomTarget.setScaleX(zoomFactor * zoomTarget.getScaleX());
                zoomTarget.setScaleY(zoomFactor * zoomTarget.getScaleY());
                double scale = 1/zoomTarget.getScaleX();
                GUI.getZoomLabel().setText((((double)Math.round(100/scale))/100)+"x");
                scrollPane.layout(); // refresh ScrollPane scroll positions & content bounds

                groupBounds = group.getLayoutBounds();
                scrollPane.setHvalue((valX + adjustment.getX()) / (groupBounds.getWidth() - viewportBounds.getWidth()));
                scrollPane.setVvalue((valY + adjustment.getY()) / (groupBounds.getHeight() - viewportBounds.getHeight()));
            }
        });
        return tab;
    }

    public static void clearTabs(){
        for(AnchorPane pane:panels.values()) pane.getChildren().removeListener(panelChangeListener);
        tabPane.getTabs().clear();
        iecObjects.clear();
        panels.clear();
        tabs.clear();
    }

    /**
     * Листнер который вызывается при добавлении/удалении графических элементов
     * Изменяет содержимое graphicNodes и connections (List)
     */
    private static final ListChangeListener<Node> panelChangeListener = c ->{
        c.next();
        if(c.wasAdded()) for(Node node:c.getAddedSubList()){
            if(node.getClass()== GraphicNode.class) {
                GraphicNodeController.getActiveNodeList().put( node.getId(),(GraphicNode) node);
                TreeController.graphicNodeAdded(((GraphicNode) node).getIecObject());
            }
            else if(node.getClass() == Link.class) LinkController.getConnections().add((Link)node);
        }
        if(c.wasRemoved()) for(Node node:c.getRemoved()){
            if(node.getClass() == GraphicNode.class) {
                GraphicNodeController.getActiveNodeList().remove(node.getId());
                TreeController.graphicNodeRemoved(((GraphicNode) node).getIecObject());
            }
            else if(node.getClass() == Link.class) LinkController.getConnections().remove(node);
        }
    };

    /**
     * Листнер который вызывается при изменении активной вкладки
     */
    private static final ChangeListener<Tab> tabChangeListener = (o, ov, tab) -> {
        if(tab==null) return;
        selectedTab = tab;
    };

    /**
     * Перейти на вкладку содержищую объект (LD)
     * @param iecObject - Объект МЭК 61850
     */
    public static void setSelectedObject(IECObject iecObject){
        if(iecObject==null) return;
        Tab selection = tabs.get(iecObject.getUID());
        if(selection!=null && selectedTab != selection) tabPane.getSelectionModel().select(selection);
    }

    /**
     * @param iecObject - объект (в основном LD) который соотвсествует вкладке
     * @return - панель для построения элементов
     */
    public static AnchorPane getPanel(IECObject iecObject){
        return panels.get(iecObject.getUID());
    }

    /**
     * Текущая открытая панель
     */
    public static AnchorPane getSelectedPanel() { if(selectedTab!=null) return panels.get(selectedTab.getId()); return null; }

    /**
     * Текущий элемент МЭК 61850 (LD)
     */
    public static IECObject getSelectedIECObject(){ if(selectedTab!=null) return iecObjects.get(selectedTab.getId()); return null; }

    /**
     * Задать TabPane (Во время старта программы)
     */
    public static void initialize(TabPane tabPane) {
        PanelsController.tabPane = tabPane;
        PanelsController.tabPane.getSelectionModel().selectedItemProperty().addListener(tabChangeListener);
    }
}
