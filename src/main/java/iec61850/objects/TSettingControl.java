
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tSettingControl", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TSettingControl
    extends TUnNaming
{

    @XmlAttribute(name = "numOfSGs", required = true)
    protected long numOfSGs;
    @XmlAttribute(name = "actSG")
    protected Long actSG;

    public long getNumOfSGs() {
        return numOfSGs;
    }

    public void setNumOfSGs(long value) {
        this.numOfSGs = value;
    }

    public long getActSG() {
        if (actSG == null) {
            return  1L;
        } else {
            return actSG;
        }
    }

    public void setActSG(Long value) {
        this.actSG = value;
    }

}
