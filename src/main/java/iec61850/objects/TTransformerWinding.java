
package iec61850.objects;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tTransformerWinding", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "tapChanger"
})
public class TTransformerWinding
    extends TAbstractConductingEquipment
{

    @XmlElement(name = "TapChanger", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TTapChanger tapChanger;
    @XmlAttribute(name = "type", required = true)
    protected TTransformerWindingEnum type;

    public TTapChanger getTapChanger() {
        return tapChanger;
    }

    public void setTapChanger(TTapChanger value) {
        this.tapChanger = value;
    }

    public TTransformerWindingEnum getType() {
        if (type == null) {
            return TTransformerWindingEnum.PTW;
        } else {
            return type;
        }
    }

    public void setType(TTransformerWindingEnum value) {
        this.type = value;
    }

}
