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
 * @description - Logical device
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class LD {

    private String name;
    private String description;

    @XmlElement(name = "LN")
    private ArrayList<LN> logicalNodeList = new ArrayList<>();

    @XmlElement(name = "GOOSEOutputDataSet")
    private ArrayList<DS> gooseOutputDS = new ArrayList<>();
    @XmlElement(name = "GOOSEInputDataSet")
    private ArrayList<DS> gooseInputDS = new ArrayList<>();
    @XmlElement(name = "MMSOutputDataSet")
    private ArrayList<DS> mmsOutputDS = new ArrayList<>();

    @XmlElement(name = "Connection")
    private ArrayList<Connection> connectionList = new ArrayList<>();


    public String toString(){ if(description!=null && !description.equals("unknown")) return String.format("%s (%s)", name, description); else return name; }


    @XmlTransient
    private String ID;
    public LD(){ ID = UUID.randomUUID().toString(); }
}
