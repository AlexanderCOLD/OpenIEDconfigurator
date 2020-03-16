package controllers.elements;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description
 */

@XmlAccessorType(XmlAccessType.PROPERTY)
@Data
public class LinkData {

    private String sourceID, targetID, sourceConnectorID, targetConnectorID;

    public LinkData(){}
    public LinkData(String sourceID, String targetID, String sourceConnectorID, String targetConnectorID) {
        this.sourceID = sourceID;
        this.targetID = targetID;
        this.sourceConnectorID = sourceConnectorID;
        this.targetConnectorID = targetConnectorID;
    }
}
