
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tDomainLNGroupSEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TDomainLNGroupSEnum {

    SARC,
    SIMG,
    SIML,
    SPDC;

    public String value() {
        return name();
    }

    public static TDomainLNGroupSEnum fromValue(String v) {
        return valueOf(v);
    }

}
