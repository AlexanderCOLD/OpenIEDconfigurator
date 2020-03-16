
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
@XmlType(name = "tExtRef", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TExtRef {

    @XmlAttribute(name = "daName")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String daName;
    @XmlAttribute(name = "intAddr")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String intAddr;
    @XmlAttribute(name = "doName", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String doName;
    @XmlAttribute(name = "prefix")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String prefix;
    @XmlAttribute(name = "lnClass", required = true)
    protected List<String> lnClass;
    @XmlAttribute(name = "lnInst", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String lnInst;
    @XmlAttribute(name = "iedName", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String iedName;
    @XmlAttribute(name = "ldInst", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String ldInst;

    public String getDaName() {
        return daName;
    }

    public void setDaName(String value) {
        this.daName = value;
    }

    public String getIntAddr() {
        return intAddr;
    }

    public void setIntAddr(String value) {
        this.intAddr = value;
    }

    public String getDoName() {
        return doName;
    }

    public void setDoName(String value) {
        this.doName = value;
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

    public String getIedName() {
        return iedName;
    }

    public void setIedName(String value) {
        this.iedName = value;
    }

    public String getLdInst() {
        return ldInst;
    }

    public void setLdInst(String value) {
        this.ldInst = value;
    }

}
