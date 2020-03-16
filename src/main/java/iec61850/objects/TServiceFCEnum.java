
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tServiceFCEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TServiceFCEnum {

    SG,
    BR,
    RP,
    LG,
    GO,
    GS,
    MS,
    US;

    public String value() {
        return name();
    }

    public static TServiceFCEnum fromValue(String v) {
        return valueOf(v);
    }

}
