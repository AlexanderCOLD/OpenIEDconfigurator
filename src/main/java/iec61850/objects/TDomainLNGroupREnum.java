
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tDomainLNGroupREnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TDomainLNGroupREnum {

    RSYN,
    RDRE,
    RADR,
    RBDR,
    RDRS,
    RBRF,
    RDIR,
    RFLO,
    RPSB,
    RREC;

    public String value() {
        return name();
    }

    public static TDomainLNGroupREnum fromValue(String v) {
        return valueOf(v);
    }

}
