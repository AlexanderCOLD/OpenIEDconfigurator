package controllers;

import application.GUI;
import controllers.dialogs.AssistantDialog;
import controllers.dialogs.InfoDialog;
import controllers.graphicNode.Connector;
import controllers.graphicNode.GraphicNode;
import controllers.graphicNode.GraphicNodeController;
import controllers.link.Link;
import controllers.link.LinkController;
import controllers.tree.TreeController;
import iec61850.*;
import iec61850.objects.SCL;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import tools.SaveLoadObject;
import tools.Settings;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Александр Холодов
 * @created 04/2020
 * @project OpenIEDconfigurator
 * @description - Контроллер для управления всем проектом
 */

public class ProjectController {

    /** Текущие файлы CID и CLD */
    public static File fileCID, fileCLD;

    /** Текущий CLD */
    public static CLD cld;

    /** Текущий CID */
    public static SCL scl;

    /** Выделенный элемент */
    private static IECObject selectedObject;

    /**
     * Задать активный элемент
     * @param iecObject - объект МЭК 61850
     */
    public static void setSelectedObject(IECObject iecObject){
        if(iecObject != null) InfoDialog.setObject(iecObject);                   // Отображаем параметры элемента
        if(iecObject != null && iecObject != selectedObject){
            selectedObject = iecObject;

            GraphicNodeController.setSelectedObject(iecObject);                  // Выделяем графический элемент
            TreeController.setSelectedObject(iecObject);                         // Выделяем ветку дерева
            PanelsController.setSelectedObject(TreeController.getSelectedLD());  // Переходим на нужную вкладку
        }
    }

    /**
     * Загрузить предудущий проект
     * (Во время запуска программы)
     */
    public static void openLastProject(){
        new Thread(() -> {
            try { Thread.sleep(2000); } catch (Exception ignored) {}
            if(Settings.lastCIDPath!=null) {
                File file = new File(Settings.lastCIDPath);
                if(file.exists()){
                    Platform.runLater(() -> {
                        GUI.writeMessage("Найден предыдущий проект: " + file.getPath());
                        if(!AssistantDialog.requestConfirm("Найден предыдущий проект", String.format("Хотите открыть предыдущий проект? \n%s", file.getPath()))) return;
                        openNewCID(file);
                    });
                }
            }
        }){{ start(); }};
    }


    /**
     * Загрузить новый CID файл
     * @param file - CID файл
     */
    public static void openNewCID(File file){
        if(file == null) return;
        if(file.exists()){
            Platform.runLater(() -> {
                SCL scl = SaveLoadObject.load(SCL.class, file);
                if(scl != null) {
                    CLD cld = CLDBuilder.buildCLD(scl);           // Извлекаем данные из SCL
                    cld.setCidFile(file);

                    TreeController.updateTreeObjects(cld);        // Строим дерево
                    PanelsController.updateTabObjects(cld);       // Создаем вкладки
                    GraphicNodeController.updateNodeObjects(cld); // Создаем граф. элементы

                    ProjectController.scl = scl;
                    ProjectController.cld = cld;
                    ProjectController.fileCID = file;

                    GUI.writeMessage("Открыт CID проект: " + file.getAbsolutePath());

                    String path = file.getParent(), name = file.getName().replaceAll(".cid",".cld");
                    File cldFile = new File(path + "\\" + name);
                    if(cldFile.exists()) if(AssistantDialog.requestConfirm("Найден CLD", String.format("Хотите открыть? \n%s", cldFile.getPath()))) importCLD(cldFile);
                }
                else{
                    AssistantDialog.requestError("Ошибка", "Невозможно открыть SCL\nВозможно версия SCL отличается от 2006");
                    GUI.writeErrMessage("Невозможно открыть SCL, возможно версия SCL отличается от 2006");
                }
            });
        }
    }

    /**
     * Загрузить новый CLD файл
     * @param file - CLD
     */
    public static void importCLD(File file){
        if(file == null) return;

        CLD newCld = SaveLoadObject.load(CLD.class, file);  // CLD с которым нужно синхронизироваться
        if(newCld == null){ GUI.writeErrMessage("Error load SLD: " + file.toString()); return; }

        /* Удаляем текущие соединения */
        for(Link link: new ArrayList<>(LinkController.getConnections())) link.remove();

        /* Удаляем такущие графические элементы (с панелей), если элемент дополнительный - будет удален навсегда */
        for(GraphicNode graphicNode: new ArrayList<>(GraphicNodeController.getActiveNodeList().values())) graphicNode.remove();

        /* Скопировать данные из нового CLD */
        CLDUtils.syncCLD(newCld, cld);


        /* Добавить в проект дополнительные элементы */
        ObservableList<IECObject> appendedList = CLDUtils.appendCLD(newCld, cld);
        for(IECObject iecObject:appendedList){
            if(iecObject.getClass()==LN.class){
                GraphicNodeController.fillByTemplate((LN) iecObject);
                GraphicNodeController.getProjectNodeList().put(iecObject.getUID(), GraphicNodeController.createGraphicNode(iecObject));
            }
        }


        /* Отрисовать графические элементы (ProjectNodeList - все текущие элементы)*/
        for(GraphicNode graphicNode:GraphicNodeController.getProjectNodeList().values()){

            IECObject iecObject = graphicNode.getIecObject();
            LD ld = CLDUtils.parentOf(LD.class, iecObject);
            AnchorPane pane = PanelsController.getPanel(ld);

            double layoutX = iecObject.getLayoutX(), layoutY = iecObject.getLayoutY();

            /* Если координаты есть, отрисовываем граф. элемент и задаем координаты */
            if(Double.compare(layoutX, -1.0) != 0 && Double.compare(layoutY, -1.0) != 0){
                pane.getChildren().add(graphicNode);
                graphicNode.relocate(layoutX, layoutY);
                Platform.runLater(graphicNode::updateGrid);
            }
        }


        /* Восстанавливаем соединения */

        /* Все соединения */
        ObservableList<Connection> connectionList = CLDUtils.connectionsOf(cld);

        /* Все коннекторы (с адресами) */
        HashMap<String, Connector> connectorMap = new HashMap<>();
        for(GraphicNode node:GraphicNodeController.getActiveNodeList().values())
            for(Connector connector:node.getConnectors()) connectorMap.put(CLDUtils.addressOf(connector.getIecObject()), connector);

        /* Поиск коннекторов и восстановление */
        for(Connection connection:connectionList){
            Connector source = connectorMap.get(connection.getSourceID());
            Connector target = connectorMap.get(connection.getTargetID());
            if (source != null && target != null) {
                Link link = new Link(); link.setSourceConnector(source); link.setTargetConnector(target);
                Pane pane = (Pane) source.getGraphicNode().getParent(); pane.getChildren().add(link);
                Platform.runLater(link::createConnection);
            }
            else GUI.writeErrMessage("Невозможно восстановить соединение :" + connection.getSourceID() + " -> " + connection.getTargetID());
        }
        GUI.writeMessage("CLD импортирован: " + file.getAbsolutePath());
    }

    /**
     * Сохранить проект в файл
     * @param file - файл с проектом .CLD
     */
    public static void saveProject(File file){

        if(fileCID == null) { GUI.writeErrMessage("CID file is not opened"); return; }

        try {
            String path = file.getParent();                                                 // Путь
            String name = file.getName().replaceAll(".cld","");           // Название проекта

            /* Если отсутствует дирректория с названием проекта, - создаем её */
            String[] pathList = path.split("\\\\");
            if(!pathList[pathList.length-1].equals(name)) path += "\\" + name;
            File directory = new File(path);
            if(!directory.exists()) Files.createDirectories(directory.toPath());

            File newCIDFile = new File(path + "\\" + name + ".cid");
            File newCLDFile = new File(path + "\\" + name + ".cld");

            /* Копируем CID в папку (чтобы лежали в одном месте) */
            if(!fileCID.toPath().equals(newCIDFile.toPath()))
                try { Files.copy(fileCID.toPath(), newCIDFile.toPath(), StandardCopyOption.REPLACE_EXISTING); } catch (IOException e) { e.printStackTrace();	}

            /* Удаляем предыдущие соединения */
            for(IED ied:cld.getIedList()) { ied.getConnectionList().clear(); for(LD ld:ied.getLogicalDeviceList()) ld.getConnectionList().clear(); }

            /* Создаем новые */
            for(Link link: LinkController.getConnections()){
                Connection connection = new Connection(CLDUtils.addressOf(link.getSourceConnector().getIecObject()), CLDUtils.addressOf(link.getTargetConnector().getIecObject()));
                LD currentLD = CLDUtils.parentOf(LD.class, (IECObject) link.getSourceConnector().getGraphicNode().getIecObject()); // Берем LD в котором находится данное соединение
                if(currentLD != null) currentLD.getConnectionList().add(connection); else System.err.println("Connection: LD is not found");
            }

            SaveLoadObject.save(cld, newCLDFile);
            GUI.writeMessage("Проект сохранен: " + newCLDFile.toString());

            fileCID = newCIDFile;
            fileCLD = newCLDFile;
            Settings.lastCLDPath = fileCLD.getPath();

        } catch (Exception e) { e.printStackTrace(); GUI.writeErrMessage("Ошибка сохранения проекта"); }
    }

}
