package iec61850;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
 * @description - Data Object (Connector)
 */

@Getter @Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class DO {

    @XmlTransient
    private String UID = UUID.randomUUID().toString();

    private String dataObjectName;     // Тип данных C++
    private String dataAttributeName;  // Название экземпляра С++
    private String description;        // Описание

    private String value;              // Значение

    public DO() { }
    public DO(String dataObjectName, String dataAttributeName) { this.dataObjectName = dataObjectName; this.dataAttributeName = dataAttributeName; }

    @XmlElement(name = "DO")
    private ArrayList<DO> content = new ArrayList<>(); // Вложенные классы

    @XmlTransient
    private DO parentDO;                // Родительский DO (для построения иерархии)

    public String toString(){
        String daName = dataAttributeName != null ? dataAttributeName.replaceAll("[in_-out_]","") : "emptyDA";
        String doName = dataObjectName != null ? dataObjectName.replaceAll("iec_", "") : "emptyDO";
        return String.format("%s (%s)", daName,doName);
    }
}
