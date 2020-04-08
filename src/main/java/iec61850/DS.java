package iec61850;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description DataSet
 */

@Data
@XmlRootElement
public class DS {

    private String name;
    private String description;
    private DSType type = DSType.Node;

    private String datSetName; // Имя из списка всех датасетов
    private String id; // appID - для Goose, rptID - для MMS

    private int intgPd, bufTime; // для MMS

    private ArrayList<DO> dataObject = new ArrayList<>();

    public String toString(){
        return String.format("%s (%s)", name, type.toString());
    }
}
