
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tLLN0Enum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TLLN0Enum {

    @XmlEnumValue("LLN0")
    LLN_0("LLN0");
    private final String value;

    TLLN0Enum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TLLN0Enum fromValue(String v) {
        for (TLLN0Enum c: TLLN0Enum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
