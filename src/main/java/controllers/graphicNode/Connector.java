package controllers.graphicNode;

import iec61850.DO;
import iec61850.DS;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import lombok.Data;

/**
 * @author Александр Холодов
 * @created 04/2020
 * @project OpenIEDconfigurator
 * @description - Коннектор, graphicNode - узел в котором лежит, dataObject - объект, который представляет, dataSet - датасет к которому относится DO
 */

@Data
public class Connector extends AnchorPane {

    private static final double size = 7;
    private static final String unselectedStyle = "-fx-background-color: WHITE; -fx-background-radius: 5";
    private static final String selectedStyle = "-fx-background-color: RED; -fx-background-radius: 5";
    private final Scale scale = new Scale();
    private GraphicNode graphicNode;
    private DO dataObject;
    private DS dataSet;
    private ConnectorType connectorType;

    public Connector(GraphicNode graphicNode, DO dataObject, DS dataSet, ConnectorType connectorType) {
        this.graphicNode = graphicNode; this.dataObject = dataObject; this.connectorType = connectorType; this.dataSet = dataSet;
        initialize();
    }

    private void initialize(){
        setPrefSize(size,size);
        scale.setPivotX(size/2);  scale.setPivotY(size/2); getTransforms().add(scale);
        setSelected(false);
    }

    /**
     * Стиль коннектора
     */
    public void setSelected(boolean value){ if(value) { setStyle(selectedStyle); scale.setX(1.5); scale.setY(1.5); toFront(); } else { setStyle(unselectedStyle); scale.setX(1.0); scale.setY(1.0); }  }

    public String toString(){ if(dataObject!=null) return dataObject.toString(); else return "unknown"; }
}
