
package iec61850.objects;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDataSet", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "fcdaOrFCCB"
})
public class TDataSet
    extends TNaming
{

    @XmlElements({
        @XmlElement(name = "FCDA", namespace = "http://www.iec.ch/61850/2006/SCL", type = TFCDA.class),
        @XmlElement(name = "FCCB", namespace = "http://www.iec.ch/61850/2006/SCL", type = TFCCB.class)
    })
    protected List<Object> fcdaOrFCCB;

    public List<Object> getFCDAOrFCCB() {
        if (fcdaOrFCCB == null) {
            fcdaOrFCCB = new ArrayList<Object>();
        }
        return this.fcdaOrFCCB;
    }

}
