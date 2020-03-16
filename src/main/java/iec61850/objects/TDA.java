
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDA", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TDA
    extends TAbstractDataAttribute
{

    @XmlAttribute(name = "fc", required = true)
    protected TFCEnum fc;
    @XmlAttribute(name = "dchg")
    protected Boolean dchg;
    @XmlAttribute(name = "qchg")
    protected Boolean qchg;
    @XmlAttribute(name = "dupd")
    protected Boolean dupd;

    public TFCEnum getFc() {
        return fc;
    }

    public void setFc(TFCEnum value) {
        this.fc = value;
    }

    public boolean isDchg() {
        if (dchg == null) {
            return false;
        } else {
            return dchg;
        }
    }

    public void setDchg(Boolean value) {
        this.dchg = value;
    }

    public boolean isQchg() {
        if (qchg == null) {
            return false;
        } else {
            return qchg;
        }
    }

    public void setQchg(Boolean value) {
        this.qchg = value;
    }

    public boolean isDupd() {
        if (dupd == null) {
            return false;
        } else {
            return dupd;
        }
    }

    public void setDupd(Boolean value) {
        this.dupd = value;
    }

}
