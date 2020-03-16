
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tSubNetwork", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "bitRate",
    "connectedAP"
})
public class TSubNetwork
    extends TNaming
{

    @XmlElement(name = "BitRate", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TBitRateInMbPerSec bitRate;
    @XmlElement(name = "ConnectedAP", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected List<TConnectedAP> connectedAP;
    @XmlAttribute(name = "type")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String type;

    public TBitRateInMbPerSec getBitRate() {
        return bitRate;
    }

    public void setBitRate(TBitRateInMbPerSec value) {
        this.bitRate = value;
    }

    public List<TConnectedAP> getConnectedAP() {
        if (connectedAP == null) {
            connectedAP = new ArrayList<TConnectedAP>();
        }
        return this.connectedAP;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }

}
