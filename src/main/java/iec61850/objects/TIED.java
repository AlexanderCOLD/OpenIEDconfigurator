
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tIED", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "services",
    "accessPoint"
})
public class TIED extends TNaming {

    @XmlElement(name = "Services", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServices services;
    @XmlElement(name = "AccessPoint", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected List<TAccessPoint> accessPoint;
    @XmlAttribute(name = "type")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String type;
    @XmlAttribute(name = "manufacturer")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String manufacturer;
    @XmlAttribute(name = "configVersion")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String configVersion;

    public TServices getServices() {
        return services;
    }

    public void setServices(TServices value) {
        this.services = value;
    }

    public List<TAccessPoint> getAccessPoint() {
        if (accessPoint == null) {
            accessPoint = new ArrayList<TAccessPoint>();
        }
        return this.accessPoint;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String value) {
        this.manufacturer = value;
    }

    public String getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(String value) {
        this.configVersion = value;
    }

}
