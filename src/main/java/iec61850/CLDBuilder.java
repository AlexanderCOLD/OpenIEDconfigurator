package iec61850;

import application.GUI;
import controllers.graphicNode.GraphicNodeController;
import iec61850.objects.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description Извлечение необходимых данных из CID (IED, LD, LN, DS, DO)
 */
public class CLDBuilder {

    /**
     * Создает CLD из CID
     * @param scl - Искомый SCL
     * @return - CLD
     */
    public static CLD buildCLD(SCL scl){
        CLD cld = new CLD();
        for(TIED ied:scl.getIED()) Optional.ofNullable(extractIED(ied)).ifPresent(cld.getIedList()::add);
        fillDatasets(cld);
        return cld;
    }

    /** Поиск типо данных для датасетов */
    private static void fillDatasets(CLD cld){
        for(IED ied: cld.getIedList()){
            for(LD ld: ied.getLogicalDeviceList()){

                /* Поиск всех DS и LN в проекте */
                ObservableList<IECObject> iecObjects = CLDUtils.objectListOf(ld);
                ObservableList<IECObject> lnList = FXCollections.observableArrayList(iecObjects.stream().filter(o -> o.getClass()==LN.class).collect(Collectors.toList()));
                ObservableList<IECObject> dsList = FXCollections.observableArrayList(iecObjects.stream().filter(o -> o.getClass()==DS.class).collect(Collectors.toList()));

                /* Дополнение логических узлов объектной моделью */
                lnList.forEach(ln -> GraphicNodeController.fillByTemplate((LN) ln));

                HashMap<String, IECObject> ldMap = CLDUtils.mapOf(ld.getChildren());

                if(dsList.isEmpty()) continue;

                /* Поиск типов */
                for(IECObject ds: dsList){
                    for(DA da: ((DS) ds).getDataAttributes()){

                        /* Путь до объекта */
                        String daPath = ied.getName() + "/" + ld.getName() + "/" + da.getName();

                        IECObject sourceAttribute = ldMap.get(daPath);
                        if(sourceAttribute==null) continue;

                        IECObject object = sourceAttribute;
                        while (object!=null) if(object.getParent().getClass()!=LN.class) object = object.getParent(); else break;

                        da.setType(object.getType());
                        da.setCppType(object.getCppType());
                    }
                }

            }
        }
    }

    /**
     * Извлекает IED
     * @param source - TIED (SCL)
     * @return - IED
     */
    private static IED extractIED(TIED source){
        IED ied = new IED();

        /* Имя экземпляра IED */
        if(source.getName()==null) {
            ied.setName("unknown"); ied.setCppName("unknown");
            GUI.writeErrMessage("IED:  "+ ied + "- не имеет названия");
        } else { ied.setName(source.getName().toLowerCase()); ied.setCppName(ied.getName()); }

        /* Описание IED */
        ied.setDescription(source.getDesc()!=null ? source.getDesc() : "unknown");

        /* Извлечение LD */
        /* Если существует Access Point, если в нем есть Server, если в сервере есть LD's, то извлекаем их */
        try {
            List<TLDevice> logicalDeviceList = source.getAccessPoint().get(0).getServer().getLDevice();
            for(TLDevice tld:logicalDeviceList) Optional.ofNullable(extractLD(tld)).ifPresent(ied.getLogicalDeviceList()::add);
        } catch (Exception e) { GUI.writeErrMessage(String.format("IED: %s не имеет логических узлов", ied.getName())); }
        return ied;
    }

    /**
     * Извлекает LD
     * @param source - TLDevice
     * @return - LD
     */
    private static LD extractLD(TLDevice source){
        LD ld = new LD();

        /* Имя LD */
        if(source.getLdName()==null) {
            ld.setType("unknown");
            GUI.writeErrMessage(String.format("LD: %s - не имеет названия", source));
        } else ld.setType(source.getLdName());

        /* Имя экземпляра LD (Instance) */
        if(source.getInst()==null) {
            ld.setName("unknown");
            GUI.writeErrMessage(String.format("LD: %s - не имеет названия экземпляра", source));
        } else { ld.setName(source.getInst().toLowerCase()); ld.setCppName(ld.getName()); }

        /* Описание LD */
        ld.setDescription(source.getDesc()!=null ? source.getDesc(): "unknown");

        /* Извлекаем логические узлы если есть */
        for(TLN tln:source.getLN()) Optional.ofNullable(extractLN(tln)).ifPresent(ld.getLogicalNodeList()::add);

        if(source.getLN0()==null){
            GUI.writeErrMessage(String.format("LD %s не имеет узла LN0", source.getLdName()));
            return ld;
        }

        /* Извлекаем датасеты (отличны от null) */
        List<TDataSet> tDataSetList = source.getLN0().getDataSet();               // Общий лист со всеми исходящими датасетами (GOOSE и MMS)
        List<TGSEControl> outputGooseList = source.getLN0().getGSEControl();      // Описание исходящих гусей (содержатся в dataSetList)
        List<TReportControl> outputMMSList = source.getLN0().getReportControl();  // Описание исходящих MMS (содержатся в dataSetList)
        List<TExtRef> inputGooseDS = source.getLN0().getInputs()!=null ?          // Датасет входящих гусей
                source.getLN0().getInputs().getExtRef() : new ArrayList<>();



        /* Извлечение исходящих гусей */
        for(TGSEControl tgseControl:outputGooseList){
            DS ds = new DS();

            /* Название DS */
            if(tgseControl.getName()==null) {
                ds.setName("unknown");
                GUI.writeErrMessage(String.format("%s DataSet: GSEControlBlock  %s - не имеет названия", DSType.GOOSE_OUT.toString(), tgseControl));
            } else ds.setName(tgseControl.getName());

            /* Тип DS */
            ds.setType(DSType.GOOSE_OUT.toString());

            /* Описание */
            ds.setDescription(tgseControl.getDesc()!=null ? tgseControl.getDesc() : "Датасет исходящих GOOSE сообщений");

            /* Имя datSet */
            String datSetName = null;
            if(tgseControl.getDatSet()==null) {
                GUI.writeErrMessage(String.format("%s DataSet: GSEControlBlock  %s - is не имеет DatSetName", DSType.GOOSE_OUT.toString(), tgseControl));
                datSetName = "unknown";
            } else datSetName = tgseControl.getDatSet();


            /* Добавление атрибута: Имя datSet */
            DA dsNameAttr = createDA("String", "datSetName","set_datSetName", "Название datSet", datSetName);
            ds.getDataAttributes().add(dsNameAttr);

            /* Добавление атрибута: Идентификатор приложения */
            DA appIdAttr = createDA("String", "appID", "set_appID", "Идентификатор приложения", tgseControl.getAppID()!=null ? tgseControl.getAppID() : "unknown");
            ds.getDataAttributes().add(appIdAttr);

            /* Добавление атрибута: Идентификатор приложения */
            DA macAttr = createDA("String", "MAC", "set_mac", "Сетевой адрес", "01:0C:CD:04:00:00");
            ds.getDataAttributes().add(macAttr);

            /* Добавление атрибута: Идентификатор приложения */
            DA goCbArr = createDA("String", "gocbRef", "set_gocbRef", "Ссылка на GOOSE Control block", "");
            ds.getDataAttributes().add(goCbArr);

            /* Добавление атрибута: Идентификатор приложения */
            DA goID = createDA("String", "goID", "set_goID", "Идентификатор GOOSE", "");
            ds.getDataAttributes().add(goID);

            /* Извлечение Объектов и Атрибутов */
            for(TDataSet tDataSet:tDataSetList){
                if(datSetName.equals("unknown")) break;
                if(datSetName.equals(tDataSet.getName())){

                    /* Functionally Constrained Data Attributes // Functionally Constrained ControlBlock */
                    for(Object object:tDataSet.getFCDAOrFCCB()){
                        if(object.getClass()==TFCDA.class){
                            TFCDA tfcda = ((TFCDA) object);

                            if(tfcda.getDaName()!=null && tfcda.getDoName()!=null){
                                String lnClass = !tfcda.getLnClass().isEmpty() ? tfcda.getLnClass().get(0).toLowerCase() : "err"; // Узел (класс) в котором нужные атрибуты
                                String inst = tfcda.getLnInst();                                                                  // Номер экземпляра узла
                                String type = lnClass + "_" + inst;                                                               // Класс в котором нужные атрибуты
                                String name = lnClass + "_" + inst + "/" + tfcda.getDoName() + "/" + tfcda.getDaName();           // Путь до атрибута
                                DA dataAttribute = createDA(type, name, name, "Объект данных","");
                                ds.getDataAttributes().add(dataAttribute);
                            }
                            else GUI.writeErrMessage(String.format("FCDA %s не имеет dataObject/dataAttribute", tfcda));
                        }
                        else if(object.getClass()==TFCCB.class){ System.err.println("TFCCB is exists"); }
                    }

                    /* Датасет найдет, объекты и атрибуты извлечены */
                    ld.getDataSets().add(ds); break;
                }
            }
        }

        /* Извлечение входящих гусей */
        if(!inputGooseDS.isEmpty()){
            DS ds = new DS();
            ds.setType(DSType.GOOSE_IN.toString());
            ds.setName("GOOSE_IN");

            /* Добавление атрибута: Имя datSet */
            DA dsNameAttr = createDA("String", "datSetName","set_datSetName", "Название datSet", "");
            ds.getDataAttributes().add(dsNameAttr);

            /* Добавление атрибута: Идентификатор приложения */
            DA appIdAttr = createDA("String", "appID", "set_appID", "Идентификатор приложения", "");
            ds.getDataAttributes().add(appIdAttr);

            /* Добавление атрибута: Идентификатор приложения */
            DA macAttr = createDA("String", "MAC", "set_mac", "Сетевой адрес", "01:0C:CD:04:00:00");
            ds.getDataAttributes().add(macAttr);

            /* Добавление атрибута: Идентификатор приложения */
            DA goCbArr = createDA("String", "gocbRef", "set_gocbRef", "Ссылка на GOOSE Control block", "");
            ds.getDataAttributes().add(goCbArr);

            /* Добавление атрибута: Идентификатор приложения */
            DA goID = createDA("String", "goID", "set_goID", "Идентификатор GOOSE", "");
            ds.getDataAttributes().add(goID);

            /* Наполнение атрибутами */
            for(TExtRef tExtRef:inputGooseDS){
                if(tExtRef.getDoName()!=null && tExtRef.getDaName()!=null){
                    String lnClass = !tExtRef.getLnClass().isEmpty() ? tExtRef.getLnClass().get(0).toLowerCase() : "err"; // Узел (класс) в котором нужные атрибуты
                    String inst = tExtRef.getLnInst();                                                                    // Номер экземпляра узла
                    String type = lnClass + "_" + inst;                                                                   // Класс в котором нужные атрибуты
                    String name = lnClass + "_" + inst + "/" + tExtRef.getDoName() + "/" + tExtRef.getDaName();           // Путь до атрибута
                    DA dataAttribute = createDA(type, name, name, "Объект данных","");
                    ds.getDataAttributes().add(dataAttribute);
                } else GUI.writeErrMessage(String.format("%s DataSet, Data Object:  %s - не имеет объектов", DSType.GOOSE_IN.toString(), tExtRef));
            }
            ld.getDataSets().add(ds);
        }


        /* Извлечение исходящих отчетов (MMS) */
        for(TReportControl reportCtrlBlock:outputMMSList){
            DS ds = new DS();
            ds.setType(DSType.MMS_OUT.toString());

//            if(reportCtrlBlock.getName()==null) {
//                GUI.writeErrMessage(String.format("%s DataSet:  %s - is not contain name", DSType.MMS_OUT.toString(), reportCtrlBlock));
//            }
//            if(reportCtrlBlock.getDatSet()==null) {
//                GUI.writeErrMessage(String.format("%s DataSet:  %s - is not contain DatSet name", DSType.MMS_OUT.toString(), reportCtrlBlock));
//            }

//            ds.setName(reportCtrlBlock.getName()!=null ? reportCtrlBlock.getName() : "unknown");
//            ds.setDatSetName(reportCtrlBlock.getDatSet()!=null ? reportCtrlBlock.getDatSet() : "unknown");
//            ds.setDescription(reportCtrlBlock.getDesc()!=null ? reportCtrlBlock.getDesc() : "unknown");
//            ds.setID(reportCtrlBlock.getRptID()!=null ? reportCtrlBlock.getRptID() : "unknown");

//            for(TDataSet tDataSet:tDataSetList){
//                if(ds.getDatSetName().equals(tDataSet.getName())){
//
//                    ObservableList<DO> dataObjectList = FXCollections.observableArrayList();
//                    for(Object obj:tDataSet.getFCDAOrFCCB()){
//                        if(obj.getClass()==TFCDA.class){
//                            TFCDA tfcda = ((TFCDA) obj);
//                            DO dataObject = new DO();
//                            if(tfcda.getDoName()==null) GUI.writeErrMessage(String.format("%s DataSet, Data Object:  %s - is not contain name", DSType.MMS_OUT.toString(), StringOf(tfcda)));
//                            if(tfcda.getDaName()==null) GUI.writeErrMessage(String.format("%s DataSet, Data Object:  %s - is not contain attribute name", DSType.MMS_OUT.toString(), StringOf(tfcda)));
//                            dataObject.setType(tfcda.getDoName()!=null ? tfcda.getDoName(): "unknown");
//                            dataObject.setName(tfcda.getDaName()!=null ? tfcda.getDaName(): "unknown");
//                            dataObjectList.add(dataObject);
//                        }
//                    }
//
//                    for(DO doc:dataObjectList) ds.getDataObjects().add(doc);
//                    ld.getDataSets().add(ds);
//
//                    break;
//                }
//            }

        }

        return ld;
    }

    /**
     * Извлечение логического узла
     * @param source - TLN
     * @return - LN
     */
    private static LN extractLN(TLN source){
        LN logicalNode = new LN();

        /** Тип класса */
        logicalNode.setType((source.getLnClass().size()>0) ? source.getLnClass().get(0) : "unknown");
        /** Номер экземпляра */
        logicalNode.setInstance(source.getInst());
        /** Имя экземпляра */
        logicalNode.setName((logicalNode.getType() + "_" + logicalNode.getInstance()).toLowerCase());
        logicalNode.setCppName(logicalNode.getName());
        /** Описание узла */
        logicalNode.setDescription(source.getDesc()!=null ? source.getDesc() : "unknown");

        if(source.getLnClass().isEmpty()) GUI.writeErrMessage(String.format("LN %s не имеет класса", source));
        return logicalNode;
    }

    /** Создать атрибут */
    private static DA createDA(String type, String name, String cppName, String desc, String value){
        DA da = new DA(); da.setType(type); da.setName(name); da.setCppName(cppName); da.setDescription(desc); da.setValue(value);
        return da;
    }

    /** Создать объект */
    private static DO createDO(String type, String name, String desc){
        DO dataObject = new DO(); dataObject.setType(type); dataObject.setName(name); dataObject.setDescription(desc);
        return dataObject;
    }
}
