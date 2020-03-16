
package iec61850.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tClientServices", namespace = "http://www.iec.ch/61850/2006/SCL")
public class TClientServices {

    @XmlAttribute(name = "goose")
    protected Boolean goose;
    @XmlAttribute(name = "gsse")
    protected Boolean gsse;
    @XmlAttribute(name = "bufReport")
    protected Boolean bufReport;
    @XmlAttribute(name = "unbufReport")
    protected Boolean unbufReport;
    @XmlAttribute(name = "readLog")
    protected Boolean readLog;

    public boolean isGoose() {
        if (goose == null) {
            return false;
        } else {
            return goose;
        }
    }

    public void setGoose(Boolean value) {
        this.goose = value;
    }

    public boolean isGsse() {
        if (gsse == null) {
            return false;
        } else {
            return gsse;
        }
    }

    public void setGsse(Boolean value) {
        this.gsse = value;
    }

    public boolean isBufReport() {
        if (bufReport == null) {
            return false;
        } else {
            return bufReport;
        }
    }

    public void setBufReport(Boolean value) {
        this.bufReport = value;
    }

    public boolean isUnbufReport() {
        if (unbufReport == null) {
            return false;
        } else {
            return unbufReport;
        }
    }

    public void setUnbufReport(Boolean value) {
        this.unbufReport = value;
    }

    public boolean isReadLog() {
        if (readLog == null) {
            return false;
        } else {
            return readLog;
        }
    }

    public void setReadLog(Boolean value) {
        this.readLog = value;
    }

}
