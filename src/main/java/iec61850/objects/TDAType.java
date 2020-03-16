
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDAType", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "bda"
})
public class TDAType
    extends TIDNaming
{

    @XmlElement(name = "BDA", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected List<TBDA> bda;
    @XmlAttribute(name = "iedType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String iedType;

    public List<TBDA> getBDA() {
        if (bda == null) {
            bda = new ArrayList<TBDA>();
        }
        return this.bda;
    }

    public String getIedType() {
        if (iedType == null) {
            return "";
        } else {
            return iedType;
        }
    }

    public void setIedType(String value) {
        this.iedType = value;
    }

}
