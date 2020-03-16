
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tServices", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {

})
public class TServices {

    @XmlElement(name = "DynAssociation", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServiceYesNo dynAssociation;
    @XmlElement(name = "SettingGroups", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServices.SettingGroups settingGroups;
    @XmlElement(name = "GetDirectory", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServiceYesNo getDirectory;
    @XmlElement(name = "GetDataObjectDefinition", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServiceYesNo getDataObjectDefinition;
    @XmlElement(name = "DataObjectDirectory", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServiceYesNo dataObjectDirectory;
    @XmlElement(name = "GetDataSetValue", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServiceYesNo getDataSetValue;
    @XmlElement(name = "SetDataSetValue", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServiceYesNo setDataSetValue;
    @XmlElement(name = "DataSetDirectory", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServiceYesNo dataSetDirectory;
    @XmlElement(name = "ConfDataSet", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServiceWithMaxAndMaxAttributesAndModify confDataSet;
    @XmlElement(name = "DynDataSet", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServiceWithMaxAndMaxAttributes dynDataSet;
    @XmlElement(name = "ReadWrite", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServiceYesNo readWrite;
    @XmlElement(name = "TimerActivatedControl", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServiceYesNo timerActivatedControl;
    @XmlElement(name = "ConfReportControl", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServiceWithMax confReportControl;
    @XmlElement(name = "GetCBValues", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServiceYesNo getCBValues;
    @XmlElement(name = "ConfLogControl", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServiceWithMax confLogControl;
    @XmlElement(name = "ReportSettings", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TReportSettings reportSettings;
    @XmlElement(name = "LogSettings", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TLogSettings logSettings;
    @XmlElement(name = "GSESettings", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TGSESettings gseSettings;
    @XmlElement(name = "SMVSettings", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TSMVSettings smvSettings;
    @XmlElement(name = "GSEDir", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServiceYesNo gseDir;
    @XmlElement(name = "GOOSE", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServiceWithMax goose;
    @XmlElement(name = "GSSE", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServiceWithMax gsse;
    @XmlElement(name = "FileHandling", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServiceYesNo fileHandling;
    @XmlElement(name = "ConfLNs", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TConfLNs confLNs;
    @XmlElement(name = "ClientServices", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TClientServices clientServices;

    public TServiceYesNo getDynAssociation() {
        return dynAssociation;
    }

    public void setDynAssociation(TServiceYesNo value) {
        this.dynAssociation = value;
    }

    public TServices.SettingGroups getSettingGroups() {
        return settingGroups;
    }

    public void setSettingGroups(TServices.SettingGroups value) {
        this.settingGroups = value;
    }

    public TServiceYesNo getGetDirectory() {
        return getDirectory;
    }

    public void setGetDirectory(TServiceYesNo value) {
        this.getDirectory = value;
    }

    public TServiceYesNo getGetDataObjectDefinition() {
        return getDataObjectDefinition;
    }

    public void setGetDataObjectDefinition(TServiceYesNo value) {
        this.getDataObjectDefinition = value;
    }

    public TServiceYesNo getDataObjectDirectory() {
        return dataObjectDirectory;
    }

    public void setDataObjectDirectory(TServiceYesNo value) {
        this.dataObjectDirectory = value;
    }

    public TServiceYesNo getGetDataSetValue() {
        return getDataSetValue;
    }

    public void setGetDataSetValue(TServiceYesNo value) {
        this.getDataSetValue = value;
    }

    public TServiceYesNo getSetDataSetValue() {
        return setDataSetValue;
    }

    public void setSetDataSetValue(TServiceYesNo value) {
        this.setDataSetValue = value;
    }

    public TServiceYesNo getDataSetDirectory() {
        return dataSetDirectory;
    }

    public void setDataSetDirectory(TServiceYesNo value) {
        this.dataSetDirectory = value;
    }

    public TServiceWithMaxAndMaxAttributesAndModify getConfDataSet() {
        return confDataSet;
    }

    public void setConfDataSet(TServiceWithMaxAndMaxAttributesAndModify value) {
        this.confDataSet = value;
    }

    public TServiceWithMaxAndMaxAttributes getDynDataSet() {
        return dynDataSet;
    }

    public void setDynDataSet(TServiceWithMaxAndMaxAttributes value) {
        this.dynDataSet = value;
    }

    public TServiceYesNo getReadWrite() {
        return readWrite;
    }

    public void setReadWrite(TServiceYesNo value) {
        this.readWrite = value;
    }

    public TServiceYesNo getTimerActivatedControl() {
        return timerActivatedControl;
    }

    public void setTimerActivatedControl(TServiceYesNo value) {
        this.timerActivatedControl = value;
    }

    public TServiceWithMax getConfReportControl() {
        return confReportControl;
    }

    public void setConfReportControl(TServiceWithMax value) {
        this.confReportControl = value;
    }

    public TServiceYesNo getGetCBValues() {
        return getCBValues;
    }

    public void setGetCBValues(TServiceYesNo value) {
        this.getCBValues = value;
    }

    public TServiceWithMax getConfLogControl() {
        return confLogControl;
    }

    public void setConfLogControl(TServiceWithMax value) {
        this.confLogControl = value;
    }

    public TReportSettings getReportSettings() {
        return reportSettings;
    }

    public void setReportSettings(TReportSettings value) {
        this.reportSettings = value;
    }

    public TLogSettings getLogSettings() {
        return logSettings;
    }

    public void setLogSettings(TLogSettings value) {
        this.logSettings = value;
    }

    public TGSESettings getGSESettings() {
        return gseSettings;
    }

    public void setGSESettings(TGSESettings value) {
        this.gseSettings = value;
    }

    public TSMVSettings getSMVSettings() {
        return smvSettings;
    }

    public void setSMVSettings(TSMVSettings value) {
        this.smvSettings = value;
    }

    public TServiceYesNo getGSEDir() {
        return gseDir;
    }

    public void setGSEDir(TServiceYesNo value) {
        this.gseDir = value;
    }

    public TServiceWithMax getGOOSE() {
        return goose;
    }

    public void setGOOSE(TServiceWithMax value) {
        this.goose = value;
    }

    public TServiceWithMax getGSSE() {
        return gsse;
    }

    public void setGSSE(TServiceWithMax value) {
        this.gsse = value;
    }

    public TServiceYesNo getFileHandling() {
        return fileHandling;
    }

    public void setFileHandling(TServiceYesNo value) {
        this.fileHandling = value;
    }

    public TConfLNs getConfLNs() {
        return confLNs;
    }

    public void setConfLNs(TConfLNs value) {
        this.confLNs = value;
    }

    public TClientServices getClientServices() {
        return clientServices;
    }

    public void setClientServices(TClientServices value) {
        this.clientServices = value;
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {

    })
    public static class SettingGroups {

        @XmlElement(name = "SGEdit", namespace = "http://www.iec.ch/61850/2006/SCL")
        protected TServiceYesNo sgEdit;
        @XmlElement(name = "ConfSG", namespace = "http://www.iec.ch/61850/2006/SCL")
        protected TServiceYesNo confSG;

        public TServiceYesNo getSGEdit() {
            return sgEdit;
        }

        public void setSGEdit(TServiceYesNo value) {
            this.sgEdit = value;
        }

        public TServiceYesNo getConfSG() {
            return confSG;
        }

        public void setConfSG(TServiceYesNo value) {
            this.confSG = value;
        }

    }

}
