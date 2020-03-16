
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tPredefinedCommonConductingEquipmentEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TPredefinedCommonConductingEquipmentEnum {

    CBR,
    DIS,
    VTR,
    CTR,
    GEN,
    CAP,
    REA,
    CON,
    MOT,
    EFN,
    PSH,
    BAT,
    BSH,
    CAB,
    GIL,
    LIN,
    RRC,
    SAR,
    TCF,
    TCR,
    IFL;

    public String value() {
        return name();
    }

    public static TPredefinedCommonConductingEquipmentEnum fromValue(String v) {
        return valueOf(v);
    }

}
