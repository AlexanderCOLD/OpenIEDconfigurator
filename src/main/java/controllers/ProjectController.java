package controllers;

import controllers.elements.GraphicNode;
import controllers.elements.Link;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Текущий проект
 */
public class ProjectController {

	private static ProjectController self = null;
	private static HashMap<String, GraphicNode> allElements = new HashMap<String, GraphicNode>();
	private static HashMap<String, Link> allLinks = new HashMap<String, Link>();

	/**
	 * Листнер который вызывается при добавлении/удалении графических элементов
	 */
	private static ListChangeListener<Node> listener = c ->{
		c.next();
		if(c.wasAdded()) {
			Node node = c.getAddedSubList().get(0);
			if(node.getClass()== GraphicNode.class) { allElements.put(node.getId(), (GraphicNode) node); }
			else if(node.getClass()==Link.class) {allLinks.put(node.getId(),(Link)node); }
		}
		if(c.wasRemoved()) {
			Node node = c.getRemoved().get(0);
			if(node.getClass()== GraphicNode.class) { allElements.remove(node.getId()); }
			else if(node.getClass()==Link.class) {allLinks.remove(node.getId()); }
		}
	};

	public static HashMap<String, GraphicNode> getAllElements(){ return allElements; }
	public static HashMap<String, Link> getAllLinks(){ return allLinks; }
	public static void addToListening(AnchorPane pane) { pane.getChildren().addListener(listener);	}
}
