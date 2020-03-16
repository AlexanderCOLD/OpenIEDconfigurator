
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tP", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "value"
})
@XmlSeeAlso({
    TPOSIAPTitle.class,
    TPOSIPSEL.class,
    TPVLANID.class,
    TPOSISSEL.class,
    TPOSIAPInvoke.class,
    TPVLANPRIORITY.class,
    TPOSINSAP.class,
    TPIP.class,
    TPIPGATEWAY.class,
    TPOSIAEQualifier.class,
    TPOSITSEL.class,
    TPIPSUBNET.class,
    TPOSIAEInvoke.class,
    TPMACAddress.class,
    TPAPPID.class
})
public class TP {

    @XmlValue
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String value;
    @XmlAttribute(name = "type", required = true)
    protected String type;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }

}
