package iec61850;

import lombok.Data;

import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Logical device
 */
@Data
public class LD {
    private String name;
    private String description;
    private ArrayList<LN> lnList = new ArrayList<>();
    private ArrayList<DataSet> gooseOutputDataSets = new ArrayList<>();
    private ArrayList<DataSet> gooseInputDataSets = new ArrayList<>();


    public String toString(){
        if(description==null) return name;
        else return name + " - " + description;
    }
}
