
package iec61850.objects;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "CondDesc", namespace = "http://www.iec.ch/61850/2006/SCLmaintenance")
public class CondDesc {

    @XmlAttribute(name = "desc", required = true)
    protected String desc;
    @XmlAttribute(name = "mop", required = true)
    protected String mop;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String value) {
        this.desc = value;
    }

    public String getMop() {
        return mop;
    }

    public void setMop(String value) {
        this.mop = value;
    }

}
