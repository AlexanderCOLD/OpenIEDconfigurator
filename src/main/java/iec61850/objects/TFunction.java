
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tFunction", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "subFunction",
    "generalEquipment"
})
public class TFunction
    extends TPowerSystemResource
{

    @XmlElement(name = "SubFunction", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TSubFunction> subFunction;
    @XmlElement(name = "GeneralEquipment", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TGeneralEquipment> generalEquipment;

    public List<TSubFunction> getSubFunction() {
        if (subFunction == null) {
            subFunction = new ArrayList<TSubFunction>();
        }
        return this.subFunction;
    }

    public List<TGeneralEquipment> getGeneralEquipment() {
        if (generalEquipment == null) {
            generalEquipment = new ArrayList<TGeneralEquipment>();
        }
        return this.generalEquipment;
    }

}
