
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDataTypeTemplates", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "lNodeType",
    "doType",
    "daType",
    "enumType"
})
public class TDataTypeTemplates {

    @XmlElement(name = "LNodeType", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected List<TLNodeType> lNodeType;
    @XmlElement(name = "DOType", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected List<TDOType> doType;
    @XmlElement(name = "DAType", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TDAType> daType;
    @XmlElement(name = "EnumType", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TEnumType> enumType;

    public List<TLNodeType> getLNodeType() {
        if (lNodeType == null) {
            lNodeType = new ArrayList<TLNodeType>();
        }
        return this.lNodeType;
    }

    public List<TDOType> getDOType() {
        if (doType == null) {
            doType = new ArrayList<TDOType>();
        }
        return this.doType;
    }

    public List<TDAType> getDAType() {
        if (daType == null) {
            daType = new ArrayList<TDAType>();
        }
        return this.daType;
    }

    public List<TEnumType> getEnumType() {
        if (enumType == null) {
            enumType = new ArrayList<TEnumType>();
        }
        return this.enumType;
    }

}
