
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tDomainLNGroupCEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TDomainLNGroupCEnum {

    CILO,
    CSWI,
    CALH,
    CCGR,
    CPOW;

    public String value() {
        return name();
    }

    public static TDomainLNGroupCEnum fromValue(String v) {
        return valueOf(v);
    }

}
