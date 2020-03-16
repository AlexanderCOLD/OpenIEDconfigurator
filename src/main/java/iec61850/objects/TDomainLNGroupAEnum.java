
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tDomainLNGroupAEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TDomainLNGroupAEnum {

    ANCR,
    ARCO,
    ATCC,
    AVCO;

    public String value() {
        return name();
    }

    public static TDomainLNGroupAEnum fromValue(String v) {
        return valueOf(v);
    }

}
