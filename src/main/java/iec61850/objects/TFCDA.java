
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tFCDA", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TFCDA {

    @XmlAttribute(name = "ldInst")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String ldInst;
    @XmlAttribute(name = "prefix")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String prefix;
    @XmlAttribute(name = "lnClass")
    protected List<String> lnClass;
    @XmlAttribute(name = "lnInst")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String lnInst;
    @XmlAttribute(name = "doName")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String doName;
    @XmlAttribute(name = "daName")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String daName;
    @XmlAttribute(name = "fc", required = true)
    protected TFCEnum fc;

    public String getLdInst() {
        return ldInst;
    }

    public void setLdInst(String value) {
        this.ldInst = value;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String value) {
        this.prefix = value;
    }

    public List<String> getLnClass() {
        if (lnClass == null) {
            lnClass = new ArrayList<String>();
        }
        return this.lnClass;
    }

    public String getLnInst() {
        return lnInst;
    }

    public void setLnInst(String value) {
        this.lnInst = value;
    }

    public String getDoName() {
        return doName;
    }

    public void setDoName(String value) {
        this.doName = value;
    }

    public String getDaName() {
        return daName;
    }

    public void setDaName(String value) {
        this.daName = value;
    }

    public TFCEnum getFc() {
        return fc;
    }

    public void setFc(TFCEnum value) {
        this.fc = value;
    }

}
