
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tPredefinedAttributeNameEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TPredefinedAttributeNameEnum {

    T("T"),
    @XmlEnumValue("Test")
    TEST("Test"),
    @XmlEnumValue("Check")
    CHECK("Check"),
    @XmlEnumValue("SIUnit")
    SI_UNIT("SIUnit"),
    @XmlEnumValue("Oper")
    OPER("Oper"),
    SBO("SBO"),
    @XmlEnumValue("SBOw")
    SB_OW("SBOw"),
    @XmlEnumValue("Cancel")
    CANCEL("Cancel");
    private final String value;

    TPredefinedAttributeNameEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TPredefinedAttributeNameEnum fromValue(String v) {
        for (TPredefinedAttributeNameEnum c: TPredefinedAttributeNameEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
