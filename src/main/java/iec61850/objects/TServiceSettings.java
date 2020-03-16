
package iec61850.objects;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tServiceSettings", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlSeeAlso({
    TReportSettings.class,
    TLogSettings.class,
    TSMVSettings.class,
    TGSESettings.class
})
public abstract class TServiceSettings {

    @XmlAttribute(name = "cbName")
    protected TServiceSettingsEnum cbName;
    @XmlAttribute(name = "datSet")
    protected TServiceSettingsEnum datSet;

    public TServiceSettingsEnum getCbName() {
        if (cbName == null) {
            return TServiceSettingsEnum.FIX;
        } else {
            return cbName;
        }
    }

    public void setCbName(TServiceSettingsEnum value) {
        this.cbName = value;
    }

    public TServiceSettingsEnum getDatSet() {
        if (datSet == null) {
            return TServiceSettingsEnum.FIX;
        } else {
            return datSet;
        }
    }

    public void setDatSet(TServiceSettingsEnum value) {
        this.datSet = value;
    }

}
