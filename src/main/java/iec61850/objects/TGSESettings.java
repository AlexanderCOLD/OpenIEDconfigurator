
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tGSESettings", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TGSESettings
    extends TServiceSettings
{

    @XmlAttribute(name = "appID")
    protected TServiceSettingsEnum appID;
    @XmlAttribute(name = "dataLabel")
    protected TServiceSettingsEnum dataLabel;

    public TServiceSettingsEnum getAppID() {
        if (appID == null) {
            return TServiceSettingsEnum.FIX;
        } else {
            return appID;
        }
    }

    public void setAppID(TServiceSettingsEnum value) {
        this.appID = value;
    }

    public TServiceSettingsEnum getDataLabel() {
        if (dataLabel == null) {
            return TServiceSettingsEnum.FIX;
        } else {
            return dataLabel;
        }
    }

    public void setDataLabel(TServiceSettingsEnum value) {
        this.dataLabel = value;
    }

}
