
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tLN0", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "gseControl",
    "sampledValueControl",
    "settingControl",
    "sclControl",
    "log"
})
@XmlSeeAlso({
    LN0 .class
})
public class TLN0
    extends TAnyLN
{

    @XmlElement(name = "GSEControl", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TGSEControl> gseControl;
    @XmlElement(name = "SampledValueControl", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TSampledValueControl> sampledValueControl;
    @XmlElement(name = "SettingControl", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TSettingControl settingControl;
    @XmlElement(name = "SCLControl", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TSCLControl sclControl;
    @XmlElement(name = "Log", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TLog log;
    @XmlAttribute(name = "lnClass", required = true)
    protected List<String> lnClass;
    @XmlAttribute(name = "inst", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String inst;

    public List<TGSEControl> getGSEControl() {
        if (gseControl == null) {
            gseControl = new ArrayList<TGSEControl>();
        }
        return this.gseControl;
    }

    public List<TSampledValueControl> getSampledValueControl() {
        if (sampledValueControl == null) {
            sampledValueControl = new ArrayList<TSampledValueControl>();
        }
        return this.sampledValueControl;
    }

    public TSettingControl getSettingControl() {
        return settingControl;
    }

    public void setSettingControl(TSettingControl value) {
        this.settingControl = value;
    }

    public TSCLControl getSCLControl() {
        return sclControl;
    }

    public void setSCLControl(TSCLControl value) {
        this.sclControl = value;
    }

    public TLog getLog() {
        return log;
    }

    public void setLog(TLog value) {
        this.log = value;
    }

    public List<String> getLnClass() {
        if (lnClass == null) {
            lnClass = new ArrayList<String>();
        }
        return this.lnClass;
    }

    public String getInst() {
        if (inst == null) {
            return "";
        } else {
            return inst;
        }
    }

    public void setInst(String value) {
        this.inst = value;
    }

}
