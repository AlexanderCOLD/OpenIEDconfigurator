package controllers.graphicNode;

import controllers.link.Link;
import iec61850.IECObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 04/2020
 * @project OpenIEDconfigurator
 * @description - Коннектор, graphicNode - узел в котором лежит, dataObject - объект, который представляет, dataSet - датасет к которому относится DO
 */

@Getter @Setter
public class Connector extends AnchorPane {

    private static final DropShadow selected = new DropShadow( BlurType.ONE_PASS_BOX, Color.RED, 15, 0.7, 0, 0);
    private static final String unselectedStyle = "-fx-background-color: WHITE; -fx-background-radius: 5";
    private static final String selectedStyle = "-fx-background-color: RED; -fx-background-radius: 5";
    private static final double size = 7;
    private final Scale scale = new Scale(1.0,1.0,size/2,size/2);
    private final Tooltip tooltip = new Tooltip(){{ setFont(Font.font(14)); }};
    private GraphicNode graphicNode;
    private IECObject iecObject;
    private ConnectorType connectorType;
    private ConnectorPosition position;
    private AnchorPane connectorPane;
    private Label label;
    private final ArrayList<Link> connections = new ArrayList<>();

    /**
     * Создать коннектор для графического элемента
     * @param iecObject - DO/DA
     * @param connectorType - входящее/исходящее
     * @param position - слева/справа
     */
    public Connector(GraphicNode graphicNode, IECObject iecObject, ConnectorType connectorType, ConnectorPosition position) {
        this.graphicNode = graphicNode; this.iecObject = iecObject; this.connectorType = connectorType; this.position = position;
        initialize();
    }

    private void initialize(){
        tooltip.setOnShowing(e -> { tooltip.setText(iecObject.getDescription()); });
        tooltip.setStyle("-fx-background-color: transparent;");
        Tooltip.install(this, tooltip);

        setPadding(new Insets(-2, -3, -2, -3));
        getTransforms().add(scale);
        setPrefSize(size,size);

        label = new Label(){{
            setPadding(new Insets(-7, -2, -7, -2));
            setTextFill(Color.WHITE);
            setFont(Font.font(10.0));
            textProperty().bind(new SimpleStringProperty(){
                @Override
                public String get() {
                    String type = Connector.this.iecObject.getType();
                    String name = Connector.this.iecObject.getName();
                    type = type!=null ? "("+type.replaceAll("iec_","")+")" : "(err)";
                    name = name!=null ? name.replaceAll("in_","").replaceAll("out_","") : "(err)";
                    if(Connector.this.position==ConnectorPosition.left) return String.format("%s %s", name, type);
                    else return String.format("%s %s", type, name);
                }
            });
        }};
        Tooltip.install(label, tooltip);

        /* Прозрачная панелька на которой лежит коннектор */
        connectorPane = new AnchorPane(){{
            setStyle("-fx-background-color: transparent;");
            setPadding(new Insets(-2, -5, -2, -5));
            setPrefHeight(5); setMaxHeight(5);
            getChildren().add(Connector.this);
            getChildren().add(label);
        }};

        AnchorPane.setTopAnchor(label, 2.0); AnchorPane.setLeftAnchor(label, 12.0); AnchorPane.setRightAnchor(label, 12.0);
        AnchorPane.setTopAnchor(this, 0.0);

        /* Выставляет положение коннектора и лейбла в зависимости от положения коннектора на граф. элементе */
        if(position==ConnectorPosition.left){ AnchorPane.setLeftAnchor(this, 0.0); label.setAlignment(Pos.CENTER_LEFT); }
        else{ AnchorPane.setRightAnchor(this, 0.0); label.setAlignment(Pos.CENTER_RIGHT); }

        setSelected(false);
    }

    /** Стиль коннектора */
    public void setSelected(boolean value){
        if(value) { setStyle(selectedStyle); scale.setX(2.0); scale.setY(2.0); label.setEffect(selected); }
        else { setStyle(unselectedStyle); scale.setX(1.0); scale.setY(1.0); label.setEffect(null); }
    }
    public ArrayList<Link> getConnections() { return connections; }

    public String toString(){ if(iecObject !=null) return iecObject.toString(); else return "unknown"; }
}
