
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDAI", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "val"
})
public class TDAI
    extends TUnNaming
{

    @XmlElement(name = "Val", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TVal> val;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "sAddr")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String sAddr;
    @XmlAttribute(name = "valKind")
    protected TValKindEnum valKind;
    @XmlAttribute(name = "ix")
    protected Long ix;

    public List<TVal> getVal() {
        if (val == null) {
            val = new ArrayList<TVal>();
        }
        return this.val;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getSAddr() {
        return sAddr;
    }

    public void setSAddr(String value) {
        this.sAddr = value;
    }

    public TValKindEnum getValKind() {
        if (valKind == null) {
            return TValKindEnum.SET;
        } else {
            return valKind;
        }
    }

    public void setValKind(TValKindEnum value) {
        this.valKind = value;
    }

    public Long getIx() {
        return ix;
    }

    public void setIx(Long value) {
        this.ix = value;
    }

}
