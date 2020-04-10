package iec61850;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description DataSet
 */

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class DS {

    private String name;
    private String description;
    private DSType type = DSType.Node;

    private String datSetName; // Имя из списка всех датасетов
    private String id; // appID - для Goose, rptID - для MMS

    private int intgPd, bufTime; // для MMS

    @XmlElement(name = "DO")
    private ArrayList<DO> dataObject = new ArrayList<>();

    public String toString(){
        return String.format("%s (%s)", name, type.toString());
    }
}
