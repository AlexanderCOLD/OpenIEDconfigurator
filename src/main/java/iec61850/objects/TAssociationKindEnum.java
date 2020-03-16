
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tAssociationKindEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TAssociationKindEnum {

    @XmlEnumValue("pre-established")
    PRE_ESTABLISHED("pre-established"),
    @XmlEnumValue("predefined")
    PREDEFINED("predefined");
    private final String value;

    TAssociationKindEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TAssociationKindEnum fromValue(String v) {
        for (TAssociationKindEnum c: TAssociationKindEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
