
package iec61850.objects;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "header",
    "substation",
    "communication",
    "ied",
    "dataTypeTemplates"
})
@XmlRootElement(name = "SCL", namespace = "http://www.iec.ch/61850/2006/SCL")
public class SCL
    extends TBaseElement
{

    @XmlElement(name = "Header", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected THeader header;
    @XmlElement(name = "Substation", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TSubstation> substation;
    @XmlElement(name = "Communication", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TCommunication communication;
    @XmlElement(name = "IED", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TIED> ied;
    @XmlElement(name = "DataTypeTemplates", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TDataTypeTemplates dataTypeTemplates;

    public THeader getHeader() {
        return header;
    }

    public void setHeader(THeader value) {
        this.header = value;
    }

    public List<TSubstation> getSubstation() {
        if (substation == null) {
            substation = new ArrayList<TSubstation>();
        }
        return this.substation;
    }

    public TCommunication getCommunication() {
        return communication;
    }

    public void setCommunication(TCommunication value) {
        this.communication = value;
    }

    public List<TIED> getIED() {
        if (ied == null) {
            ied = new ArrayList<TIED>();
        }
        return this.ied;
    }

    public TDataTypeTemplates getDataTypeTemplates() {
        return dataTypeTemplates;
    }

    public void setDataTypeTemplates(TDataTypeTemplates value) {
        this.dataTypeTemplates = value;
    }

}
