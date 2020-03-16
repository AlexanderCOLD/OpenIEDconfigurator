
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tAnyLN", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "dataSet",
    "reportControl",
    "logControl",
    "doi",
    "inputs"
})
@XmlSeeAlso({
    TLN.class,
    TLN0.class
})
public abstract class TAnyLN
    extends TUnNaming
{

    @XmlElement(name = "DataSet", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TDataSet> dataSet;
    @XmlElement(name = "ReportControl", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TReportControl> reportControl;
    @XmlElement(name = "LogControl", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TLogControl> logControl;
    @XmlElement(name = "DOI", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TDOI> doi;
    @XmlElement(name = "Inputs", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TInputs inputs;
    @XmlAttribute(name = "lnType", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String lnType;

    public List<TDataSet> getDataSet() {
        if (dataSet == null) {
            dataSet = new ArrayList<TDataSet>();
        }
        return this.dataSet;
    }

    public List<TReportControl> getReportControl() {
        if (reportControl == null) {
            reportControl = new ArrayList<TReportControl>();
        }
        return this.reportControl;
    }

    public List<TLogControl> getLogControl() {
        if (logControl == null) {
            logControl = new ArrayList<TLogControl>();
        }
        return this.logControl;
    }

    public List<TDOI> getDOI() {
        if (doi == null) {
            doi = new ArrayList<TDOI>();
        }
        return this.doi;
    }

    public TInputs getInputs() {
        return inputs;
    }

    public void setInputs(TInputs value) {
        this.inputs = value;
    }

    public String getLnType() {
        return lnType;
    }

    public void setLnType(String value) {
        this.lnType = value;
    }

}
