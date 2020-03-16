
package iec61850.objects;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tLNodeContainer", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "lNode"
})
@XmlSeeAlso({
    TConnectivityNode.class,
    TPowerSystemResource.class
})
public abstract class TLNodeContainer
    extends TNaming
{

    @XmlElement(name = "LNode", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TLNode> lNode;

    public List<TLNode> getLNode() {
        if (lNode == null) {
            lNode = new ArrayList<TLNode>();
        }
        return this.lNode;
    }

}
