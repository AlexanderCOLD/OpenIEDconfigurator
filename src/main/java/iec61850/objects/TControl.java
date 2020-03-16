
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tControl", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlSeeAlso({
    TControlWithIEDName.class,
    TControlWithTriggerOpt.class
})
public abstract class TControl
    extends TNaming
{

    @XmlAttribute(name = "datSet")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String datSet;

    public String getDatSet() {
        return datSet;
    }

    public void setDatSet(String value) {
        this.datSet = value;
    }

}
