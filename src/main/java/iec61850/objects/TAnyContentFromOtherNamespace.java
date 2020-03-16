
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tAnyContentFromOtherNamespace", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "content"
})
@XmlSeeAlso({
    TText.class,
    TPrivate.class,
    THitem.class
})
public abstract class TAnyContentFromOtherNamespace {

    @XmlMixed
    @XmlAnyElement(lax = true)
    protected List<Object> content;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    public List<Object> getContent() {
        if (content == null) {
            content = new ArrayList<Object>();
        }
        return this.content;
    }

    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
