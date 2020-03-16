
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tPowerTransformerEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TPowerTransformerEnum {

    PTR;

    public String value() {
        return name();
    }

    public static TPowerTransformerEnum fromValue(String v) {
        return valueOf(v);
    }

}
