package controllers.dialogs;

import application.GUI;
import application.Main;
import controllers.ResizeController;
import controllers.graphicNode.GraphicNode;
import iec61850.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.DefaultStringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Редактор конекторов
 */
public class ConnectorDialog extends AnchorPane{

	private static final ConnectorDialog self = new ConnectorDialog();
	private final Image lnIcon = new Image(Main.class.getResource("/view/image/LNIcon.png").toString());
	private final Image dsIcon = new Image(Main.class.getResource("/view/image/DSIcon.png").toString());
	private final Image doIcon = new Image(Main.class.getResource("/view/image/DOIcon.png").toString());
	private final Image daIcon = new Image(Main.class.getResource("/view/image/DAIcon.png").toString());

	private boolean draggable = false;
	private double dragOffsetX, dragOffsetY; // поправка при перетаскивании на позицию мышки

	private final Stage stage = new Stage();

	@FXML private Accordion accordion;
	@FXML private TitledPane titledPane;
	@FXML private TreeTableView<Object> treeTableView;
	@FXML private TreeTableColumn<Object,String> nameColumn;
	@FXML private TreeTableColumn<Object,Boolean> valueColumn;

	/* Корневой элемент LogicalNode */
	private final TreeItem<Object> rootItem = new TreeItem<Object>(){{
		setGraphic(new ImageView(lnIcon){{ setFitWidth(20); setFitHeight(20); }});
	}};

	/* Буффер из вложений DO / DA */
	private ArrayList<TreeItem<Object>> itemsDOBuffer, itemsDABuffer;
	private final ArrayList<TreeItem<Object>> itemsDOList = new ArrayList<TreeItem<Object>>(){{
		IntStream.range(0, 500).forEach(i -> this.add(new TreeItem<Object>(){{
			ImageView iv = new ImageView(doIcon){{ setFitHeight(20); setFitWidth(20); }}; setGraphic(iv); setExpanded(true);
		}} ));
	}};
	private final ArrayList<TreeItem<Object>> itemsDAList = new ArrayList<TreeItem<Object>>(){{
		IntStream.range(0, 500).forEach(i -> this.add(new TreeItem<Object>(){{
			ImageView iv = new ImageView(daIcon){{ setFitHeight(20); setFitWidth(20); }}; setGraphic(iv); setExpanded(true);
		}} ));
	}};

	/* Логический узел и DO/DA */
	private IECObject iecObject;
	private GraphicNode graphicNode;
	private final ArrayList<Object> objectList = new ArrayList<>();

	public ConnectorDialog() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/view/FXML/ConnectorDialog.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try { loader.load(); } catch (IOException e) { e.printStackTrace();	}

		Scene scene = new Scene(this);
		scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> { if((key.getCode()==KeyCode.ENTER && key.isControlDown()) || key.getCode()==KeyCode.ESCAPE) close(); });
		scene.getStylesheets().add("view/CSS/" + GUI.colorStyle + ".css");
		scene.getStylesheets().add("view/CSS/stylesheet.css");

		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initOwner(GUI.getStage());
		stage.setTitle("Параметры элемента");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/view/image/Icon.png")));
		stage.initModality(Modality.APPLICATION_MODAL);
		ResizeController.addStage(stage);

		/* Перетаскивание главного окна */
		addEventFilter(MouseEvent.MOUSE_PRESSED,e->{
			dragOffsetX = e.getScreenX() - stage.getX(); dragOffsetY = e.getScreenY() - stage.getY();
			draggable = !(dragOffsetY<7 || dragOffsetY>25 || dragOffsetX<7 || dragOffsetX>(stage.getWidth()-7)); // Границы перетаскивания
		});
		addEventFilter(MouseEvent.MOUSE_DRAGGED, e->{ if(draggable){ stage.setX(e.getScreenX() - this.dragOffsetX); stage.setY(e.getScreenY() - this.dragOffsetY); } });
	}

	@FXML
	private void initialize() {
		stage.setMinWidth(1300); stage.setMinHeight(400);
		stage.setWidth(1300); stage.setHeight(400);
		stage.setOnShowing(event -> onShowing()); stage.setOnHiding(event -> onHiding());
		accordion.setExpandedPane(titledPane);
		treeTableView.setRoot(rootItem);
		treeTableView.setEditable(true);

		/* Внесение данных из объекта в таблицу */
		nameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));
		valueColumn.setCellValueFactory(p-> new SimpleBooleanProperty(((IECObject) p.getValue().getValue()).getLayoutX()!=null));

		valueColumn.setCellFactory(param -> new CheckBoxTreeTableCell<Object, Boolean>(){
			@Override
			public void updateItem(Boolean item, boolean empty) {

				TreeItem<Object> currentItem = getTreeTableRow().getTreeItem();

				/* Если это DA/DO - добавляем CheckBox */
				if(currentItem != null && (currentItem.getValue().getClass()==DA.class || currentItem.getValue().getClass()==DO.class)){
					item = ((IECObject) currentItem.getValue()).getLayoutX()!=null;
					super.updateItem(item, empty);
				} else super.updateItem(null, true);
			}
		});

		/* Вызывается при внесении изменений */
		valueColumn.setOnEditCommit(event -> {
			System.out.println("COMMIT: " + event);
//			DA da = (DA) event.getRowValue().getValue();

			event.getTreeTableView().refresh();
		});
	}

	/**
	 * Вызов во время открытия
	 */
	private void onShowing(){
		titledPane.setText("Параметры - " + iecObject.getName());
		itemsDOBuffer = new ArrayList<>(itemsDOList);
		itemsDABuffer = new ArrayList<>(itemsDAList);
		for(TreeItem<Object> item: itemsDOBuffer) item.getChildren().clear();
		for(TreeItem<Object> item: itemsDABuffer) item.getChildren().clear();
		rootItem.getChildren().clear();

		rootItem.setValue(iecObject); rootItem.setExpanded(true);

		/* Наполнение таблицы параметрами */
		for(Object dataObject: objectList){
			if(dataObject.getClass()==DO.class){
				TreeItem<Object> localRoot = getDOItem(dataObject);
				rootItem.getChildren().add(localRoot);
				fillItem(localRoot, (DO) dataObject);
			}
			else if(dataObject.getClass()==DA.class){
				TreeItem<Object> localRoot = getDAItem(dataObject);
				rootItem.getChildren().add(localRoot);
			}
		}
	}

	/**
	 * Наполнить верку (рекурсия)
	 */
	private void fillItem(TreeItem<Object> root, DO dataObject){
		root.setExpanded(true);
		for(DO doCont:dataObject.getDataObjects()){ TreeItem<Object> doItem = getDOItem(doCont); doItem.setExpanded(true); root.getChildren().add(doItem); }
		for(DA daCont:dataObject.getDataAttributes()){ TreeItem<Object> daItem = getDAItem(daCont); root.setExpanded(true); root.getChildren().add(daItem); }

		for(TreeItem<Object> doItem:root.getChildren())
			if(doItem.getValue().getClass()==DO.class) {
				DO doCont = (DO) doItem.getValue();
				if(!doCont.getDataAttributes().isEmpty() || !doCont.getDataObjects().isEmpty()) fillItem(doItem, doCont);
			}
	}

	/** Вызов во время закрытия */
	private void onHiding(){ graphicNode.restConnectors(); }

	/** Взять элемент из буффера */
	public TreeItem<Object> getDOItem(Object dataObject){ TreeItem<Object> item = itemsDOBuffer.get(0); itemsDOBuffer.remove(item); item.setValue(dataObject); return item; }
	public TreeItem<Object> getDAItem(Object dataObject){ TreeItem<Object> item = itemsDABuffer.get(0); itemsDABuffer.remove(item); item.setValue(dataObject); return item; }

	/** Показать окно */
	public static void show(GraphicNode graphicNode) { self.graphicNode = graphicNode; self.iecObject = graphicNode.getIecObject(); self.stage.showAndWait(); }

	/** Скрыть окно */
	@FXML public void close(){ self.stage.hide(); }
}
