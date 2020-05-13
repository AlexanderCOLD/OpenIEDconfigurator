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
 * @description - Data Object (Connector)
 */

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class DO {

    @XmlTransient
    private String UID = UUID.randomUUID().toString();

    private String dataObjectName;     // Тип данных C++
    private String dataAttributeName;  // Название экземпляра С++
    private String description;        // Описание

    public DO() { }
    public DO(String dataObjectName, String dataAttributeName) { this.dataObjectName = dataObjectName; this.dataAttributeName = dataAttributeName; }

    @XmlElement(name = "DO")
    private ArrayList<DO> content = new ArrayList<>(); // Содержание (пустое для простых типов)

    public String toString(){
        return String.format("%s (%s)", dataAttributeName.replaceAll("[in_-out_]",""), dataObjectName.replaceAll("iec_", ""));
    }
}
