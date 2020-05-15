package controllers;

import application.GUI;
import iec61850.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Александр Холодов
 * @created 04/2020
 * @project OpenIEDconfigurator
 * @description - Синхронизация между новым CLD и текущим
 */
public class CLDVersionControl {

    /**
     * Скопировать данные из нового CLD в текущий
     * @param newCLD
     */
    public static void synchronizeCLD(CLD newCLD){
        for(IED newIed:newCLD.getIedList())
            if(!synchronizeIED(newIed)) GUI.writeErrMessage(String.format("IED %s is not found", newIed.getName())); // Синхронизация IED
    }

    /**
     * Синхронизировать IED
     * @param newIED - IED из нового CLD
     * @return false если IED не найден
     */
    private static boolean synchronizeIED(IED newIED){
        CLD currentCLD = ProjectController.getCld();
        for(IED currentIED:currentCLD.getIedList()){
            if(currentIED.getName().equals(newIED.getName())){

                currentIED.setDescription(newIED.getDescription());

                for(LD newLD:newIED.getLogicalDeviceList())
                    if(!synchronizeLD(currentIED, newLD)) GUI.writeErrMessage(String.format("LD %s is not found", newLD.getName())); // Синхронизация LD

                currentIED.getConnectionList().clear();
                for(Connection connection:newIED.getConnectionList())
                    currentIED.getConnectionList().add(new Connection(connection.getSourceID(), connection.getTargetID()));          // Сонхронизация соединений
                return true;
            }
        }
        return false;
    }

    /**
     * Синхронизировать LD
     * @param newLD - LD из нового CLD
     * @param currentIED - IED из текущего проекта
     * @return false если LD не найден
     */
    private static boolean synchronizeLD(IED currentIED, LD newLD){
        for(LD currentLD:currentIED.getLogicalDeviceList()){
            if(currentLD.getName().equals(newLD.getName())){

                currentLD.setDescription(newLD.getDescription());

                for(LN newLN:newLD.getLogicalNodeList())
                    if(!synchronizeLN(currentLD, newLN)) GUI.writeErrMessage(String.format("LD %s is not found", newLD.getName())); // Синхронизация LN

                for(DS newDS:newLD.getGooseInputDS())
                    if(!synchronizeDS(currentLD, newDS)) GUI.writeErrMessage(String.format("DS %s is not found", newDS.getName())); // Синхронизация DS

                for(DS newDS:newLD.getMmsOutputDS())
                    if(!synchronizeDS(currentLD, newDS)) GUI.writeErrMessage(String.format("DS %s is not found", newDS.getName())); // Синхронизация DS

                for(DS newDS:newLD.getMmsOutputDS())
                    if(!synchronizeDS(currentLD, newDS)) GUI.writeErrMessage(String.format("DS %s is not found", newDS.getName())); // Синхронизация DS

                currentLD.getConnectionList().clear();
                for(Connection connection:newLD.getConnectionList())
                    currentLD.getConnectionList().add(new Connection(connection.getSourceID(), connection.getTargetID()));          // Сонхронизация соединений

                return true;
            }
        }
        return false;
    }

    /**
     * Синхронизировать LN
     * @param newLN - LN из нового CLD
     * @param currentLD - LD из текущего проекта
     * @return false если LN не найден
     */
    private static boolean synchronizeLN(LD currentLD, LN newLN){
        for(LN currentLN:currentLD.getLogicalNodeList()){
            if(currentLN.getName().equals(newLN.getName())){

                currentLN.setDescription(newLN.getDescription());
                currentLN.setLayoutX(newLN.getLayoutX());
                currentLN.setLayoutY(newLN.getLayoutY());

                /* Поиск родительских DO */
                findParentsDO(currentLN.getDataSetInput().getDataObject());
                findParentsDO(currentLN.getDataSetOutput().getDataObject());
                findParentsDO(newLN.getDataSetInput().getDataObject());

                /* Построение адресов для DO */
                HashMap<String, DO> currentMapInDO = new HashMap<>(); createDOmap(currentMapInDO, currentLN.getDataSetInput().getDataObject());
                HashMap<String, DO> newMapInDO = new HashMap<>(); createDOmap(newMapInDO, newLN.getDataSetInput().getDataObject());

                /* Копирование значений (уставок тоже) */
                for(Map.Entry<String, DO> newEntry:newMapInDO.entrySet()){
                    DO currentDO = currentMapInDO.get(newEntry.getKey());
                    if(currentDO != null) currentDO.setValue(newEntry.getValue().getValue());
                }

                return true;
            }
        }
        return false;
    }

    /**
     * Синхронизировать DS (отдельные DS)
     * @param newDS - DS из нового CLD
     * @param currentLD - LD из текущего проекта
     * @return false если LN не найден
     */
    private static boolean synchronizeDS(LD currentLD, DS newDS){
        DS currentDS = null;

        for(DS ds:currentLD.getGooseInputDS()) if(ds.getName().equals(newDS.getName())){ currentDS = ds; break; }                           // Поиск в GooseInput
        if(currentDS == null) for(DS ds:currentLD.getGooseOutputDS()) if(ds.getName().equals(newDS.getName())){ currentDS = ds; break; }    // Поиск в GooseOutput
        if(currentDS == null) for(DS ds:currentLD.getMmsOutputDS()) if(ds.getName().equals(newDS.getName())){ currentDS = ds; break; }      // Поиск в MMSOutput

        /* Если DS нейден копируем данные */
        if(currentDS != null){

            currentDS.setDescription(newDS.getDescription());
            currentDS.setLayoutX(newDS.getLayoutX());
            currentDS.setLayoutY(newDS.getLayoutY());
            currentDS.setBufTime(newDS.getBufTime());
            currentDS.setID(newDS.getID());

            return true;
        }
        return false;
    }

    /**
     * Создать словать DO в заданном датасете
     * @param dataObjects - лист DO из DS
     * @return - словать, ключ - относительный адрес DO, значение - Value
     */
    private static HashMap<String, DO> createDOmap(HashMap<String, DO> doMap, ArrayList<DO> dataObjects){
        /* Добавление DO в словать */
        for(DO dataObject:dataObjects) {
            if(dataObject.getDataAttributeName() != null){
                DO currentDO = dataObject;
                String address = dataObject.getDataAttributeName();
                while(currentDO != null){
                    currentDO = currentDO.getParentDO();
                    if(currentDO != null) address = currentDO.getDataAttributeName() + "\\" + address;
                }
                doMap.put(address, dataObject);
            }
        }
        /* Добавление DO во вложениях в словать */
        for(DO dataObject:dataObjects) if(!dataObject.getContent().isEmpty()) createDOmap(doMap, dataObject.getContent());
        return doMap;
    }

    /**
     * Найти в каждом DO родительский класс
     */
    public static void findParentsDO(ArrayList<DO> dataObjects){
        for(DO parentDO:dataObjects) for(DO childDO:parentDO.getContent()) childDO.setParentDO(parentDO);       // Заполняем родителя
        for(DO parentDO:dataObjects) if(!parentDO.getContent().isEmpty()) findParentsDO(parentDO.getContent()); // ищем родителей глубже
    }

}
