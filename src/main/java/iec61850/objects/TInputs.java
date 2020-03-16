
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tInputs", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "extRef"
})
public class TInputs
    extends TUnNaming
{

    @XmlElement(name = "ExtRef", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected List<TExtRef> extRef;

    public List<TExtRef> getExtRef() {
        if (extRef == null) {
            extRef = new ArrayList<TExtRef>();
        }
        return this.extRef;
    }

}
