
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
@XmlType(name = "tLNode", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TLNode
    extends TUnNaming
{

    @XmlAttribute(name = "lnInst")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String lnInst;
    @XmlAttribute(name = "lnClass", required = true)
    protected List<String> lnClass;
    @XmlAttribute(name = "iedName")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String iedName;
    @XmlAttribute(name = "ldInst")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String ldInst;
    @XmlAttribute(name = "prefix")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String prefix;
    @XmlAttribute(name = "lnType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String lnType;

    public String getLnInst() {
        if (lnInst == null) {
            return "";
        } else {
            return lnInst;
        }
    }

    public void setLnInst(String value) {
        this.lnInst = value;
    }

    public List<String> getLnClass() {
        if (lnClass == null) {
            lnClass = new ArrayList<String>();
        }
        return this.lnClass;
    }

    public String getIedName() {
        if (iedName == null) {
            return "None";
        } else {
            return iedName;
        }
    }

    public void setIedName(String value) {
        this.iedName = value;
    }

    public String getLdInst() {
        if (ldInst == null) {
            return "";
        } else {
            return ldInst;
        }
    }

    public void setLdInst(String value) {
        this.ldInst = value;
    }

    public String getPrefix() {
        if (prefix == null) {
            return "";
        } else {
            return prefix;
        }
    }

    public void setPrefix(String value) {
        this.prefix = value;
    }

    public String getLnType() {
        return lnType;
    }

    public void setLnType(String value) {
        this.lnType = value;
    }

}
