
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tConnectedAP", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "address",
    "gse",
    "smv",
    "physConn"
})
public class TConnectedAP
    extends TUnNaming
{

    @XmlElement(name = "Address", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TAddress address;
    @XmlElement(name = "GSE", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TGSE> gse;
    @XmlElement(name = "SMV", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TSMV> smv;
    @XmlElement(name = "PhysConn", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TPhysConn> physConn;
    @XmlAttribute(name = "iedName", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String iedName;
    @XmlAttribute(name = "apName", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String apName;

    public TAddress getAddress() {
        return address;
    }

    public void setAddress(TAddress value) {
        this.address = value;
    }

    public List<TGSE> getGSE() {
        if (gse == null) {
            gse = new ArrayList<TGSE>();
        }
        return this.gse;
    }

    public List<TSMV> getSMV() {
        if (smv == null) {
            smv = new ArrayList<TSMV>();
        }
        return this.smv;
    }

    public List<TPhysConn> getPhysConn() {
        if (physConn == null) {
            physConn = new ArrayList<TPhysConn>();
        }
        return this.physConn;
    }

    public String getIedName() {
        return iedName;
    }

    public void setIedName(String value) {
        this.iedName = value;
    }

    public String getApName() {
        return apName;
    }

    public void setApName(String value) {
        this.apName = value;
    }

}
