
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tPhysConn", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "p"
})
public class TPhysConn {

    @XmlElement(name = "P", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TP> p;
    @XmlAttribute(name = "type", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String type;

    public List<TP> getP() {
        if (p == null) {
            p = new ArrayList<TP>();
        }
        return this.p;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }

}
