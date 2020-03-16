
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tReportSettings", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TReportSettings
    extends TServiceSettings
{

    @XmlAttribute(name = "rptID")
    protected TServiceSettingsEnum rptID;
    @XmlAttribute(name = "optFields")
    protected TServiceSettingsEnum optFields;
    @XmlAttribute(name = "bufTime")
    protected TServiceSettingsEnum bufTime;
    @XmlAttribute(name = "trgOps")
    protected TServiceSettingsEnum trgOps;
    @XmlAttribute(name = "intgPd")
    protected TServiceSettingsEnum intgPd;

    public TServiceSettingsEnum getRptID() {
        if (rptID == null) {
            return TServiceSettingsEnum.FIX;
        } else {
            return rptID;
        }
    }

    public void setRptID(TServiceSettingsEnum value) {
        this.rptID = value;
    }

    public TServiceSettingsEnum getOptFields() {
        if (optFields == null) {
            return TServiceSettingsEnum.FIX;
        } else {
            return optFields;
        }
    }

    public void setOptFields(TServiceSettingsEnum value) {
        this.optFields = value;
    }

    public TServiceSettingsEnum getBufTime() {
        if (bufTime == null) {
            return TServiceSettingsEnum.FIX;
        } else {
            return bufTime;
        }
    }

    public void setBufTime(TServiceSettingsEnum value) {
        this.bufTime = value;
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
