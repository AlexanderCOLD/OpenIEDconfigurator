package iec61850;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.UUID;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Data Object (Connector)
 */

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class DO {

    private String dataObjectName;     // Op, Str, ...
    private String dataAttributeName;  // phA, general, ...

    private String cppObjectName;
    private String cppAttributeName;

    public String toString(){ return String.format("%s (%s)", dataAttributeName, dataObjectName); }


    @XmlTransient
    private String ID;
    public DO(){ ID = UUID.randomUUID().toString(); }
}
