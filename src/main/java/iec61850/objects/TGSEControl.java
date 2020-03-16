
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tGSEControl", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TGSEControl
    extends TControlWithIEDName
{

    @XmlAttribute(name = "type")
    protected TGSEControlTypeEnum type;
    @XmlAttribute(name = "appID", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String appID;

    public TGSEControlTypeEnum getType() {
        if (type == null) {
            return TGSEControlTypeEnum.GOOSE;
        } else {
            return type;
        }
    }

    public void setType(TGSEControlTypeEnum value) {
        this.type = value;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String value) {
        this.appID = value;
    }

}
