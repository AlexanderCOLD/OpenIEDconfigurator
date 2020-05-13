package iec61850;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Intelligent electronic device
 */

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class IED {

    @XmlTransient
    private String UID = UUID.randomUUID().toString();

    private String name;
    private String description;
    @XmlElement(name = "LD")
    private ArrayList<LD> logicalDeviceList = new ArrayList<>();


    public String toString(){ if(description!=null && !description.equals("unknown")) return String.format("%s (%s)", name, description); else return name; }
    @XmlElement(name = "Connection")
    private ArrayList<Connection> connectionList = new ArrayList<>(); // Пока не используется

}

