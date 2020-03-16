
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tSubEquipment", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TSubEquipment
    extends TPowerSystemResource
{

    @XmlAttribute(name = "phase")
    protected TPhaseEnum phase;
    @XmlAttribute(name = "virtual")
    protected Boolean virtual;

    public TPhaseEnum getPhase() {
        if (phase == null) {
            return TPhaseEnum.NONE;
        } else {
            return phase;
        }
    }

    public void setPhase(TPhaseEnum value) {
        this.phase = value;
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
