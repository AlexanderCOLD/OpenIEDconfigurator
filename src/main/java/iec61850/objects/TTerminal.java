
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tTerminal", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TTerminal
    extends TUnNaming
{

    @XmlAttribute(name = "name")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String name;
    @XmlAttribute(name = "connectivityNode", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String connectivityNode;
    @XmlAttribute(name = "substationName", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String substationName;
    @XmlAttribute(name = "voltageLevelName", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String voltageLevelName;
    @XmlAttribute(name = "bayName", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String bayName;
    @XmlAttribute(name = "cNodeName", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String cNodeName;

    public String getName() {
        if (name == null) {
            return "";
        } else {
            return name;
        }
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getConnectivityNode() {
        return connectivityNode;
    }

    public void setConnectivityNode(String value) {
        this.connectivityNode = value;
    }

    public String getSubstationName() {
        return substationName;
    }

    public void setSubstationName(String value) {
        this.substationName = value;
    }

    public String getVoltageLevelName() {
        return voltageLevelName;
    }

    public void setVoltageLevelName(String value) {
        this.voltageLevelName = value;
    }

    public String getBayName() {
        return bayName;
    }

    public void setBayName(String value) {
        this.bayName = value;
    }

    public String getCNodeName() {
        return cNodeName;
    }

    public void setCNodeName(String value) {
        this.cNodeName = value;
    }

}
