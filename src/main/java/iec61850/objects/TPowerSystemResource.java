
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tPowerSystemResource", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlSeeAlso({
    TSubFunction.class,
    TEquipmentContainer.class,
    TFunction.class,
    TEquipment.class,
    TTapChanger.class,
    TSubEquipment.class
})
public abstract class TPowerSystemResource
    extends TLNodeContainer
{


}
