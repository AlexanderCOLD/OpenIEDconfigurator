package iec61850;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Logical node
 */

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "LN")
public class LN {

    private String name;
    private String description;
    private String classType;

    private DS dataSetInput = new DS();   // connections
    private DS dataSetOutput = new DS();  // connections

    public String toString(){ if(classType!=null && !classType.equals("unknown")) return String.format("%s (%s)", name, classType); else return name; }
}
