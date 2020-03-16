
package iec61850.objects;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEquipment", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlSeeAlso({
    TPowerTransformer.class,
    TGeneralEquipment.class,
    TAbstractConductingEquipment.class
})
public abstract class TEquipment
    extends TPowerSystemResource
{

    @XmlAttribute(name = "virtual")
    protected Boolean virtual;

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
