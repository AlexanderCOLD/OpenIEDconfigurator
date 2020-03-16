
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tLPHDEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TLPHDEnum {

    LPHD;

    public String value() {
        return name();
    }

    public static TLPHDEnum fromValue(String v) {
        return valueOf(v);
    }

}
