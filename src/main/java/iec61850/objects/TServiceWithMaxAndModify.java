
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tServiceWithMaxAndModify", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TServiceWithMaxAndModify
    extends TServiceWithMax
{

    @XmlAttribute(name = "modify")
    protected Boolean modify;

    public boolean isModify() {
        if (modify == null) {
            return true;
        } else {
            return modify;
        }
    }

    public void setModify(Boolean value) {
        this.modify = value;
    }

}
