package controllers;

import application.GUI;
import controllers.elements.GraphicNode;
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

import java.util.HashMap;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Класс для создания панелей проекта
 */
public class PanelsController {

    private static TabPane tabPane;
    private static HashMap<Tab, AnchorPane> allTabs = new HashMap<>();
    private static AnchorPane selectedPanel;
    private static Tab selectedTab;
    private static double scale = 1;

    /**
     * Создать новую вкладку
     * @param name - Название
     */
    public static void createTab(String name){
        for(Tab tab: allTabs.keySet()) if(tab.getId().equals(name)) { GUI.writeErrMessage("Панель уже существует "+name); return; }

        AnchorPane pane = new AnchorPane(); pane.setId(name);
        pane.setPrefHeight(2160); pane.setPrefWidth(3840);
        pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        pane.setStyle(" -fx-background-color: #ecf0f1;");
        ProjectController.addPaneToListening(pane);
        pane.setOnContextMenuRequested(e->{ ContextMenuController.showContextMenu(ContextMenuController.getMainContextMenu(), e); });
        pane.addEventFilter(MouseEvent.MOUSE_PRESSED, e ->{  ContextMenuController.hideContextMenu(); });

        pane.setStyle("-fx-border-color: -fx-first-color; -fx-background-color: -fx-fourth-color,\n" +
                "        linear-gradient(from 0.1px 0.0px to 15.1px  0.0px, repeat, rgba(119,119,119,0.15) 3%, transparent 0%),\n" +
                "        linear-gradient(from 0.0px 0.1px to  0.0px 15.1px, repeat, rgba(119,119,119,0.15) 3%, transparent 0%);");

        Tab tab = new Tab(name); tab.setId(name); tab.setClosable(false); allTabs.put(tab, pane); tabPane.getTabs().add(tab);

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
                scale = 1/zoomTarget.getScaleX();
                GUI.getZoomLabel().setText((((double)Math.round(100/scale))/100)+"x");
                scrollPane.layout(); // refresh ScrollPane scroll positions & content bounds

                groupBounds = group.getLayoutBounds();
                scrollPane.setHvalue((valX + adjustment.getX()) / (groupBounds.getWidth() - viewportBounds.getWidth()));
                scrollPane.setVvalue((valY + adjustment.getY()) / (groupBounds.getHeight() - viewportBounds.getHeight()));
            }
        });
    }

    public static GraphicNode createNode(Object userData){
        GraphicNode node = new GraphicNode();
        node.prepareHandlers();
        node.setUserData(userData);
        node.setDraggable(true);
        selectedPanel.getChildren().add(node);
        return node;
    }

    public static void removeTab(String name){ for(Tab tab: allTabs.keySet()) if(tab.getId().equals(name)) { tabPane.getTabs().remove(tab); allTabs.remove(tab); return; } }
    public static void clear(){ tabPane.getTabs().clear(); allTabs.clear();  }

    public static AnchorPane getSelectedPanel() { return selectedPanel; }
    public static void setTabPane(TabPane tabPane) { PanelsController.tabPane = tabPane; PanelsController.tabPane.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> { selectedTab = nv; selectedPanel = allTabs.get(nv); } );  }
    public static double getScale() { return scale; }
}
