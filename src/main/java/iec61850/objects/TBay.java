
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tBay", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "conductingEquipment",
    "connectivityNode"
})
public class TBay
    extends TEquipmentContainer
{

    @XmlElement(name = "ConductingEquipment", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TConductingEquipment> conductingEquipment;
    @XmlElement(name = "ConnectivityNode", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TConnectivityNode> connectivityNode;

    public List<TConductingEquipment> getConductingEquipment() {
        if (conductingEquipment == null) {
            conductingEquipment = new ArrayList<TConductingEquipment>();
        }
        return this.conductingEquipment;
    }

    public List<TConnectivityNode> getConnectivityNode() {
        if (connectivityNode == null) {
            connectivityNode = new ArrayList<TConnectivityNode>();
        }
        return this.connectivityNode;
    }

}
