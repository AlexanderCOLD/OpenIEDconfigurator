
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tBaseElement", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "any",
    "text",
    "_private"
})
@XmlSeeAlso({
    TUnNaming.class,
    TIDNaming.class,
    TNaming.class
})
public abstract class TBaseElement {

    @XmlAnyElement(lax = true)
    protected List<Object> any;
    @XmlElement(name = "Text", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TText text;
    @XmlElement(name = "Private", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TPrivate> _private;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }

    public TText getText() {
        return text;
    }

    public void setText(TText value) {
        this.text = value;
    }

    public List<TPrivate> getPrivate() {
        if (_private == null) {
            _private = new ArrayList<TPrivate>();
        }
        return this._private;
    }

    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
