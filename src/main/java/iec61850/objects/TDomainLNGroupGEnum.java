
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tDomainLNGroupGEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TDomainLNGroupGEnum {

    GAPC,
    GGIO,
    GSAL;

    public String value() {
        return name();
    }

    public static TDomainLNGroupGEnum fromValue(String v) {
        return valueOf(v);
    }

}
