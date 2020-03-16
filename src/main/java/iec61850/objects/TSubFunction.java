
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tSubFunction", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "generalEquipment"
})
public class TSubFunction
    extends TPowerSystemResource
{

    @XmlElement(name = "GeneralEquipment", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TGeneralEquipment> generalEquipment;

    public List<TGeneralEquipment> getGeneralEquipment() {
        if (generalEquipment == null) {
            generalEquipment = new ArrayList<TGeneralEquipment>();
        }
        return this.generalEquipment;
    }

}
