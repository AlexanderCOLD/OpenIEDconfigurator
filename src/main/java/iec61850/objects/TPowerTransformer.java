
package iec61850.objects;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tPowerTransformer", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "transformerWinding"
})
public class TPowerTransformer
    extends TEquipment
{

    @XmlElement(name = "TransformerWinding", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected List<TTransformerWinding> transformerWinding;
    @XmlAttribute(name = "type", required = true)
    protected TPowerTransformerEnum type;

    public List<TTransformerWinding> getTransformerWinding() {
        if (transformerWinding == null) {
            transformerWinding = new ArrayList<TTransformerWinding>();
        }
        return this.transformerWinding;
    }

    public TPowerTransformerEnum getType() {
        if (type == null) {
            return TPowerTransformerEnum.PTR;
        } else {
            return type;
        }
    }

    public void setType(TPowerTransformerEnum value) {
        this.type = value;
    }

}
