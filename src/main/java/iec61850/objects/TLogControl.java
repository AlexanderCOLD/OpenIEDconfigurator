
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tLogControl", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TLogControl
    extends TControlWithTriggerOpt
{

    @XmlAttribute(name = "logName", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String logName;
    @XmlAttribute(name = "logEna")
    protected Boolean logEna;
    @XmlAttribute(name = "reasonCode")
    protected Boolean reasonCode;

    public String getLogName() {
        return logName;
    }

    public void setLogName(String value) {
        this.logName = value;
    }

    public boolean isLogEna() {
        if (logEna == null) {
            return true;
        } else {
            return logEna;
        }
    }

    public void setLogEna(Boolean value) {
        this.logEna = value;
    }

    public boolean isReasonCode() {
        if (reasonCode == null) {
            return true;
        } else {
            return reasonCode;
        }
    }

    public void setReasonCode(Boolean value) {
        this.reasonCode = value;
    }

}
