
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tConnectivityNode", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TConnectivityNode
    extends TLNodeContainer
{

    @XmlAttribute(name = "pathName", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String pathName;

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String value) {
        this.pathName = value;
    }

}
