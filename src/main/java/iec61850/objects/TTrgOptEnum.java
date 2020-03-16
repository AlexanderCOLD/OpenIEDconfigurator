
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tTrgOptEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TTrgOptEnum {

    @XmlEnumValue("dchg")
    DCHG("dchg"),
    @XmlEnumValue("qchg")
    QCHG("qchg"),
    @XmlEnumValue("dupd")
    DUPD("dupd"),
    @XmlEnumValue("none")
    NONE("none");
    private final String value;

    TTrgOptEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TTrgOptEnum fromValue(String v) {
        for (TTrgOptEnum c: TTrgOptEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
