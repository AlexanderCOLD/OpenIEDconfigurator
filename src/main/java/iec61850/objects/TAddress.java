
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tAddress", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "p"
})
public class TAddress {

    @XmlElement(name = "P", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected List<TP> p;

    public List<TP> getP() {
        if (p == null) {
            p = new ArrayList<TP>();
        }
        return this.p;
    }

}
