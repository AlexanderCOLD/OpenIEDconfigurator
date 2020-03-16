
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tLNodeType", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "_do"
})
public class TLNodeType
    extends TIDNaming
{

    @XmlElement(name = "DO", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected List<TDO> _do;
    @XmlAttribute(name = "iedType")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String iedType;
    @XmlAttribute(name = "lnClass", required = true)
    protected List<String> lnClass;

    public List<TDO> getDO() {
        if (_do == null) {
            _do = new ArrayList<TDO>();
        }
        return this._do;
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

    public List<String> getLnClass() {
        if (lnClass == null) {
            lnClass = new ArrayList<String>();
        }
        return this.lnClass;
    }

}
