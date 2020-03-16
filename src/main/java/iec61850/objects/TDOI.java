
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDOI", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "sdiOrDAI"
})
public class TDOI
    extends TUnNaming
{

    @XmlElements({
        @XmlElement(name = "SDI", namespace = "http://www.iec.ch/61850/2006/SCL", type = TSDI.class),
        @XmlElement(name = "DAI", namespace = "http://www.iec.ch/61850/2006/SCL", type = TDAI.class)
    })
    protected List<TUnNaming> sdiOrDAI;
    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String name;
    @XmlAttribute(name = "ix")
    protected Long ix;
    @XmlAttribute(name = "accessControl")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String accessControl;

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

    public String getAccessControl() {
        return accessControl;
    }

    public void setAccessControl(String value) {
        this.accessControl = value;
    }

}
