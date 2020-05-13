package controllers.graphicNode;

import iec61850.DO;
import iec61850.DS;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
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
    private ConnectorPosition position;
    private AnchorPane connectorPane;

    public Connector(GraphicNode graphicNode, DO dataObject, DS dataSet, ConnectorType connectorType, ConnectorPosition position) {
        this.graphicNode = graphicNode; this.dataObject = dataObject; this.connectorType = connectorType; this.dataSet = dataSet; this.position = position;
        initialize();
    }

    private void initialize(){
//        Tooltip tooltip=new Tooltip("Description");
//        Tooltip.install(this, tooltip);

        setPrefSize(size,size);
        scale.setPivotX(size/2);  scale.setPivotY(size/2); getTransforms().add(scale);
        setSelected(false);

        Label label = new Label(){{
            setText(String.format("%s (%s)", dataObject.getDataAttributeName().replaceAll("[in_-out_]",""), dataObject.getDataObjectName().replaceAll("iec_", "")));
            setTextFill(Color.WHITE);
            /*label.setFont(Font.font(10.0));*/
        }};

        /* Прозрачная панелька на которой лежит коннектор */
        connectorPane = new AnchorPane(){{
            setStyle("-fx-background-color: transparent");
            getChildren().add(Connector.this);
            getChildren().add(label);
        }};

        AnchorPane.setTopAnchor(label, 3.0); AnchorPane.setLeftAnchor(label, 7.0); AnchorPane.setRightAnchor(label, 7.0);
        AnchorPane.setTopAnchor(this, 8.0);

        /* Выставляет положение коннектора и лейбла в зависимости от положения коннектора на граф. элементе */
        if(position==ConnectorPosition.left){ AnchorPane.setLeftAnchor(this, -5.0); label.setAlignment(Pos.CENTER_LEFT); } else{ AnchorPane.setRightAnchor(this, -5.0); label.setAlignment(Pos.CENTER_RIGHT); }
    }

    /**
     * Стиль коннектора
     */
    public void setSelected(boolean value){ if(value) { setStyle(selectedStyle); scale.setX(1.5); scale.setY(1.5); toFront(); } else { setStyle(unselectedStyle); scale.setX(1.0); scale.setY(1.0); }  }

    public String toString(){ if(dataObject!=null) return dataObject.toString(); else return "unknown"; }
}
