
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tConfLNs", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TConfLNs {

    @XmlAttribute(name = "fixPrefix")
    protected Boolean fixPrefix;
    @XmlAttribute(name = "fixLnInst")
    protected Boolean fixLnInst;

    public boolean isFixPrefix() {
        if (fixPrefix == null) {
            return false;
        } else {
            return fixPrefix;
        }
    }

    public void setFixPrefix(Boolean value) {
        this.fixPrefix = value;
    }

    public boolean isFixLnInst() {
        if (fixLnInst == null) {
            return false;
        } else {
            return fixLnInst;
        }
    }

    public void setFixLnInst(Boolean value) {
        this.fixLnInst = value;
    }

}
