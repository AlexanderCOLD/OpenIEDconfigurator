
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tReportControl", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "optFields",
    "rptEnabled"
})
public class TReportControl
    extends TControlWithTriggerOpt
{

    @XmlElement(name = "OptFields", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected TReportControl.OptFields optFields;
    @XmlElement(name = "RptEnabled", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected TRptEnabled rptEnabled;
    @XmlAttribute(name = "rptID", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String rptID;
    @XmlAttribute(name = "confRev", required = true)
    protected long confRev;
    @XmlAttribute(name = "buffered")
    protected Boolean buffered;
    @XmlAttribute(name = "bufTime")
    protected Long bufTime;

    public TReportControl.OptFields getOptFields() {
        return optFields;
    }

    public void setOptFields(TReportControl.OptFields value) {
        this.optFields = value;
    }

    public TRptEnabled getRptEnabled() {
        return rptEnabled;
    }

    public void setRptEnabled(TRptEnabled value) {
        this.rptEnabled = value;
    }

    public String getRptID() {
        return rptID;
    }

    public void setRptID(String value) {
        this.rptID = value;
    }

    public long getConfRev() {
        return confRev;
    }

    public void setConfRev(long value) {
        this.confRev = value;
    }

    public boolean isBuffered() {
        if (buffered == null) {
            return false;
        } else {
            return buffered;
        }
    }

    public void setBuffered(Boolean value) {
        this.buffered = value;
    }

    public long getBufTime() {
        if (bufTime == null) {
            return  0L;
        } else {
            return bufTime;
        }
    }

    public void setBufTime(Long value) {
        this.bufTime = value;
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class OptFields {

        @XmlAttribute(name = "seqNum")
        protected Boolean seqNum;
        @XmlAttribute(name = "timeStamp")
        protected Boolean timeStamp;
        @XmlAttribute(name = "dataSet")
        protected Boolean dataSet;
        @XmlAttribute(name = "reasonCode")
        protected Boolean reasonCode;
        @XmlAttribute(name = "dataRef")
        protected Boolean dataRef;
        @XmlAttribute(name = "bufOvfl")
        protected Boolean bufOvfl;
        @XmlAttribute(name = "entryID")
        protected Boolean entryID;
        @XmlAttribute(name = "configRef")
        protected Boolean configRef;

        public boolean isSeqNum() {
            if (seqNum == null) {
                return false;
            } else {
                return seqNum;
            }
        }

        public void setSeqNum(Boolean value) {
            this.seqNum = value;
        }

        public boolean isTimeStamp() {
            if (timeStamp == null) {
                return false;
            } else {
                return timeStamp;
            }
        }

        public void setTimeStamp(Boolean value) {
            this.timeStamp = value;
        }

        public boolean isDataSet() {
            if (dataSet == null) {
                return false;
            } else {
                return dataSet;
            }
        }

        public void setDataSet(Boolean value) {
            this.dataSet = value;
        }

        public boolean isReasonCode() {
            if (reasonCode == null) {
                return false;
            } else {
                return reasonCode;
            }
        }

        public void setReasonCode(Boolean value) {
            this.reasonCode = value;
        }

        public boolean isDataRef() {
            if (dataRef == null) {
                return false;
            } else {
                return dataRef;
            }
        }

        public void setDataRef(Boolean value) {
            this.dataRef = value;
        }

        public boolean isBufOvfl() {
            if (bufOvfl == null) {
                return false;
            } else {
                return bufOvfl;
            }
        }

        public void setBufOvfl(Boolean value) {
            this.bufOvfl = value;
        }

        public boolean isEntryID() {
            if (entryID == null) {
                return false;
            } else {
                return entryID;
            }
        }

        public void setEntryID(Boolean value) {
            this.entryID = value;
        }

        public boolean isConfigRef() {
            if (configRef == null) {
                return false;
            } else {
                return configRef;
            }
        }

        public void setConfigRef(Boolean value) {
            this.configRef = value;
        }

    }

}
