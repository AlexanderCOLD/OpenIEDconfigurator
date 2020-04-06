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

    private ArrayList<LN> logicalNodeList = new ArrayList<>();

    private ArrayList<DS> gooseOutputDS = new ArrayList<>();
    private ArrayList<DS> gooseInputDS = new ArrayList<>();
    private ArrayList<DS> mmsOutputDS = new ArrayList<>();

    public String toString(){ if(description!=null && !description.equals("unknown")) return String.format("%s (%s)", name, description); else return name; }
}
