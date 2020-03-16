
package iec61850.objects;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tAbstractConductingEquipment", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "terminal",
    "subEquipment"
})
@XmlSeeAlso({
    TConductingEquipment.class,
    TTransformerWinding.class
})
public abstract class TAbstractConductingEquipment
    extends TEquipment
{

    @XmlElement(name = "Terminal", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TTerminal> terminal;
    @XmlElement(name = "SubEquipment", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TSubEquipment> subEquipment;

    public List<TTerminal> getTerminal() {
        if (terminal == null) {
            terminal = new ArrayList<TTerminal>();
        }
        return this.terminal;
    }

    public List<TSubEquipment> getSubEquipment() {
        if (subEquipment == null) {
            subEquipment = new ArrayList<TSubEquipment>();
        }
        return this.subEquipment;
    }

}
