
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tDomainLNGroupZEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TDomainLNGroupZEnum {

    ZAXN,
    ZBAT,
    ZBSH,
    ZCAB,
    ZCAP,
    ZCON,
    ZGEN,
    ZGIL,
    ZLIN,
    ZMOT,
    ZREA,
    ZRRC,
    ZSAR,
    ZTCF,
    ZTCR;

    public String value() {
        return name();
    }

    public static TDomainLNGroupZEnum fromValue(String v) {
        return valueOf(v);
    }

}
