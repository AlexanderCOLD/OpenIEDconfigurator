
package iec61850.objects;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tSampledValueControl", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "smvOpts"
})
public class TSampledValueControl
    extends TControlWithIEDName
{

    @XmlElement(name = "SmvOpts", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected TSampledValueControl.SmvOpts smvOpts;
    @XmlAttribute(name = "smvID", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String smvID;
    @XmlAttribute(name = "multicast")
    protected Boolean multicast;
    @XmlAttribute(name = "smpRate", required = true)
    protected long smpRate;
    @XmlAttribute(name = "nofASDU", required = true)
    protected long nofASDU;

    public TSampledValueControl.SmvOpts getSmvOpts() {
        return smvOpts;
    }

    public void setSmvOpts(TSampledValueControl.SmvOpts value) {
        this.smvOpts = value;
    }

    public String getSmvID() {
        return smvID;
    }

    public void setSmvID(String value) {
        this.smvID = value;
    }

    public boolean isMulticast() {
        if (multicast == null) {
            return true;
        } else {
            return multicast;
        }
    }

    public void setMulticast(Boolean value) {
        this.multicast = value;
    }

    public long getSmpRate() {
        return smpRate;
    }

    public void setSmpRate(long value) {
        this.smpRate = value;
    }

    public long getNofASDU() {
        return nofASDU;
    }

    public void setNofASDU(long value) {
        this.nofASDU = value;
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class SmvOpts {

        @XmlAttribute(name = "refreshTime")
        protected Boolean refreshTime;
        @XmlAttribute(name = "sampleSynchronized")
        protected Boolean sampleSynchronized;
        @XmlAttribute(name = "sampleRate")
        protected Boolean sampleRate;
        @XmlAttribute(name = "dataSet")
        protected Boolean dataSet;
        @XmlAttribute(name = "security")
        protected Boolean security;

        public boolean isRefreshTime() {
            if (refreshTime == null) {
                return false;
            } else {
                return refreshTime;
            }
        }

        public void setRefreshTime(Boolean value) {
            this.refreshTime = value;
        }

        public boolean isSampleSynchronized() {
            if (sampleSynchronized == null) {
                return true;
            } else {
                return sampleSynchronized;
            }
        }

        public void setSampleSynchronized(Boolean value) {
            this.sampleSynchronized = value;
        }

        public boolean isSampleRate() {
            if (sampleRate == null) {
                return false;
            } else {
                return sampleRate;
            }
        }

        public void setSampleRate(Boolean value) {
            this.sampleRate = value;
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

        public boolean isSecurity() {
            if (security == null) {
                return false;
            } else {
                return security;
            }
        }

        public void setSecurity(Boolean value) {
            this.security = value;
        }

    }

}
