
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tText", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TText
    extends TAnyContentFromOtherNamespace
{

    @XmlAttribute(name = "source")
    protected String source;

    public String getSource() {
        return source;
    }

    public void setSource(String value) {
        this.source = value;
    }

}
