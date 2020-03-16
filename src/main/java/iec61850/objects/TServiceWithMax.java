
package iec61850.objects;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tServiceWithMax", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlSeeAlso({
    TServiceWithMaxAndModify.class,
    TServiceWithMaxAndMaxAttributes.class
})
public class TServiceWithMax {

    @XmlAttribute(name = "max", required = true)
    protected long max;

    public long getMax() {
        return max;
    }

    public void setMax(long value) {
        this.max = value;
    }

}
