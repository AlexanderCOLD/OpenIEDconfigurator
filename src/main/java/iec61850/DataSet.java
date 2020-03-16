package iec61850;

import lombok.Data;

import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description датасет
 */

@Data
public class DataSet {

    private String name;
    private String dsName;
    private String description;
    private String appId;
    private String iedName;
    private DSType type;

    private ArrayList<DO> doList = new ArrayList<>();
}
