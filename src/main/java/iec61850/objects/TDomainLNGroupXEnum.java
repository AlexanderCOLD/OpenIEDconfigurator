
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tDomainLNGroupXEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TDomainLNGroupXEnum {

    XCBR,
    XSWI;

    public String value() {
        return name();
    }

    public static TDomainLNGroupXEnum fromValue(String v) {
        return valueOf(v);
    }

}
