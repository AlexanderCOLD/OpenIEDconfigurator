package iec61850;

import lombok.Data;
import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - IED
 */
@Data
public class IED {
    private String name;
    private String description;
    private ArrayList<LD> ldList = new ArrayList<>();

    public String toString(){
        if(description==null) return name;
        else return name + " - " + description;
    }
}
