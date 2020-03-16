
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tDomainLNGroupYEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TDomainLNGroupYEnum {

    YPTR,
    YEFN,
    YLTC,
    YPSH;

    public String value() {
        return name();
    }

    public static TDomainLNGroupYEnum fromValue(String v) {
        return valueOf(v);
    }

}
