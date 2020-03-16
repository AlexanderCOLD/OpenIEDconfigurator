
package iec61850.objects;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tSMVSettings", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "smpRate"
})
public class TSMVSettings
    extends TServiceSettings
{

    @XmlElement(name = "SmpRate", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected List<BigDecimal> smpRate;
    @XmlAttribute(name = "svID")
    protected TServiceSettingsEnum svID;
    @XmlAttribute(name = "optFields")
    protected TServiceSettingsEnum optFields;
    @XmlAttribute(name = "smpRateValue")
    protected TServiceSettingsEnum smpRateValue;

    public List<BigDecimal> getSmpRate() {
        if (smpRate == null) {
            smpRate = new ArrayList<BigDecimal>();
        }
        return this.smpRate;
    }

    public TServiceSettingsEnum getSvID() {
        if (svID == null) {
            return TServiceSettingsEnum.FIX;
        } else {
            return svID;
        }
    }

    public void setSvID(TServiceSettingsEnum value) {
        this.svID = value;
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

    public TServiceSettingsEnum getSmpRateValue() {
        if (smpRateValue == null) {
            return TServiceSettingsEnum.FIX;
        } else {
            return smpRateValue;
        }
    }

    public void setSmpRateValue(TServiceSettingsEnum value) {
        this.smpRateValue = value;
    }

}
