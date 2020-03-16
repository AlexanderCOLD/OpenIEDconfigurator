
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tPredefinedBasicTypeEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TPredefinedBasicTypeEnum {

    BOOLEAN("BOOLEAN"),
    @XmlEnumValue("INT8")
    INT_8("INT8"),
    @XmlEnumValue("INT16")
    INT_16("INT16"),
    @XmlEnumValue("INT24")
    INT_24("INT24"),
    @XmlEnumValue("INT32")
    INT_32("INT32"),
    @XmlEnumValue("INT128")
    INT_128("INT128"),
    @XmlEnumValue("INT8U")
    INT_8_U("INT8U"),
    @XmlEnumValue("INT16U")
    INT_16_U("INT16U"),
    @XmlEnumValue("INT24U")
    INT_24_U("INT24U"),
    @XmlEnumValue("INT32U")
    INT_32_U("INT32U"),
    @XmlEnumValue("FLOAT32")
    FLOAT_32("FLOAT32"),
    @XmlEnumValue("FLOAT64")
    FLOAT_64("FLOAT64"),
    @XmlEnumValue("Enum")
    ENUM("Enum"),
    @XmlEnumValue("Dbpos")
    DBPOS("Dbpos"),
    @XmlEnumValue("Tcmd")
    TCMD("Tcmd"),
    @XmlEnumValue("Quality")
    QUALITY("Quality"),
    @XmlEnumValue("Timestamp")
    TIMESTAMP("Timestamp"),
    @XmlEnumValue("VisString32")
    VIS_STRING_32("VisString32"),
    @XmlEnumValue("VisString64")
    VIS_STRING_64("VisString64"),
    @XmlEnumValue("VisString129")
    VIS_STRING_129("VisString129"),
    @XmlEnumValue("VisString255")
    VIS_STRING_255("VisString255"),
    @XmlEnumValue("Octet64")
    OCTET_64("Octet64"),
    @XmlEnumValue("Struct")
    STRUCT("Struct"),
    @XmlEnumValue("EntryTime")
    ENTRY_TIME("EntryTime"),
    @XmlEnumValue("Unicode255")
    UNICODE_255("Unicode255"),
    @XmlEnumValue("Check")
    CHECK("Check");
    private final String value;

    TPredefinedBasicTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TPredefinedBasicTypeEnum fromValue(String v) {
        for (TPredefinedBasicTypeEnum c: TPredefinedBasicTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
