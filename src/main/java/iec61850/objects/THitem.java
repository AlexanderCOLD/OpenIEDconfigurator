
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tHitem", namespace = "http://www.iec.ch/61850/2006/SCL")
public class THitem
    extends TAnyContentFromOtherNamespace
{

    @XmlAttribute(name = "version", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String version;
    @XmlAttribute(name = "revision", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String revision;
    @XmlAttribute(name = "when", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String when;
    @XmlAttribute(name = "who")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String who;
    @XmlAttribute(name = "what")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String what;
    @XmlAttribute(name = "why")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String why;

    public String getVersion() {
        return version;
    }

    public void setVersion(String value) {
        this.version = value;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String value) {
        this.revision = value;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String value) {
        this.when = value;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String value) {
        this.who = value;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String value) {
        this.what = value;
    }

    public String getWhy() {
        return why;
    }

    public void setWhy(String value) {
        this.why = value;
    }

}
