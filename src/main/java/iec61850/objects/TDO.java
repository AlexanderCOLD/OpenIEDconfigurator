
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDO", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TDO
    extends TUnNaming
{

    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String name;
    @XmlAttribute(name = "type", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String type;
    @XmlAttribute(name = "accessControl")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String accessControl;
    @XmlAttribute(name = "transient")
    protected Boolean _transient;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }

    public String getAccessControl() {
        return accessControl;
    }

    public void setAccessControl(String value) {
        this.accessControl = value;
    }

    public boolean isTransient() {
        if (_transient == null) {
            return false;
        } else {
            return _transient;
        }
    }

    public void setTransient(Boolean value) {
        this._transient = value;
    }

}
