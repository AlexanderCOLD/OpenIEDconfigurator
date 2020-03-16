
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
@XmlType(name = "tAssociation", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TAssociation {

    @XmlAttribute(name = "kind", required = true)
    protected TAssociationKindEnum kind;
    @XmlAttribute(name = "associationID")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String associationID;
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

    public TAssociationKindEnum getKind() {
        return kind;
    }

    public void setKind(TAssociationKindEnum value) {
        this.kind = value;
    }

    public String getAssociationID() {
        return associationID;
    }

    public void setAssociationID(String value) {
        this.associationID = value;
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
