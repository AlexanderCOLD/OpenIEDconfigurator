package iec61850;

import lombok.Data;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description
 */

@Data
public class DO {
    String name;
    String description;
    String type;

    public String toString(){
        if(type==null) return name;
        else return name + " - " + type;
    }
}
