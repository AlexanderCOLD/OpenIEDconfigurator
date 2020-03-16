
package iec61850.objects;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tAccessPoint", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "server",
    "ln"
})
public class TAccessPoint
    extends TNaming
{

    @XmlElement(name = "Server", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TServer server;
    @XmlElement(name = "LN", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TLN> ln;
    @XmlAttribute(name = "router")
    protected Boolean router;
    @XmlAttribute(name = "clock")
    protected Boolean clock;

    public TServer getServer() {
        return server;
    }

    public void setServer(TServer value) {
        this.server = value;
    }

    public List<TLN> getLN() {
        if (ln == null) {
            ln = new ArrayList<TLN>();
        }
        return this.ln;
    }

    public boolean isRouter() {
        if (router == null) {
            return false;
        } else {
            return router;
        }
    }

    public void setRouter(Boolean value) {
        this.router = value;
    }

    public boolean isClock() {
        if (clock == null) {
            return false;
        } else {
            return clock;
        }
    }

    public void setClock(Boolean value) {
        this.clock = value;
    }

}
