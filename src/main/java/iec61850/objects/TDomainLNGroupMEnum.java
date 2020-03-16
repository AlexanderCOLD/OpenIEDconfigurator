
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tDomainLNGroupMEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TDomainLNGroupMEnum {

    MMXU,
    MDIF,
    MHAI,
    MHAN,
    MMTR,
    MMXN,
    MSQI,
    MSTA;

    public String value() {
        return name();
    }

    public static TDomainLNGroupMEnum fromValue(String v) {
        return valueOf(v);
    }

}
