
package iec61850.objects;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "tPredefinedPTypeEnum", namespace = "http://www.iec.ch/61850/2006/SCL")
@XmlEnum
public enum TPredefinedPTypeEnum {

    IP("IP"),
    @XmlEnumValue("IP-SUBNET")
    IP_SUBNET("IP-SUBNET"),
    @XmlEnumValue("IP-GATEWAY")
    IP_GATEWAY("IP-GATEWAY"),
    @XmlEnumValue("OSI-NSAP")
    OSI_NSAP("OSI-NSAP"),
    @XmlEnumValue("OSI-TSEL")
    OSI_TSEL("OSI-TSEL"),
    @XmlEnumValue("OSI-SSEL")
    OSI_SSEL("OSI-SSEL"),
    @XmlEnumValue("OSI-PSEL")
    OSI_PSEL("OSI-PSEL"),
    @XmlEnumValue("OSI-AP-Title")
    OSI_AP_TITLE("OSI-AP-Title"),
    @XmlEnumValue("OSI-AP-Invoke")
    OSI_AP_INVOKE("OSI-AP-Invoke"),
    @XmlEnumValue("OSI-AE-Qualifier")
    OSI_AE_QUALIFIER("OSI-AE-Qualifier"),
    @XmlEnumValue("OSI-AE-Invoke")
    OSI_AE_INVOKE("OSI-AE-Invoke"),
    @XmlEnumValue("MAC-Address")
    MAC_ADDRESS("MAC-Address"),
    APPID("APPID"),
    @XmlEnumValue("VLAN-PRIORITY")
    VLAN_PRIORITY("VLAN-PRIORITY"),
    @XmlEnumValue("VLAN-ID")
    VLAN_ID("VLAN-ID");
    private final String value;

    TPredefinedPTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TPredefinedPTypeEnum fromValue(String v) {
        for (TPredefinedPTypeEnum c: TPredefinedPTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
