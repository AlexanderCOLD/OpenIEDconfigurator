package iec61850;

import lombok.Data;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Logical device
 */
@Data
public class LN {

    private String name;
    private String description;
    private String prefix;


    public String toString(){
        if(description==null) return name;
        else return name + " - " + description;
    }
}
