
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tTransformerWindingEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TTransformerWindingEnum {

    PTW;

    public String value() {
        return name();
    }

    public static TTransformerWindingEnum fromValue(String v) {
        return valueOf(v);
    }

}
