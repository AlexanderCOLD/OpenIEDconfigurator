package iec61850;

import application.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Александр Холодов
 * @created 04/2020
 * @project OpenIEDconfigurator
 * @description - Некоторые функции CLD
 */
public class CLDUtils {


    /**
     * Копирование данных (из source в target)
     * @param source - источник (новый CLD)
     * @param target - назначение (текущий CLD)
     */
    public static void syncCLD(CLD source, CLD target){

        /* Построение справочника объектов */
        HashMap<String, IECObject> sourceMap = mapOf(source);
        HashMap<String, IECObject> targetMap = mapOf(target);

        /* Поиск объектов по их адресами и синхронизация */
        for(Map.Entry<String, IECObject> sourceEntry:sourceMap.entrySet()){
            IECObject targetIEC = targetMap.get(sourceEntry.getKey());
            if(targetIEC != null) syncObject(sourceEntry.getValue(), targetIEC);
            else GUI.writeErrMessage(String.format("Object %s is not found", sourceEntry.getKey()));
        }
    }

    /**
     * Копирование данных (из source в target)
     * @param source - источник (из нового CLD)
     * @param target - назначение (из текущего CLD)
     */
    public static void syncObject(IECObject source, IECObject target){

        /* Если это объект перенесенный из нового CLD */
        if(source==target) return;

        /* Синхронизация координат */
        target.setLayoutX(source.getLayoutX()); target.setLayoutY(source.getLayoutY());

        /* Синхронизация тегов */
        target.getTags().clear(); target.getTags().addAll(source.getTags());

        /* Синхронизация атрибутов  */
        if(source.getClass()==DA.class) { ((DA) target).setValue(((DA) source).getValue()); }

        /* Сихронизация соединений внутри IED */
        else if(source.getClass()==IED.class){
            ((IED) target).getConnectionList().clear();
            for(Connection connection:((IED) source).getConnectionList())
                ((IED) target).getConnectionList().add(new Connection(connection.getSourceID(), connection.getTargetID()));
        }

        /* Синхронизация соединений внетри LD */
        else if(source.getClass()==LD.class){
            ((LD) target).getConnectionList().clear();
            for(Connection connection:((LD) source).getConnectionList())
                ((LD) target).getConnectionList().add(new Connection(connection.getSourceID(), connection.getTargetID()));
        }
    }


    /**
     * Дополнить новыми объектами (из source в target)
     * @param source - источник (новый CLD)
     * @param target - назначение (текущий CLD)
     * @return список добавленных объектов
     */
    public static ObservableList<IECObject> appendCLD(CLD source, CLD target){
        ObservableList<IECObject> appendedObjects = FXCollections.observableArrayList();

        /* Построение справочника объектов */
        HashMap<String, IECObject> sourceMap = mapOf(source);
        HashMap<String, IECObject> targetMap = mapOf(target);

        /* Добавление новых объектов */
        for(Map.Entry<String, IECObject> sourceEntry:sourceMap.entrySet()){
            if(!targetMap.containsKey(sourceEntry.getKey()) && sourceEntry.getValue().getTags().contains("additional"))
                if(appendObject(targetMap, sourceEntry)) appendedObjects.add(sourceEntry.getValue());
        }
        return appendedObjects;
    }

    /**
     * Добавить дополнительный логический узел
     * @param objectList - текущая объектная модель
     * @param object - дополнительный логический узел
     * @return true если успешно добавлен
     */
    private static boolean appendObject(HashMap<String, IECObject> objectList, Map.Entry<String, IECObject> object){
        if(object.getValue().getClass()==LN.class){
            String ldPath = new File(object.getKey()).getParent();
            try {
                LD ld = (LD) objectList.get(ldPath);
                ld.getLogicalNodeList().add((LN) object.getValue());
                return true;
            }
            catch (Exception e) {
                GUI.writeErrMessage("Невозможно добавить объект " + object.getValue().getName()+ ", путь не найден " + ldPath);
                return false;
            }
        }
        return false;
    }



    /**
     * Создать словать объектов из CLD
     * @param cld - файл CLD
     * @return - Словарь объектов, key - абсолютный адрес
     */
    private static HashMap<String, IECObject> mapOf(CLD cld){  return fillObjectMap(new HashMap<>(), cld.getChildren()); }
    /** Наполнить словать объектами (рекурсия) */
    private static HashMap<String, IECObject> fillObjectMap(HashMap<String, IECObject> map, ObservableList<IECObject> objectList){
        for(IECObject object:objectList) map.put(object.getAddress().fullWithSlash(), object);
        for(IECObject object:objectList) if(!object.getChildren().isEmpty()) fillObjectMap(map, object.getChildren());
        return map;
    }





    /**
     * Поиск всех соединений в CLD
     * (При загрузке проект, для восстановления связей)
     */
    public static ObservableList<Connection> connectionsOf(CLD cld){ return fillConnectionsList(FXCollections.observableArrayList(), cld.getChildren()); }
    /** Наполнить список соединениями (рекурсия) */
    private static ObservableList<Connection> fillConnectionsList(ObservableList<Connection> connectionList, ObservableList<IECObject> objectList){
        for(IECObject object:objectList) {
            if(object.getClass()== LD.class) connectionList.addAll(((LD) object).getConnectionList());
            else if(object.getClass()== IED.class) connectionList.addAll(((IED) object).getConnectionList());
        }
        for (IECObject object:objectList) if(!object.getChildren().isEmpty()) fillConnectionsList(connectionList, object.getChildren());
        return connectionList;
    }



    /**
     * Лист объектов в CLD
     */
    public static ObservableList<IECObject> objectListOf(CLD cld){ return fillObjectList(FXCollections.observableArrayList(), cld.getChildren()); }
    /** Лист объектов в CLD */
    public static ObservableList<IECObject> objectListOf(IECObject iecObject){ return fillObjectList(FXCollections.observableArrayList(), iecObject.getChildren()); }
    /** Наполнить словать объектами (рекурсия) */
    private static ObservableList<IECObject> fillObjectList(ObservableList<IECObject> targetList, ObservableList<IECObject> objectList){
        for(IECObject object:objectList) targetList.add(object);
        for(IECObject object:objectList) if(!object.getChildren().isEmpty()) fillObjectList(targetList, object.getChildren());
        return targetList;
    }


    /**
     * Взять родительский объект
     * @param classType - тип объекта
     * @param object - текущий объект
     * @return - родительский объект
     */
    public static <T> T parentOf(Class<T> classType, IECObject object){
        if(object.getClass()==classType) return (T) object;
        while(object!=null){ object = object.getParent(); if(object!=null && object.getClass()==classType) return (T) object; }
        return null;
    }

}
