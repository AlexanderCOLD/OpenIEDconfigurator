
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEnumType", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "enumVal"
})
public class TEnumType
    extends TIDNaming
{

    @XmlElement(name = "EnumVal", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected List<TEnumVal> enumVal;

    public List<TEnumVal> getEnumVal() {
        if (enumVal == null) {
            enumVal = new ArrayList<TEnumVal>();
        }
        return this.enumVal;
    }

}
