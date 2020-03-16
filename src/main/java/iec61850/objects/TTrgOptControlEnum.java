
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tTrgOptControlEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TTrgOptControlEnum {

    @XmlEnumValue("dchg")
    DCHG("dchg"),
    @XmlEnumValue("qchg")
    QCHG("qchg"),
    @XmlEnumValue("dupd")
    DUPD("dupd"),
    @XmlEnumValue("period")
    PERIOD("period"),
    @XmlEnumValue("none")
    NONE("none");
    private final String value;

    TTrgOptControlEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TTrgOptControlEnum fromValue(String v) {
        for (TTrgOptControlEnum c: TTrgOptControlEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
