
package iec61850.objects;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tServiceWithMaxAndMaxAttributes", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlSeeAlso({
    TServiceWithMaxAndMaxAttributesAndModify.class
})
public class TServiceWithMaxAndMaxAttributes
    extends TServiceWithMax
{

    @XmlAttribute(name = "maxAttributes")
    protected Long maxAttributes;

    public Long getMaxAttributes() {
        return maxAttributes;
    }

    public void setMaxAttributes(Long value) {
        this.maxAttributes = value;
    }

}
