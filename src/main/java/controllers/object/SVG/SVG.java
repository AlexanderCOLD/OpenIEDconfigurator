package controllers.object.SVG;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.FillRule;
import javafx.scene.transform.Scale;

import javax.sound.sampled.Clip;
import java.util.*;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - SVG Image
 */
public class SVG extends Group {

    private HashMap<SVGData, Canvas> pathList = new HashMap<>();
    private Scale scale = new Scale();
    private double prefWidth = 200;
    private double prefHeight = 200;
    double maxWidth = 1; double maxHeight = 1;

    public SVG(){  }
    public SVG(Collection<SVGData> collection){ addAllSVGPath(collection); }


    public void addAllSVGPath(Collection<SVGData> collection) { Object[] list = collection.toArray(); for(Object p:list){ addSVGData((SVGData)p); } }

    public void addSVGData(SVGData svgData){
        Canvas canvas = new Canvas();
        if(!svgData.getContent().equals("")){
            canvas.getGraphicsContext2D().appendSVGPath(svgData.getContent());

            canvas.setId(svgData.getId()!=null ? svgData.getId() : UUID.randomUUID().toString());
            canvas.getGraphicsContext2D().setFill(svgData.getFill()!=null ? svgData.getFill() : Color.BLACK);
            canvas.getGraphicsContext2D().setFillRule(svgData.getFillRule()!=null ? svgData.getFillRule() : FillRule.NON_ZERO);

            canvas.widthProperty().set(svgData.getMaxX());
            canvas.heightProperty().set(svgData.getMaxY());

            canvas.getTransforms().add(scale);
            pathList.put(svgData, canvas);
            getChildren().add(canvas);
            updateSize();
        }
        else System.err.println("SVG is not contain content");

    }

    /**
     * Размер картинки
     */
    private void updateSize(){
        maxWidth = 1; maxHeight = 1;
        for(SVGData svg:pathList.keySet()){ maxWidth = Math.max(svg.getMaxX(), maxWidth); maxHeight = Math.max(svg.getMaxY(), maxHeight); }
        double scaleX = prefWidth/ maxWidth, scaleY = prefHeight/ maxHeight;
//        scale.setPivotX(0);
//        scale.setPivotY(0);
        scale.setX(scaleX);
        scale.setY(scaleY);
        updatePanelSize(maxWidth, maxHeight);
    }

    /**
     * Размер подложки (равен максимальному SVG)
     */
    private void updatePanelSize(double x, double y){
        for(Node node:getChildren()){
            ((Canvas)node).widthProperty().set(x);
            ((Canvas)node).heightProperty().set(y);
            ((Canvas)node).getGraphicsContext2D().fill();
        }
    }
    public void setPrefSize(double prefWidth, double prefHeight) { this.prefWidth = prefWidth; this.prefHeight = prefHeight; updateSize(); }
    public void setFill(Paint color){ for(Node svg:getChildren()) if(svg.getClass()==Canvas.class){  ((Canvas)svg).getGraphicsContext2D().setFill(color); ((Canvas)svg).getGraphicsContext2D().fill(); } }
}
