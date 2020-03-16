
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tVal", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "value"
})
public class TVal {

    @XmlValue
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String value;
    @XmlAttribute(name = "sGroup")
    protected Long sGroup;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getSGroup() {
        return sGroup;
    }

    public void setSGroup(Long value) {
        this.sGroup = value;
    }

}
