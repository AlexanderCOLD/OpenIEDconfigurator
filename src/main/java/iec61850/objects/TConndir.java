
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tConndir", namespace = "http://www.iec.ch/61850/2006/SCLcoordinates")
@XmlEnum
public enum TConndir {

    @XmlEnumValue("horizontal")
    HORIZONTAL("horizontal"),
    @XmlEnumValue("vertical")
    VERTICAL("vertical");
    private final String value;

    TConndir(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TConndir fromValue(String v) {
        for (TConndir c: TConndir.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
