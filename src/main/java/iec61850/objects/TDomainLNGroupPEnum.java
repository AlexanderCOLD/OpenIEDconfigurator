
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tDomainLNGroupPEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TDomainLNGroupPEnum {

    PDIF,
    PDIS,
    PDIR,
    PDOP,
    PDUP,
    PFRC,
    PHAR,
    PHIZ,
    PIOC,
    PMRI,
    PMSS,
    POPF,
    PPAM,
    PSCH,
    PSDE,
    PTEF,
    PTOC,
    PTOF,
    PTOV,
    PTRC,
    PTTR,
    PTUC,
    PTUV,
    PUPF,
    PTUF,
    PVOC,
    PVPH,
    PZSU;

    public String value() {
        return name();
    }

    public static TDomainLNGroupPEnum fromValue(String v) {
        return valueOf(v);
    }

}
