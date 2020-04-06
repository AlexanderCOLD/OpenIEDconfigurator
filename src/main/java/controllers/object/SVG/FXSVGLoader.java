package controllers.object.SVG;

import application.Main;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.SVGPath;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Загрузка SVG и преобразование в , который содержит SVGPath
 */

public class FXSVGLoader {

    private static HashMap<String, ArrayList<SVGData>> imageList = new HashMap<>();
    private static ArrayList<SVGData> temp = new ArrayList<>();

    /**
     * Загрузить SVG
     * @param fileName - путь к картинке
     * @return - SVG картинку
     */
    public static SVG load(String fileName){
        if(imageList.containsKey(fileName)) return svgCopy(imageList.get(fileName));

        File file = null; URL url;

        url = Main.class.getResource("/"+fileName);
        if(url==null) url = Main.class.getResource("/"+fileName + ".svg");
        if(url==null) url = Main.class.getResource("/"+fileName + ".svg");
        if(url==null) url = Main.class.getResource("/view/image/SVG/"+fileName);
        if(url==null) url = Main.class.getResource("/view/image/SVG/"+fileName+".svg");

        if(url!=null) file = new File(url.getFile());
        if(file==null) file = new File(fileName);


        if(file.exists()){
            if(!imageList.containsKey(file.getName().replaceAll(".svg", ""))) loadSVG(file);
            if(imageList.containsKey(file.getName().replaceAll(".svg", "")))
                return svgCopy(imageList.get(file.getName().replaceAll(".svg", "")));
            else System.err.println("File " + fileName + " load error");
        }
        else{ System.err.println("File " + fileName + " not found"); }
        return null;
    }

    public static SVG load(File file){
        if(imageList.containsKey(file.getName().replaceAll(".svg", "")))
        return svgCopy(imageList.get(file.getName().replaceAll(".svg", "")));

        if(file.exists()){
            if(!imageList.containsKey(file.getName())) loadSVG(file);
            if(imageList.containsKey(file.getName()))
                return svgCopy(imageList.get(file.getName().replaceAll(".svg", "")));
            else System.err.println("File " + file.getName() + " load error");
        }
        else{ System.err.println("File " + file.getName() + " not found"); }
        return null;
    }

    /**
     * Создает копию SVG
     * @param svgSource - оригинал
     * @return - копию SVG
     */
    private static SVG svgCopy(ArrayList<SVGData> svgSource){
        SVG svg = new SVG();
        SVGData svgData;
        for(SVGData dataSource:svgSource){
            svgData = new SVGData();
            svgData.setId(dataSource.getId());
            svgData.setContent(dataSource.getContent());
            svgData.setFill(dataSource.getFill());
            svgData.setFillRule(dataSource.getFillRule());
            svgData.setMaxX(dataSource.getMaxX());
            svgData.setMaxY(dataSource.getMaxY());
            svg.addSVGData(svgData);
        }
        return svg;
    }

    /**
     * Загрузить SVG из файла
     * @param file - файл SVG
     * @return SVG (javafx)
     */
    private static ArrayList<SVGData> loadSVG(File file){
        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            temp.clear();
            if (doc.hasChildNodes()) findSVGPath(doc.getChildNodes());
            if(temp !=null) { imageList.put(file.getName().replaceAll(".svg",""), temp); return temp; }
        }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    /**
     * Поиск XML с тегом path во всех вложениях
     * @param nodeList - корневой лист узлов
     */
    private static void findSVGPath(NodeList nodeList) {
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node node = nodeList.item(count);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if(node.getNodeName().equals("path")) extractSVG(node); // Если это path то распарсить в SVGPath (javafx)
                if (node.hasChildNodes()) { findSVGPath(node.getChildNodes()); } // Если есть вложенные узлы - просмотреть их тоже
            }
        }
    }

    private static SVGPath tempPath = new SVGPath();

    /**
     * Извлечение из XML-Node -> SVGPath (javafx)
     * @param node - XML узел
     */
    private static void extractSVG(Node node){
        if (node.hasAttributes()) {
            NamedNodeMap nodeMap = node.getAttributes();
            SVGData svgData = new SVGData();
            for (int i = 0; i < nodeMap.getLength(); i++) {
                Node attrNode = nodeMap.item(i);
                if(attrNode.getNodeName()!=null && attrNode.getNodeValue()!=null){
                    if(attrNode.getNodeName().equals("id")){ svgData.setId(attrNode.getNodeValue()); }
                    if(attrNode.getNodeName().equals("fill")){ svgData.setFill(Color.web(attrNode.getNodeValue())); }
                    if(attrNode.getNodeName().equals("fill-rule") && attrNode.getNodeValue().equals("evenodd")){ svgData.setFillRule(FillRule.EVEN_ODD); }
                    if(attrNode.getNodeName().equals("d")){
                        tempPath.setContent(attrNode.getNodeValue());
                        svgData.setMaxX(tempPath.prefWidth(0));
                        svgData.setMaxY(tempPath.prefHeight(0));
                        svgData.setContent(attrNode.getNodeValue());
                    }
                }
            }
            if(!svgData.getContent().equals("")) temp.add(svgData); // если SVG распаршен правильно добавить во временный массив
        }
    }
}