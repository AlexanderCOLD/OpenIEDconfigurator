
package iec61850.objects;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEquipmentContainer", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "powerTransformer",
    "generalEquipment"
})
@XmlSeeAlso({
    TSubstation.class,
    TBay.class,
    TVoltageLevel.class
})
public abstract class TEquipmentContainer
    extends TPowerSystemResource
{

    @XmlElement(name = "PowerTransformer", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TPowerTransformer> powerTransformer;
    @XmlElement(name = "GeneralEquipment", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TGeneralEquipment> generalEquipment;

    public List<TPowerTransformer> getPowerTransformer() {
        if (powerTransformer == null) {
            powerTransformer = new ArrayList<TPowerTransformer>();
        }
        return this.powerTransformer;
    }

    public List<TGeneralEquipment> getGeneralEquipment() {
        if (generalEquipment == null) {
            generalEquipment = new ArrayList<TGeneralEquipment>();
        }
        return this.generalEquipment;
    }

}
