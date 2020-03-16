
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tMopEnum", namespace = "http://www.iec.ch/61850/2006/SCLmaintenance")
@XmlEnum
public enum TMopEnum {

    M("M"),
    O("O"),
    P("P"),
    C("C"),
    @XmlEnumValue("C1")
    C_1("C1"),
    @XmlEnumValue("C2")
    C_2("C2");
    private final String value;

    TMopEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TMopEnum fromValue(String v) {
        for (TMopEnum c: TMopEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
