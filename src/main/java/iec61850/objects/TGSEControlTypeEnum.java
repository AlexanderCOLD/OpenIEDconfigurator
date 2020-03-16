
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tGSEControlTypeEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TGSEControlTypeEnum {

    GSSE,
    GOOSE;

    public String value() {
        return name();
    }

    public static TGSEControlTypeEnum fromValue(String v) {
        return valueOf(v);
    }

}
