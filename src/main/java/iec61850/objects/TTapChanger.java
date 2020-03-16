
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tTapChanger", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TTapChanger
    extends TPowerSystemResource
{

    @XmlAttribute(name = "type", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String type;
    @XmlAttribute(name = "virtual")
    protected Boolean virtual;

    public String getType() {
        if (type == null) {
            return "LTC";
        } else {
            return type;
        }
    }

    public void setType(String value) {
        this.type = value;
    }

    public boolean isVirtual() {
        if (virtual == null) {
            return false;
        } else {
            return virtual;
        }
    }

    public void setVirtual(Boolean value) {
        this.virtual = value;
    }

}
