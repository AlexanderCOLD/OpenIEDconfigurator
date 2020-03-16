
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigInteger;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEnumVal", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "value"
})
public class TEnumVal {

    @XmlValue
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String value;
    @XmlAttribute(name = "ord", required = true)
    protected BigInteger ord;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public BigInteger getOrd() {
        return ord;
    }

    public void setOrd(BigInteger value) {
        this.ord = value;
    }

}
