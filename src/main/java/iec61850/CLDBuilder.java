package iec61850;

import application.GUI;
import iec61850.objects.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description Извлечение необходимых данных из CID (IED, LD, LN, DS, DO)
 */
public class CLDBuilder {

    /**
     * Извлекает IED лист из SCL
     * @param scl - Искомый SCL
     * @return - CLD
     */
    public static CLD buildCLD(SCL scl){
        CLD cld = new CLD();
        if(scl.getIED()!=null) for(TIED ied:scl.getIED()) Optional.ofNullable(extractIED(ied)).ifPresent(cld.getIedList()::add);
        return cld;
    }

    /**
     * Извлекает IED
     * @param source - TIED (SCL)
     * @return - IED
     */
    private static IED extractIED(TIED source){
        IED ied = new IED();
        if(source.getName()==null) { GUI.writeErrMessage("IED:  "+ ied + "- is not contain name"); }
        ied.setName(source.getName()!=null ? source.getName() : "unknown");
        ied.setDescription(source.getDesc()!=null ? source.getDesc() : "unknown");

        /* Если существует Access Point, если в нем есть Server, если в сервере есть LD's, то извлекаем их */
        try {
            List<TLDevice> logicalDeviceList = source.getAccessPoint().get(0).getServer().getLDevice();
            for(TLDevice tld:logicalDeviceList) Optional.ofNullable(extractLD(tld)).ifPresent(ied.getLogicalDeviceList()::add);
        }
        catch (Exception e) { GUI.writeErrMessage(String.format("IED: %s is not contain logical devices", StringOf(source))); }

        return ied;
    }

    /**
     * Извлекает LD
     * @param source - TLDevice
     * @return - LD
     */
    private static LD extractLD(TLDevice source){
        LD ld = new LD();
        if(source.getLdName()==null) { GUI.writeErrMessage("LD:  "+ StringOf(source) + "- is not contain name"); }
        ld.setName(source.getLdName()!=null ? source.getLdName() : "unknown");
        ld.setDescription(source.getDesc()!=null ? source.getDesc(): "unknown");

        /* Извлекаем логические узлы если есть */
        List<TLN> tlnList = source.getLN();
        if(tlnList!=null) for(TLN tln:source.getLN()) Optional.ofNullable(extractLN(tln)).ifPresent(ld.getLogicalNodeList()::add);

        /* Извлекаем датасеты */
        List<TDataSet> tDataSetList = source.getLN0().getDataSet(); // Общий лист со всеми исходящими датасетами (GOOSE и MMS)
        List<TGSEControl> outputGooseList = source.getLN0().getGSEControl();     // Описание исходящих гусей (содержатся в dataSetList)
        List<TReportControl> outputMMSList = source.getLN0().getReportControl(); // Описание исходящих MMS (содержатся в dataSetList)

        /* Извлечение исходящих гусей */
        if(outputGooseList!=null && tDataSetList!=null){

            for(TGSEControl gseCtrlBlock:outputGooseList){
                DS ds = new DS();
                if(gseCtrlBlock.getName()==null) GUI.writeErrMessage(String.format("%s DataSet: GSEControlBlock  %s - is not contain name", DSType.GOOSE_OUT.toString(), StringOf(gseCtrlBlock)));
                if(gseCtrlBlock.getDatSet()==null) GUI.writeErrMessage(String.format("%s DataSet: GSEControlBlock  %s - is not contain DatSet name", DSType.GOOSE_OUT.toString(), StringOf(gseCtrlBlock)));
                ds.setType(DSType.GOOSE_OUT.toString());
                ds.setName(gseCtrlBlock.getName()!=null ? gseCtrlBlock.getName() : "unknown");
                ds.setDatSetName(gseCtrlBlock.getDatSet()!=null ? gseCtrlBlock.getDatSet() : "unknown");
                ds.setDescription(gseCtrlBlock.getDesc()!=null ? gseCtrlBlock.getDesc() : "unknown");
                ds.setID(gseCtrlBlock.getAppID()!=null ? gseCtrlBlock.getAppID() : "unknown");

                for(TDataSet tDataSet:tDataSetList){
                    if(ds.getDatSetName().equals(tDataSet.getName())){

                        ObservableList<DO> dataObjectList = FXCollections.observableArrayList();
                        for(Object obj:tDataSet.getFCDAOrFCCB()){
                            if(obj.getClass()==TFCDA.class){
                                TFCDA tfcda = ((TFCDA) obj);
                                DO dataObject = new DO();
                                if(tfcda.getDoName()==null) GUI.writeErrMessage(String.format("%s DataSet, Data Object:  %s - is not contain name", DSType.GOOSE_OUT.toString(), StringOf(tfcda)));
                                if(tfcda.getDaName()==null) GUI.writeErrMessage(String.format("%s DataSet, Data Object:  %s - is not contain attribute name", DSType.GOOSE_OUT.toString(), StringOf(tfcda)));
                                dataObject.setType(tfcda.getDoName()!=null ? tfcda.getDoName(): "unknown");
                                dataObject.setName(tfcda.getDaName()!=null ? tfcda.getDaName(): "unknown");
                                dataObjectList.add(dataObject);
                            }
                        }

                        for(DO doc:dataObjectList) ds.getDataObjects().add(doc);
                        ld.getDataSets().add(ds);

                        break;
                    }
                }
            }
        }

        /* Извлечение входящих гусей */
        if(source.getLN0().getInputs()!=null && source.getLN0().getInputs().getExtRef()!=null){
            List<TExtRef> extRefList = source.getLN0().getInputs().getExtRef();

            DS dataSet = new DS();
            dataSet.setType(DSType.GOOSE_IN.toString());
            dataSet.setName("GOOSE_IN");

            for(TExtRef tExtRef:extRefList){
                DO dataObject = new DO();
                if(tExtRef.getDoName()==null) GUI.writeErrMessage(String.format("%s DataSet, Data Object:  %s - is not contain name", DSType.GOOSE_IN.toString(), StringOf(tExtRef)));
                if(tExtRef.getDaName()==null) GUI.writeErrMessage(String.format("%s DataSet, Data Object:  %s - is not contain attribute name", DSType.GOOSE_IN.toString(), StringOf(tExtRef)));
                dataObject.setType(tExtRef.getDoName()!=null ? tExtRef.getDoName(): "unknown");
                dataObject.setName(tExtRef.getDaName()!=null ? tExtRef.getDaName(): "unknown");
                dataSet.getDataObjects().add(dataObject);
            }

            ld.getDataSets().add(dataSet);
        }

        /* Извлечение исходящих отчетов (MMS) */
        if(outputMMSList!=null && tDataSetList!=null){

            for(TReportControl reportCtrlBlock:outputMMSList){
                DS ds = new DS();
                ds.setType(DSType.MMS_OUT.toString());

                if(reportCtrlBlock.getName()==null) GUI.writeErrMessage(String.format("%s DataSet:  %s - is not contain name", DSType.MMS_OUT.toString(), StringOf(reportCtrlBlock)));
                if(reportCtrlBlock.getDatSet()==null) GUI.writeErrMessage(String.format("%s DataSet:  %s - is not contain DatSet name", DSType.MMS_OUT.toString(), StringOf(reportCtrlBlock)));
                ds.setName(reportCtrlBlock.getName()!=null ? reportCtrlBlock.getName() : "unknown");
                ds.setDatSetName(reportCtrlBlock.getDatSet()!=null ? reportCtrlBlock.getDatSet() : "unknown");
                ds.setDescription(reportCtrlBlock.getDesc()!=null ? reportCtrlBlock.getDesc() : "unknown");
                ds.setID(reportCtrlBlock.getRptID()!=null ? reportCtrlBlock.getRptID() : "unknown");

                for(TDataSet tDataSet:tDataSetList){
                    if(ds.getDatSetName().equals(tDataSet.getName())){

                        ObservableList<DO> dataObjectList = FXCollections.observableArrayList();
                        for(Object obj:tDataSet.getFCDAOrFCCB()){
                            if(obj.getClass()==TFCDA.class){
                                TFCDA tfcda = ((TFCDA) obj);
                                DO dataObject = new DO();
                                if(tfcda.getDoName()==null) GUI.writeErrMessage(String.format("%s DataSet, Data Object:  %s - is not contain name", DSType.MMS_OUT.toString(), StringOf(tfcda)));
                                if(tfcda.getDaName()==null) GUI.writeErrMessage(String.format("%s DataSet, Data Object:  %s - is not contain attribute name", DSType.MMS_OUT.toString(), StringOf(tfcda)));
                                dataObject.setType(tfcda.getDoName()!=null ? tfcda.getDoName(): "unknown");
                                dataObject.setName(tfcda.getDaName()!=null ? tfcda.getDaName(): "unknown");
                                dataObjectList.add(dataObject);
                            }
                        }

                        for(DO doc:dataObjectList) ds.getDataObjects().add(doc);
                        ld.getDataSets().add(ds);

                        break;
                    }
                }
            }
        }
        return ld;
    }

    /**
     * Извлечение логического узла
     * @param source - TLN
     * @return - LN
     */
    private static LN extractLN(TLN source){
        LN ln = new LN();
        if(source.getLnType()==null) { GUI.writeErrMessage("LN:  "+ StringOf(source) + "- is not contain type"); }
        ln.setType((source.getLnClass()!=null && source.getLnClass().size()>0 && source.getLnClass().get(0)!=null) ? source.getLnClass().get(0) : "unknown");
        ln.setName(source.getLnType()!=null ? source.getLnType() : ln.getType());
        ln.setDescription(source.getDesc()!=null ? source.getDesc() : "unknown");
        return ln;
    }

    private static String StringOf(Object object) {
        String result = "{";
        for (Field field : object.getClass().getDeclaredFields()) { field.setAccessible(true); try { Object value = field.get(object); if(value!=null) result += String.format(" %s: [%s] ", field.getName(), value); } catch (IllegalAccessException ignored) { } }
        result += "}";
        return result;
    }

}
