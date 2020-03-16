
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tLDevice", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "ln0",
    "ln",
    "accessControl"
})
public class TLDevice
    extends TUnNaming
{

    @XmlElement(name = "LN0", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected LN0 ln0;
    @XmlElement(name = "LN", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TLN> ln;
    @XmlElement(name = "AccessControl", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TAccessControl accessControl;
    @XmlAttribute(name = "inst", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String inst;
    @XmlAttribute(name = "ldName")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String ldName;

    public LN0 getLN0() {
        return ln0;
    }

    public void setLN0(LN0 value) {
        this.ln0 = value;
    }

    public List<TLN> getLN() {
        if (ln == null) {
            ln = new ArrayList<TLN>();
        }
        return this.ln;
    }

    public TAccessControl getAccessControl() {
        return accessControl;
    }

    public void setAccessControl(TAccessControl value) {
        this.accessControl = value;
    }

    public String getInst() {
        return inst;
    }

    public void setInst(String value) {
        this.inst = value;
    }

    public String getLdName() {
        return ldName;
    }

    public void setLdName(String value) {
        this.ldName = value;
    }

}
