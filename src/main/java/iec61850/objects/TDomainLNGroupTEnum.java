
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tDomainLNGroupTEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TDomainLNGroupTEnum {

    TCTR,
    TVTR;

    public String value() {
        return name();
    }

    public static TDomainLNGroupTEnum fromValue(String v) {
        return valueOf(v);
    }

}
