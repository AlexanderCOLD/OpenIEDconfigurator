
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tControlWithIEDName", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "iedName"
})
@XmlSeeAlso({
    TGSEControl.class,
    TSampledValueControl.class
})
public class TControlWithIEDName
    extends TControl
{

    @XmlElement(name = "IEDName", namespace = "http://www.iec.ch/61850/2006/SCL")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected List<String> iedName;
    @XmlAttribute(name = "confRev")
    protected Long confRev;

    public List<String> getIEDName() {
        if (iedName == null) {
            iedName = new ArrayList<String>();
        }
        return this.iedName;
    }

    public Long getConfRev() {
        return confRev;
    }

    public void setConfRev(Long value) {
        this.confRev = value;
    }

}
