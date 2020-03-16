
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tControlBlock", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "address"
})
@XmlSeeAlso({
    TSMV.class,
    TGSE.class
})
public abstract class TControlBlock
    extends TUnNaming
{

    @XmlElement(name = "Address", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TAddress address;
    @XmlAttribute(name = "ldInst", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String ldInst;
    @XmlAttribute(name = "cbName", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String cbName;

    public TAddress getAddress() {
        return address;
    }

    public void setAddress(TAddress value) {
        this.address = value;
    }

    public String getLdInst() {
        return ldInst;
    }

    public void setLdInst(String value) {
        this.ldInst = value;
    }

    public String getCbName() {
        return cbName;
    }

    public void setCbName(String value) {
        this.cbName = value;
    }

}
