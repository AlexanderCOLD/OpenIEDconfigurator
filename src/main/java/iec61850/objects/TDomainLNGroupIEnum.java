
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tDomainLNGroupIEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TDomainLNGroupIEnum {

    IHMI,
    IARC,
    ITCI,
    ITMI;

    public String value() {
        return name();
    }

    public static TDomainLNGroupIEnum fromValue(String v) {
        return valueOf(v);
    }

}
