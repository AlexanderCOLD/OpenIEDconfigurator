package iec61850;

import iec61850.objects.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description Извлечение LD, LN, DS ...
 */
public class IEDExtract {

    public static ArrayList<IED> extractIEDList(SCL object){
        ArrayList<IED> iedList = new ArrayList<>();
        for(TIED tied:object.getIED()){
            IED ied = extractIED(tied);
            if(ied!=null) iedList.add(ied);
        }
        return iedList;
    }

    /**
     * Извлечение IED
     */
    private static IED extractIED(TIED source){
        IED ied = new IED();
        ied.setName(source.getName());
        ied.setDescription(source.getDesc());

        List<TLDevice> tldList = null;
        if(source.getAccessPoint()!=null){
            if(source.getAccessPoint().size()>0){
                if(source.getAccessPoint().get(0).getServer()!=null){
                    tldList = source.getAccessPoint().get(0).getServer().getLDevice();
                }
            }
        }
        if(tldList!=null){
            for(TLDevice tld:tldList){
                LD ld = extractLD(tld);
                if(ld!=null) ied.getLdList().add(ld);
            }
        }
        return ied;
    }

    /**
     * Извлечение LD
     */
    private static LD extractLD(TLDevice source){
        LD ld = new LD();
        ld.setName(source.getInst());
        ld.setDescription(source.getDesc());

        List<TGSEControl> gseList = source.getLN0().getGSEControl();
        List<TDataSet> dsList = source.getLN0().getDataSet();

        if(gseList!=null && dsList!=null){
            ArrayList<DataSet> ds = extractGOOSEOutputDataSets(gseList, dsList);
            ld.setGooseOutputDataSets(ds);
        }

        if(source.getLN0().getInputs()!=null){
            List<TExtRef> list = source.getLN0().getInputs().getExtRef();
            ArrayList<DataSet> ds = extractGOOSEInputDataSets(list);
            if(ds!=null) ld.setGooseInputDataSets(ds);
        }

        List<TLN> tlnList = source.getLN();
        if(tlnList!=null){
            for(TLN tln:source.getLN()){
                LN ln = extractLN(tln);
                if(ln!=null) ld.getLnList().add(ln);
            }
        }



        return ld;
    }

    /**
     * Изслечение Goose датасетов
     * @param tgseControls - датасеты относящиеся только с данному LD
     * @param tDataSets - все датасеты
     * @return
     */
    private static ArrayList<DataSet> extractGOOSEOutputDataSets(List<TGSEControl> tgseControls, List<TDataSet> tDataSets){
        ArrayList<DataSet> dataSetList = new ArrayList<>();
        for(TGSEControl tgc:tgseControls){
            DataSet dataSet = new DataSet();
            dataSet.setName(tgc.getName());
            dataSet.setDescription(tgc.getDesc());
            dataSet.setType(DSType.gooseOut);
            dataSet.setAppId(tgc.getAppID());
            if(tgc.getIEDName()!=null) if(tgc.getIEDName().size()>0) dataSet.setIedName(tgc.getIEDName().get(0));
            dataSet.setDsName(tgc.getDatSet());
            for(TDataSet tds:tDataSets){
                if(dataSet.getDsName().equals(tds.getName())){
                    ArrayList<DO> doList = extractDOList(tds);
                    if(doList!=null) {
                        dataSet.setDoList(doList);
                        dataSetList.add(dataSet);
                    }
                    break;
                }
            }
        }

        return dataSetList;
    }

    private static ArrayList<DataSet> extractGOOSEInputDataSets(List<TExtRef> source){
        ArrayList<DataSet> dsList = new ArrayList<>();
        DataSet ds = new DataSet();
        dsList.add(ds);
        ds.setName(source.get(0).getIedName());

        for(TExtRef tdo:source){
            DO dobject = new DO();
            dobject.setName(tdo.getDoName());
            dobject.setDescription(tdo.getLdInst());
            if(tdo.getLnClass()!=null) dobject.setType(tdo.getLnClass().get(0));
            ds.getDoList().add(dobject);
        }
        return dsList;
    }


    /**
     * Извлечение листа объектов
     * @param source
     * @return
     */
    private static ArrayList<DO> extractDOList(TDataSet source){
        ArrayList<DO> doList = new ArrayList<>();
        for(Object obj:source.getFCDAOrFCCB()){
            if(obj.getClass()==TFCDA.class){
                TFCDA src = ((TFCDA)obj);
                DO dobject = new DO();
                dobject.setName(src.getDoName());
                dobject.setDescription(src.getLdInst());
                if(src.getLnClass()!=null){ dobject.setType(src.getLnClass().get(0)); }
                doList.add(dobject);
            }
        }

        return doList;
    }

    /**
     * Извлечение LN
     */
    private static LN extractLN(TLN source){
        LN target = new LN();
        if(source.getLnClass()!=null) target.setName(source.getLnClass().get(0));
        if(source.getLnType()!=null) target.setName(source.getLnType());
        target.setDescription(source.getDesc());
        target.setPrefix(source.getPrefix());
        return target;
    }


}
