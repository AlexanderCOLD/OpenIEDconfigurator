
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tLogSettings", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TLogSettings
    extends TServiceSettings
{

    @XmlAttribute(name = "logEna")
    protected TServiceSettingsEnum logEna;
    @XmlAttribute(name = "trgOps")
    protected TServiceSettingsEnum trgOps;
    @XmlAttribute(name = "intgPd")
    protected TServiceSettingsEnum intgPd;

    public TServiceSettingsEnum getLogEna() {
        if (logEna == null) {
            return TServiceSettingsEnum.FIX;
        } else {
            return logEna;
        }
    }

    public void setLogEna(TServiceSettingsEnum value) {
        this.logEna = value;
    }

    public TServiceSettingsEnum getTrgOps() {
        if (trgOps == null) {
            return TServiceSettingsEnum.FIX;
        } else {
            return trgOps;
        }
    }

    public void setTrgOps(TServiceSettingsEnum value) {
        this.trgOps = value;
    }

    public TServiceSettingsEnum getIntgPd() {
        if (intgPd == null) {
            return TServiceSettingsEnum.FIX;
        } else {
            return intgPd;
        }
    }

    public void setIntgPd(TServiceSettingsEnum value) {
        this.intgPd = value;
    }

}
