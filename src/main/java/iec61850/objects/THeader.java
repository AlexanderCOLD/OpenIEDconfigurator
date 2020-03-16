
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tHeader", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "text",
    "history"
})
public class THeader {

    @XmlElement(name = "Text", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TText text;
    @XmlElement(name = "History", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected THeader.History history;
    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String id;
    @XmlAttribute(name = "version")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String version;
    @XmlAttribute(name = "revision")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String revision;
    @XmlAttribute(name = "toolID")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String toolID;
    @XmlAttribute(name = "nameStructure")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String nameStructure;

    public TText getText() {
        return text;
    }

    public void setText(TText value) {
        this.text = value;
    }

    public THeader.History getHistory() {
        return history;
    }

    public void setHistory(THeader.History value) {
        this.history = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String value) {
        this.version = value;
    }

    public String getRevision() {
        if (revision == null) {
            return "";
        } else {
            return revision;
        }
    }

    public void setRevision(String value) {
        this.revision = value;
    }

    public String getToolID() {
        return toolID;
    }

    public void setToolID(String value) {
        this.toolID = value;
    }

    public String getNameStructure() {
        if (nameStructure == null) {
            return "IEDName";
        } else {
            return nameStructure;
        }
    }

    public void setNameStructure(String value) {
        this.nameStructure = value;
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "hitem"
    })
    public static class History {

        @XmlElement(name = "Hitem", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
        protected List<THitem> hitem;

        public List<THitem> getHitem() {
            if (hitem == null) {
                hitem = new ArrayList<THitem>();
            }
            return this.hitem;
        }

    }

}
