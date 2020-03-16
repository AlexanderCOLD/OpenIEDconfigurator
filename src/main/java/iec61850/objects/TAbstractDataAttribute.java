
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tAbstractDataAttribute", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "val"
})
@XmlSeeAlso({
    TDA.class,
    TBDA.class
})
public abstract class TAbstractDataAttribute
    extends TUnNaming
{

    @XmlElement(name = "Val", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TVal> val;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "sAddr")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String sAddr;
    @XmlAttribute(name = "bType", required = true)
    protected String bType;
    @XmlAttribute(name = "valKind")
    protected TValKindEnum valKind;
    @XmlAttribute(name = "type")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String type;
    @XmlAttribute(name = "count")
    protected Long count;

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

    public String getBType() {
        return bType;
    }

    public void setBType(String value) {
        this.bType = value;
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

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }

    public long getCount() {
        if (count == null) {
            return  0L;
        } else {
            return count;
        }
    }

    public void setCount(Long value) {
        this.count = value;
    }

}
