
package iec61850.objects;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tControlWithTriggerOpt", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "trgOps"
})
@XmlSeeAlso({
    TReportControl.class,
    TLogControl.class
})
public abstract class TControlWithTriggerOpt
    extends TControl
{

    @XmlElement(name = "TrgOps", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TTrgOps trgOps;
    @XmlAttribute(name = "intgPd")
    protected Long intgPd;

    public TTrgOps getTrgOps() {
        return trgOps;
    }

    public void setTrgOps(TTrgOps value) {
        this.trgOps = value;
    }

    public long getIntgPd() {
        if (intgPd == null) {
            return  0L;
        } else {
            return intgPd;
        }
    }

    public void setIntgPd(Long value) {
        this.intgPd = value;
    }

}
