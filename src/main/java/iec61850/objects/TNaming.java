
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tNaming", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlSeeAlso({
    TLNodeContainer.class
})
public abstract class TNaming extends TBaseElement {

    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String name;
    @XmlAttribute(name = "desc")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String desc;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String value) {
        this.desc = value;
    }

}
