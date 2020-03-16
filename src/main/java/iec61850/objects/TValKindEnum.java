
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tValKindEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TValKindEnum {

    @XmlEnumValue("Spec")
    SPEC("Spec"),
    @XmlEnumValue("Conf")
    CONF("Conf"),
    RO("RO"),
    @XmlEnumValue("Set")
    SET("Set");
    private final String value;

    TValKindEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TValKindEnum fromValue(String v) {
        for (TValKindEnum c: TValKindEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
