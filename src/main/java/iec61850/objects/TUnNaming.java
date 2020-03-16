
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tUnNaming", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlSeeAlso({
    TLNode.class,
    TTerminal.class
})
public abstract class TUnNaming
    extends TBaseElement
{

    @XmlAttribute(name = "desc")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String value) {
        this.desc = value;
    }

}
