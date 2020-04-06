package iec61850;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Data Object (Connector)
 */

@Data
@XmlRootElement
public class DO {

    String dataObjectName;     // Op, Str, ...
    String dataAttributeName;  // phA, general, ...

    public String toString(){ return String.format("%s (%s)", dataAttributeName, dataObjectName); }
}
