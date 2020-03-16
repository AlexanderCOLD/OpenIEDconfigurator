package controllers;

import application.GUI;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import tools.LOG;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Класс для создания панелей проекта
 */
public class PanelsController {

    private static HashMap<Tab, AnchorPane> allPanels = new HashMap<>();
    private static Tab currentTab;
    private static TabPane tabPane;
    private static AnchorPane currentPanel;
    private static double scale = 1;

    public static void addTab(String name){
        Tab tab = createTab(name);
        if(tab!=null){
            tabPane.getTabs().add(tab);
            if(currentTab==null) { currentTab = tab; currentPanel = allPanels.get(tab); }
        }
    }

    /**
     * Создать новую вкладку
     * @param name - Название
     */
    public static Tab createTab(String name){
        for(Map.Entry<Tab, AnchorPane> es:allPanels.entrySet()) if(es.getKey().getId().equals(name)) { LOG.print("Панель уже существует "+name); return null; }

        Tab tab = new Tab(name);
        tab.setId(UUID.randomUUID().toString());

        AnchorPane pane = new AnchorPane();
        pane.setId(name);
        pane.setPrefHeight(2160); pane.setPrefWidth(3840);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        pane.setStyle(" -fx-background-color: #ecf0f1;");
        ProjectController.addToListening(pane);
        pane.setOnContextMenuRequested(e->{ ContextMenuController.showContextMenu(ContextMenuController.getMainContextMenu(), e); });
        pane.addEventFilter(MouseEvent.MOUSE_PRESSED, e ->{  ContextMenuController.hideContextMenu(); });

        pane.setStyle("-fx-background-color: #1b2938,\n" +
                "        linear-gradient(from 0.1px 0.0px to 15.1px  0.0px, repeat, rgba(119,119,119,0.15) 3%, transparent 0%),\n" +
                "        linear-gradient(from 0.0px 0.1px to  0.0px 15.1px, repeat, rgba(119,119,119,0.15) 3%, transparent 0%);");

        tab.setOnSelectionChanged(e->{ if(((Tab)e.getSource()).isSelected()) { currentTab = (Tab) e.getSource(); currentPanel = allPanels.get(currentTab); } });
        allPanels.put(tab, pane);

        ScrollPane scrollPane  = new ScrollPane();
        scrollPane.setOnDragDetected(e -> { scrollPane.setPannable(GUI.isCtrl()); });
        scrollPane.setOnMouseClicked(e->{ scrollPane.setPannable(GUI.isCtrl()); });
        tab.setContent(scrollPane);

        StackPane zoomTarget = new StackPane(pane); zoomTarget.setAlignment(Pos.CENTER);
        Group group = new Group(zoomTarget);

        StackPane content = new StackPane(group); content.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null))); scrollPane.setContent(content);
        scrollPane.viewportBoundsProperty().addListener((observable, oldBounds, newBounds) -> {  content.setPrefSize(newBounds.getWidth(), newBounds.getHeight());  });
        content.setStyle("-fx-background-color: linear-gradient(to bottom, #1b2938, #1b2938);");

        content.setOnScroll(evt -> {
            if (evt.isControlDown()) {
                evt.consume();

                final double zoomFactor = evt.getDeltaY() > 0 ? 1.1 : 1 / 1.1;
                if(zoomTarget.getScaleX()<0.35 && evt.getDeltaY()<0) return;
                if(zoomTarget.getScaleX()>4 && evt.getDeltaY()>0) return;

                Bounds groupBounds = group.getLayoutBounds();
                Bounds viewportBounds = scrollPane.getViewportBounds();

                double valX = scrollPane.getHvalue() * (groupBounds.getWidth() - viewportBounds.getWidth());
                double valY = scrollPane.getVvalue() * (groupBounds.getHeight() - viewportBounds.getHeight());

                Point2D posInZoomTarget = zoomTarget.parentToLocal(group.parentToLocal(new Point2D(evt.getX(), evt.getY())));
                Point2D adjustment = zoomTarget.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

                zoomTarget.setScaleX(zoomFactor * zoomTarget.getScaleX());
                zoomTarget.setScaleY(zoomFactor * zoomTarget.getScaleY());
                scale = 1/zoomTarget.getScaleX();
                GUI.getZoomLabel().setText((((double)Math.round(100/scale))/100)+"x");
                scrollPane.layout(); // refresh ScrollPane scroll positions & content bounds

                groupBounds = group.getLayoutBounds();
                scrollPane.setHvalue((valX + adjustment.getX()) / (groupBounds.getWidth() - viewportBounds.getWidth()));
                scrollPane.setVvalue((valY + adjustment.getY()) / (groupBounds.getHeight() - viewportBounds.getHeight()));
            }
        });
        return tab;
    }

    public static void setTabPane(TabPane tp) { tabPane = tp; }
    public static HashMap<Tab, AnchorPane> getAllPanels() { return allPanels; }
    public static Tab getCurrentTab() { return currentTab; }
    public static AnchorPane getCurrentPanel() { return currentPanel; }
}
