
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tValueWithUnit", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "value"
})
@XmlSeeAlso({
    TBitRateInMbPerSec.class,
    TVoltage.class,
    TDurationInSec.class,
    TDurationInMilliSec.class
})
public class TValueWithUnit {

    @XmlValue
    protected BigDecimal value;
    @XmlAttribute(name = "unit", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String unit;
    @XmlAttribute(name = "multiplier")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String multiplier;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String value) {
        this.unit = value;
    }

    public String getMultiplier() {
        if (multiplier == null) {
            return "";
        } else {
            return multiplier;
        }
    }

    public void setMultiplier(String value) {
        this.multiplier = value;
    }

}
