
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tGSE", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "minTime",
    "maxTime"
})
public class TGSE
    extends TControlBlock
{

    @XmlElement(name = "MinTime", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TDurationInMilliSec minTime;
    @XmlElement(name = "MaxTime", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TDurationInMilliSec maxTime;

    public TDurationInMilliSec getMinTime() {
        return minTime;
    }

    public void setMinTime(TDurationInMilliSec value) {
        this.minTime = value;
    }

    public TDurationInMilliSec getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(TDurationInMilliSec value) {
        this.maxTime = value;
    }

}
