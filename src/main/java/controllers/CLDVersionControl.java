package controllers;

import application.GUI;
import iec61850.*;

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
     * @param newIED
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
     * @param newLD
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
     * @param newLN
     * @return false если LN не найден
     */
    private static boolean synchronizeLN(LD currentLD, LN newLN){
        for(LN currentLN:currentLD.getLogicalNodeList()){
            if(currentLN.getName().equals(newLN.getName())){

                currentLN.setDescription(newLN.getDescription());
                currentLN.setLayoutX(newLN.getLayoutX());
                currentLN.setLayoutY(newLN.getLayoutY());

                return true;
            }
        }
        return false;
    }

    /**
     * Синхронизировать DS
     * @param currentObject - LN либо LD
     * @return false если LN не найден
     */
    private static boolean synchronizeDS(Object currentObject, DS newDS){
        if(currentObject.getClass() == LD.class){

            LD currentLD = (LD) currentObject;
            DS currentDS = null;

            for(DS ds:currentLD.getGooseInputDS()) if(ds.getName().equals(newDS.getName())){ currentDS = ds; break; }                           // Поиск в GooseInput
            if(currentDS == null) for(DS ds:currentLD.getGooseOutputDS()) if(ds.getName().equals(newDS.getName())){ currentDS = ds; break; }    // Поиск в GooseOutput
            if(currentDS == null) for(DS ds:currentLD.getMmsOutputDS()) if(ds.getName().equals(newDS.getName())){ currentDS = ds; break; }      // Поиск в MMSOutput

            if(currentDS != null){

                currentDS.setDescription(newDS.getDescription());
                currentDS.setLayoutX(newDS.getLayoutX());
                currentDS.setLayoutY(newDS.getLayoutY());
                currentDS.setBufTime(newDS.getBufTime());
                currentDS.setId(newDS.getId());

                return true;
            }
        }
        return false;
    }

}
