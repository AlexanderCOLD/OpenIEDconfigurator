
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tVoltageLevel", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "voltage",
    "bay"
})
public class TVoltageLevel
    extends TEquipmentContainer
{

    @XmlElement(name = "Voltage", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TVoltage voltage;
    @XmlElement(name = "Bay", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected List<TBay> bay;

    public TVoltage getVoltage() {
        return voltage;
    }

    public void setVoltage(TVoltage value) {
        this.voltage = value;
    }

    public List<TBay> getBay() {
        if (bay == null) {
            bay = new ArrayList<TBay>();
        }
        return this.bay;
    }

}
