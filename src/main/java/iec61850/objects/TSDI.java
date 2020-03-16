
package iec61850.objects;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tSDI", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "sdiOrDAI"
})
public class TSDI
    extends TUnNaming
{

    @XmlElements({
        @XmlElement(name = "SDI", namespace = "http://www.iec.ch/61850/2006/SCL", type = TSDI.class),
        @XmlElement(name = "DAI", namespace = "http://www.iec.ch/61850/2006/SCL", type = TDAI.class)
    })
    protected List<TUnNaming> sdiOrDAI;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "ix")
    protected Long ix;

    public List<TUnNaming> getSDIOrDAI() {
        if (sdiOrDAI == null) {
            sdiOrDAI = new ArrayList<TUnNaming>();
        }
        return this.sdiOrDAI;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public Long getIx() {
        return ix;
    }

    public void setIx(Long value) {
        this.ix = value;
    }

}
