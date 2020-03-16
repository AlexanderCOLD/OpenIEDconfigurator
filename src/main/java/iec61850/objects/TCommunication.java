
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tCommunication", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "subNetwork"
})
public class TCommunication
    extends TUnNaming
{

    @XmlElement(name = "SubNetwork", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected List<TSubNetwork> subNetwork;

    public List<TSubNetwork> getSubNetwork() {
        if (subNetwork == null) {
            subNetwork = new ArrayList<TSubNetwork>();
        }
        return this.subNetwork;
    }

}
