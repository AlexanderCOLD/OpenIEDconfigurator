
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tServiceSettingsEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TServiceSettingsEnum {

    @XmlEnumValue("Dyn")
    DYN("Dyn"),
    @XmlEnumValue("Conf")
    CONF("Conf"),
    @XmlEnumValue("Fix")
    FIX("Fix");
    private final String value;

    TServiceSettingsEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TServiceSettingsEnum fromValue(String v) {
        for (TServiceSettingsEnum c: TServiceSettingsEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
