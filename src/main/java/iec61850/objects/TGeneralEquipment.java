
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tGeneralEquipment", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TGeneralEquipment
    extends TEquipment
{

    @XmlAttribute(name = "type", required = true)
    protected String type;

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }

}
