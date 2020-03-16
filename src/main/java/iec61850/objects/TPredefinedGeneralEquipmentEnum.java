
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tPredefinedGeneralEquipmentEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TPredefinedGeneralEquipmentEnum {

    AXN,
    BAT,
    MOT;

    public String value() {
        return name();
    }

    public static TPredefinedGeneralEquipmentEnum fromValue(String v) {
        return valueOf(v);
    }

}
