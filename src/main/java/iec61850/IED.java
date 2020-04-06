package iec61850;

import lombok.Data;
import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Intelligent electronic device
 */
@Data
public class IED {

    private String name;
    private String description;
    private ArrayList<LD> logicalDeviceList = new ArrayList<>();

    public String toString(){ if(description!=null && !description.equals("unknown")) return String.format("%s (%s)", name, description); else return name; }
}
