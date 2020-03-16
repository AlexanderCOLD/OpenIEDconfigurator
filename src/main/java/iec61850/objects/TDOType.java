
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDOType", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "sdoOrDA"
})
public class TDOType
    extends TIDNaming
{

    @XmlElements({
        @XmlElement(name = "SDO", namespace = "http://www.iec.ch/61850/2006/SCL", type = TSDO.class),
        @XmlElement(name = "DA", namespace = "http://www.iec.ch/61850/2006/SCL", type = TDA.class)
    })
    protected List<TBaseElement> sdoOrDA;
    @XmlAttribute(name = "iedType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String iedType;
    @XmlAttribute(name = "cdc", required = true)
    protected String cdc;

    public List<TBaseElement> getSDOOrDA() {
        if (sdoOrDA == null) {
            sdoOrDA = new ArrayList<TBaseElement>();
        }
        return this.sdoOrDA;
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

    public String getCdc() {
        return cdc;
    }

    public void setCdc(String value) {
        this.cdc = value;
    }

}
