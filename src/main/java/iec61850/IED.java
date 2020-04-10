package iec61850;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Intelligent electronic device
 */

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class IED {

    private String name;
    private String description;
    @XmlElement(name = "LD")
    private ArrayList<LD> logicalDeviceList = new ArrayList<>();

    public String toString(){ if(description!=null && !description.equals("unknown")) return String.format("%s (%s)", name, description); else return name; }
}

