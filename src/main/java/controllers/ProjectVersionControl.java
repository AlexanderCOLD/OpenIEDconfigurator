package controllers;

import application.GUI;
import controllers.dialogs.AssistDialog;
import controllers.graphicNode.Connector;
import controllers.graphicNode.GraphicNode;
import controllers.graphicNode.GraphicNodeController;
import controllers.link.Link;
import controllers.link.LinkController;
import controllers.tree.TreeController;
import iec61850.*;
import iec61850.objects.SCL;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import tools.SaveLoadObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Александр Холодов
 * @created 04/2020
 * @project OpenIEDconfigurator
 * @description - сохранение проекта (CID + CLD)
 */
public class ProjectVersionControl {


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
                    CLD cld = new CLD();
                    ArrayList<IED> iedList = IEDExtractor.extractIEDList(scl); // Извлекаем данные из SCL
                    cld.setIedList(iedList);

                    TreeController.updateTreeObjects(cld.getIedList()); // Строим дерево
                    PanelsController.updateTabObjects(cld.getIedList()); // Создаем вкладки
                    GraphicNodeController.updateNodeObjects(cld.getIedList()); // Создаем граф. элементы

                    ProjectController.setScl(scl);
                    ProjectController.setCld(cld);
                    ProjectController.setFileCID(file);

                    String path = file.getParent(), name = file.getName().replaceAll(".cid",".cld");
                    File cldFile = new File(path + "\\" + name);
                    if(cldFile.exists()) if(AssistDialog.requestConfirm("Найден CLD", String.format("Хотите открыть? \n%s", cldFile.getPath()))) openNewCLD(cldFile);
                    else GUI.get().handleOpenCLD();
                }
                else{
                    AssistDialog.requestError("Ошибка", "Невозможно открыть SCL\nВерсия SCL отличается от 2006");
                    GUI.writeErrMessage("Невозможно открыть SCL, версия SCL отличается от 2006");
                }
            });
        }
    }

    /**
     * Загрузить новый CLD файл
     * @param file - CLD
     */
    public static void openNewCLD(File file){
        if(file == null) return;

        CLD newCld = SaveLoadObject.load(CLD.class, file);  // CLD с которым нужно синхронизироваться
        if(newCld == null){ GUI.writeErrMessage("Error load SLD: " + file.toString()); return; }

        /* Удаляем текущие соединения */
        for(Link link:LinkController.getConnections()) link.remove();

        /* Удаляем такущие графические элементы (с панелей) */
        for(GraphicNode graphicNode:GraphicNodeController.getProjectNodeList().values()) graphicNode.remove();

        /* Скопировать данные из нового CLD */
        CLDVersionControl.synchronizeCLD(newCld);

        /* Отрисовать графические элементы */
        for(GraphicNode graphicNode:GraphicNodeController.getProjectNodeList().values()){
            LD ld = TreeController.getParentObject(LD.class, graphicNode.getUserData());
            if(ld != null){
                AnchorPane pane = PanelsController.getPanel(ld);
                if(pane != null){

                    double layoutX = -1.0, layoutY = -1.0;
                    if(graphicNode.getUserData().getClass() == LN.class) { LN ln = (LN) graphicNode.getUserData(); layoutX = ln.getLayoutX(); layoutY = ln.getLayoutY(); }
                    else if(graphicNode.getUserData().getClass() == DS.class){ DS ds = (DS) graphicNode.getUserData(); layoutX = ds.getLayoutX(); layoutY = ds.getLayoutY(); }

                    if(Double.compare(layoutX, -1.0) != 0 && Double.compare(layoutY, -1.0) != 0){
                        pane.getChildren().add(graphicNode);
                        graphicNode.relocate(layoutX, layoutY);         // Если координаты есть, отрисовываем граф. элемент и задаем координаты
                        Platform.runLater(graphicNode::updateGrid);
                    }
                }
            }
        }

        /* Восстанавливаем соединения */
        for(IED ied:ProjectController.getCld().getIedList())
            for(LD ld:ied.getLogicalDeviceList())
                for(Connection connection:ld.getConnectionList()){
                    Connector sourceConnector = connectorByAddress(connection.getSourceID());
                    Connector targetConnector = connectorByAddress(connection.getTargetID());
                    if(sourceConnector != null && targetConnector != null){
                        Pane sourcePane = (Pane) sourceConnector.getGraphicNode().getParent();
                        Pane targetPane = (Pane) targetConnector.getGraphicNode().getParent();
                        if(sourcePane != null && targetPane != null && sourcePane == targetPane){
                            Link link = new Link(); link.setSourceConnector(sourceConnector); link.setTargetConnector(targetConnector);
                            sourcePane.getChildren().add(link);
                            Platform.runLater(link::createConnection);
                        }
                    }
                }
    }

    /**
     * Сохранить проект в файл
     * @param file - файл с проектом .CLD
     */
    public static void saveProject(File file){

        if(ProjectController.getFileCID() == null) { GUI.writeErrMessage("CID file is not opened"); return; }

        try {
            String path = file.getParent();                                                 // Путь
            String name = file.getName().replaceAll(".cld","");           // Название проекта

            String[] pathList = path.split("\\\\");
            if(!pathList[pathList.length-1].equals(name)) path += "\\" + name;       // Если отсутствует дирректория с названием проекта, - создаем её
            File directory = new File(path);
            if(!directory.exists()) Files.createDirectories(directory.toPath());

            File newCIDFile = new File(path + "\\" + name + ".cid");
            File newCLDFile = new File(path + "\\" + name + ".cld");

            File oldCIDFile = ProjectController.getFileCID();
            if(!oldCIDFile.toPath().equals(newCIDFile.toPath()))
                try { Files.copy(oldCIDFile.toPath(), newCIDFile.toPath(), StandardCopyOption.REPLACE_EXISTING); } catch (IOException e) { e.printStackTrace();	} // Копируем CID в папку (чтобы лежали в одном месте)

            CLD cld = ProjectController.getCld();
            for(IED ied:cld.getIedList()) { ied.getConnectionList().clear(); for(LD ld:ied.getLogicalDeviceList()) ld.getConnectionList().clear(); } // Удаляем предыдущие соединения

            for(Link link: LinkController.getConnections()){
                Connection connection = new Connection(addressOfConnector(link.getSourceConnector()), addressOfConnector(link.getTargetConnector()));
                LD currentLD = TreeController.getParentObject(LD.class, link.getSourceConnector().getGraphicNode().getUserData()); // Берем LD в котором находится данное соединение
                if(currentLD != null) currentLD.getConnectionList().add(connection); else System.err.println("Connection: LD is not found");
            }

            SaveLoadObject.save(cld, newCLDFile);
            GUI.writeMessage("Project saved: " + newCLDFile.toString());

            ProjectController.setFileCID(newCIDFile);
            ProjectController.setFileCLD(newCLDFile);

        } catch (Exception e) {
            e.printStackTrace();

            GUI.writeErrMessage("Error project saving"); }

    }

    /**
     * Возвращает адрес объекта данных
     * @param connector - коннектор у которого нужно найти адрес
     * @return - (IED/LD/LN/DO) - если DO принадлежит LN
     *  (IED/LD/DS/DO) - если DO принадлежит DS (GOOSE/MMS)
     */
    private static String addressOfConnector(Connector connector){
        DO dataObject = connector.getDataObject();
        Object object = connector.getGraphicNode().getUserData(); // На текущий момент это LN или DS

        String iedName = Objects.requireNonNull(TreeController.getParentObject(IED.class, object)).getName();
        String ldName = Objects.requireNonNull(TreeController.getParentObject(LD.class, object)).getName();
        String doName = dataObject.getDataAttributeName();

        if(object.getClass() == LN.class){
            String lnName = ((LN) object).getName();
            return iedName + "\\" + ldName + "\\" + lnName + "\\" + doName;
        }
        else if (object.getClass() == DS.class){
            String dsName = connector.getDataSet().getName();
            return iedName + "\\" + ldName + "\\" + dsName + "\\" + doName;
        }
        else {
            System.err.println("Address DO: Unknown type");
            return "address_unknown";
        }
    }

    /**
     * Возвращает коннектор по его адресу (обратное действие от addressOfConnector)
     * @param address
     * @return коннектор
     */
    private static Connector connectorByAddress(String address){
        String[] addressList = address.split("\\\\");
        if(addressList.length == 4){
            String iedName = addressList[0], ldName = addressList[1], lnDsName = addressList[2], doName = addressList[3];

            for(IED ied:ProjectController.getCld().getIedList()){
                if(ied.getName().equals(iedName)){
                    for(LD ld:ied.getLogicalDeviceList()){
                        if(ld.getName().equals(ldName)){

                            GraphicNode graphicNode = null;

                            for(LN ln:ld.getLogicalNodeList()) if(ln.getName().equals(lnDsName)){ graphicNode = GraphicNodeController.getProjectNodeList().getValue(ln); break; }
                            if(graphicNode == null) for(DS ds:ld.getMmsOutputDS()) if(ds.getName().equals(lnDsName)){ graphicNode = GraphicNodeController.getProjectNodeList().getValue(ds); break; }
                            if(graphicNode == null) for(DS ds:ld.getGooseInputDS()) if(ds.getName().equals(lnDsName)){ graphicNode = GraphicNodeController.getProjectNodeList().getValue(ds); break; }
                            if(graphicNode == null) for(DS ds:ld.getGooseOutputDS()) if(ds.getName().equals(lnDsName)){ graphicNode = GraphicNodeController.getProjectNodeList().getValue(ds); break; }

                            /* Поиск непосредственно коннектора */
                            if(graphicNode != null) for(Connector connector:graphicNode.getConnectors()) if(connector.getDataObject().getDataAttributeName().equals(doName)) return connector;
                        }
                    }
                }
            }
        }

        GUI.writeErrMessage("Connector is not found by address:  " + address);
        return null;
    }

}
