package controllers.object.SVG;

import javafx.scene.shape.FillRule;
import lombok.Data;

import java.awt.*;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - SVG получаемый из xml
 */
@Data
public class SVGData {
    private String id;
    private String content;
    private FillRule fillRule;
    private javafx.scene.paint.Color fill;
    private double maxX = 0, maxY = 0;
}
