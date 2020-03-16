
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tAuthenticationEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TAuthenticationEnum {

    @XmlEnumValue("none")
    NONE("none"),
    @XmlEnumValue("password")
    PASSWORD("password"),
    @XmlEnumValue("week")
    WEEK("week"),
    @XmlEnumValue("strong")
    STRONG("strong"),
    @XmlEnumValue("certificate")
    CERTIFICATE("certificate");
    private final String value;

    TAuthenticationEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TAuthenticationEnum fromValue(String v) {
        for (TAuthenticationEnum c: TAuthenticationEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
