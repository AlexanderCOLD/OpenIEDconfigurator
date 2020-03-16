
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tFCEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TFCEnum {

    ST,
    MX,
    CO,
    SP,
    SG,
    SE,
    SV,
    CF,
    DC,
    EX;

    public String value() {
        return name();
    }

    public static TFCEnum fromValue(String v) {
        return valueOf(v);
    }

}
