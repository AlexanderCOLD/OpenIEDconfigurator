
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tPredefinedCDCEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TPredefinedCDCEnum {

    SPS,
    DPS,
    INS,
    ACT,
    ACD,
    SEC,
    BCR,
    MV,
    CMV,
    SAV,
    WYE,
    DEL,
    SEQ,
    HMV,
    HWYE,
    HDEL,
    SPC,
    DPC,
    INC,
    BSC,
    ISC,
    APC,
    SPG,
    ING,
    ASG,
    CURVE,
    DPL,
    LPL,
    CSD;

    public String value() {
        return name();
    }

    public static TPredefinedCDCEnum fromValue(String v) {
        return valueOf(v);
    }

}
