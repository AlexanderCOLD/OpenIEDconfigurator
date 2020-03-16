
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tPhaseEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TPhaseEnum {

    A("A"),
    B("B"),
    C("C"),
    N("N"),
    @XmlEnumValue("all")
    ALL("all"),
    @XmlEnumValue("none")
    NONE("none");
    private final String value;

    TPhaseEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TPhaseEnum fromValue(String v) {
        for (TPhaseEnum c: TPhaseEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
