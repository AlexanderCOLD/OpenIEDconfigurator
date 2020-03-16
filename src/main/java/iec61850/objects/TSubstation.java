
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tSubstation", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "voltageLevel",
    "function"
})
public class TSubstation
    extends TEquipmentContainer
{

    @XmlElement(name = "VoltageLevel", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected List<TVoltageLevel> voltageLevel;
    @XmlElement(name = "Function", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TFunction> function;

    public List<TVoltageLevel> getVoltageLevel() {
        if (voltageLevel == null) {
            voltageLevel = new ArrayList<TVoltageLevel>();
        }
        return this.voltageLevel;
    }

    public List<TFunction> getFunction() {
        if (function == null) {
            function = new ArrayList<TFunction>();
        }
        return this.function;
    }

}
