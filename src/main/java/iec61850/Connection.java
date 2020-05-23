package iec61850;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description connection between two elements
 */

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Connection {
    private String sourceID;
    private String targetID;

    public Connection() {}
    public Connection(String sourceID, String targetID) { this.sourceID = sourceID; this.targetID = targetID; }
}

