package iec61850;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

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
    private String cppPrefix;

    public String toString(){ return String.format("%s (%s)", dataAttributeName, dataObjectName); }
}
