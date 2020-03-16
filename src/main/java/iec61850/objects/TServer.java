
package iec61850.objects;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tServer", namespace = "http://www.iec.ch/61850/2006/SCL", propOrder = {
    "authentication",
    "lDevice",
    "association"
})
public class TServer
    extends TUnNaming
{

    @XmlElement(name = "Authentication", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected TServer.Authentication authentication;
    @XmlElement(name = "LDevice", namespace = "http://www.iec.ch/61850/2006/SCL", required = true)
    protected List<TLDevice> lDevice;
    @XmlElement(name = "Association", namespace = "http://www.iec.ch/61850/2006/SCL")
    protected List<TAssociation> association;
    @XmlAttribute(name = "timeout")
    protected Long timeout;

    public TServer.Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(TServer.Authentication value) {
        this.authentication = value;
    }

    public List<TLDevice> getLDevice() {
        if (lDevice == null) {
            lDevice = new ArrayList<TLDevice>();
        }
        return this.lDevice;
    }

    public List<TAssociation> getAssociation() {
        if (association == null) {
            association = new ArrayList<TAssociation>();
        }
        return this.association;
    }

    public long getTimeout() {
        if (timeout == null) {
            return  30L;
        } else {
            return timeout;
        }
    }

    public void setTimeout(Long value) {
        this.timeout = value;
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Authentication {

        @XmlAttribute(name = "none")
        protected Boolean none;
        @XmlAttribute(name = "password")
        protected Boolean password;
        @XmlAttribute(name = "weak")
        protected Boolean weak;
        @XmlAttribute(name = "strong")
        protected Boolean strong;
        @XmlAttribute(name = "certificate")
        protected Boolean certificate;

        public boolean isNone() {
            if (none == null) {
                return true;
            } else {
                return none;
            }
        }

        public void setNone(Boolean value) {
            this.none = value;
        }

        public boolean isPassword() {
            if (password == null) {
                return false;
            } else {
                return password;
            }
        }

        public void setPassword(Boolean value) {
            this.password = value;
        }

        public boolean isWeak() {
            if (weak == null) {
                return false;
            } else {
                return weak;
            }
        }

        public void setWeak(Boolean value) {
            this.weak = value;
        }

        public boolean isStrong() {
            if (strong == null) {
                return false;
            } else {
                return strong;
            }
        }

        public void setStrong(Boolean value) {
            this.strong = value;
        }

        public boolean isCertificate() {
            if (certificate == null) {
                return false;
            } else {
                return certificate;
            }
        }

        public void setCertificate(Boolean value) {
            this.certificate = value;
        }

    }

}
