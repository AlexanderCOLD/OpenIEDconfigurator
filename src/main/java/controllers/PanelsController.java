package controllers;

import application.GUI;
import controllers.graphicNode.GraphicNode;
import controllers.graphicNode.GraphicNodeController;
import controllers.link.Link;
import controllers.link.LinkController;
import controllers.tree.TreeController;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import tools.ArrayMap;

import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Класс для создания панелей проекта
 */
public class PanelsController {

    private static TabPane tabPane;
    private static final ArrayMap<Object, Tab> tabList = new ArrayMap<>();     // Вкладка и объект который ей соотвествует (LD)
    private static final ArrayMap<Tab, AnchorPane> allTabs = new ArrayMap<>(); // Вкладка и панель которая в ней лежит

    private static AnchorPane selectedPanel;
    private static Tab selectedTab;

    /**
     * Создать вкладки в ссотвествие с листом объектов
     * @param objectList
     */
    public static void createTabs(Object... objectList){
        clearTabs();
        for (Object object:objectList) createTab(object);
    }

    /**
     * Создать вкладки при загрузке нового проекта
     * @param iedList
     */
    public static void updateTabObjects(ArrayList<IED> iedList){
        clearTabs();
        for(IED ied:iedList) for(LD ld:ied.getLogicalDeviceList())
            PanelsController.createTab(ld); // Добавляем вкладки
    }

    /**
     * Создать одну вкладку
     * @param object
     */
    public static void createTab(Object object){
        if(tabList.contains(object)) { GUI.writeErrMessage("Вкладка существует"); return; }
        Tab newTab = createNewTab(object.toString()); newTab.setUserData(object);
        tabList.put(object, newTab);
    }

    /**
     * Создать новую вкладку
     * @param name - Название
     */
    private static Tab createNewTab(String name){
        AnchorPane pane = new AnchorPane(); pane.setId(name);
        pane.setPrefHeight(2160); pane.setPrefWidth(3840);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        pane.setStyle(" -fx-background-color: #ecf0f1;");
        pane.getChildren().addListener(panelChangeListener);
//        pane.setOnContextMenuRequested(e->{ ContextMenuController.showContextMenu(ContextMenuController.getMainContextMenu(), e); });
        pane.addEventFilter(MouseEvent.MOUSE_PRESSED, e ->{  ContextMenuController.hideContextMenu(); });

        pane.setStyle("-fx-border-color: -fx-first-color; -fx-background-color: -fx-fourth-color,\n" +
                "        linear-gradient(from 0.1px 0.0px to 15.1px  0.0px, repeat, rgba(119,119,119,0.15) 5%, transparent 0%),\n" +
                "        linear-gradient(from 0.0px 0.1px to  0.0px 15.1px, repeat, rgba(119,119,119,0.15) 5%, transparent 0%);");

        Tab tab = new Tab(name); tab.setId(name); tab.setClosable(false); allTabs.put(tab, pane); tabPane.getTabs().add(tab);

        ScrollPane scrollPane  = new ScrollPane();
        scrollPane.setOnDragDetected(e -> { scrollPane.setPannable(e.isControlDown()); });
        scrollPane.setOnMouseClicked(e->{ scrollPane.setPannable(e.isControlDown()); });
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
        for(AnchorPane pane:allTabs.values()) pane.getChildren().removeListener(panelChangeListener);
        tabPane.getTabs().clear();
        allTabs.clear();
        tabList.clear();
    }

    /**
     * Листнер который вызывается при добавлении/удалении графических элементов
     * Изменяет содержимое graphicNodes и connections (List)
     */
    private static final ListChangeListener<Node> panelChangeListener = c ->{
        c.next();
        if(c.wasAdded()) for(Node node:c.getAddedSubList()){
            if(node.getClass()== GraphicNode.class) { GraphicNodeController.getActiveNodeList().put( node.getUserData(),(GraphicNode) node); TreeController.graphicNodeAdded(node.getUserData()); }
            else if(node.getClass() == Link.class) LinkController.getConnections().add((Link)node);
        }
        if(c.wasRemoved()) for(Node node:c.getRemoved()){
            if(node.getClass() == GraphicNode.class) { GraphicNodeController.getActiveNodeList().removeByValue((GraphicNode) node); TreeController.graphicNodeRemoved(node.getUserData()); }
            else if(node.getClass() == Link.class) LinkController.getConnections().remove(node);
        }
    };

    /**
     * Листнер который вызывается при изменении активной вкладки
     */
    private static final ChangeListener<Tab> tabChangeListener = (o, ov, tabSelection) -> {
        selectedTab = tabSelection;
        selectedPanel = allTabs.getValue(tabSelection);
    };

    /**
     * Перейти на вкладку содержищую объект (LD)
     * @param object
     */
    public static void setSelectedObject(Object object){
        if(object==null) return;
        Tab selection = tabList.getValue(object);
        if(selection!=null && selectedTab != selection) tabPane.getSelectionModel().select(selection);
    }

    /**
     * @param object - объект (в основном LD) который соотвсествует вкладке
     * @return - панель для построения элементов
     */
    public static AnchorPane getPanel(Object object){
        Tab tab = tabList.getValue(object);
        if(tab != null) return allTabs.getValue(tab);
        return null;
    }

    public static AnchorPane getSelectedPanel() { return selectedPanel; }
    public static void setTabPane(TabPane tabPane) { PanelsController.tabPane = tabPane; PanelsController.tabPane.getSelectionModel().selectedItemProperty().addListener(tabChangeListener); }
}
