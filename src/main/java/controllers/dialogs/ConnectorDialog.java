package controllers.dialogs;

import application.GUI;
import application.Main;
import controllers.ResizeController;
import controllers.graphicNode.Connector;
import controllers.graphicNode.ConnectorPosition;
import controllers.graphicNode.GraphicNode;
import iec61850.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Редактор конекторов
 */
public class ConnectorDialog extends AnchorPane{

	private static final ConnectorDialog self = new ConnectorDialog();
	private final Image doIcon = new Image(Main.class.getResource("/view/image/DOIcon.png").toString());
	private final Image daIcon = new Image(Main.class.getResource("/view/image/DAIcon.png").toString());

	private boolean draggable = false;
	private double dragOffsetX, dragOffsetY; // поправка при перетаскивании на позицию мышки

	private final Stage stage = new Stage();

	@FXML private Accordion accordion;
	@FXML private TitledPane titledPane;
	@FXML private TreeTableView<Object> treeTableView_L, treeTableView_R;
	@FXML private TreeTableColumn<Object,String> nameColumn_L, nameColumn_R;
	@FXML private TreeTableColumn<Object,Boolean> valueColumn_L, valueColumn_R;

	/* Корневой элемент LogicalNode */
	private final TreeItem<Object> rootItem_L = new TreeItem<>();
	private final TreeItem<Object> rootItem_R = new TreeItem<>();
	private final CheckBox rootCB_L = new CheckBox(), rootCB_R = new CheckBox();

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

	/* Текущий графический элемент */
	private GraphicNode graphicNode;

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
		scene.addEventFilter(KeyEvent.KEY_PRESSED, e->{ if(e.getCode()==KeyCode.ESCAPE) stage.hide(); });
	}

	@FXML
	private void initialize() {
		stage.setMinWidth(500); stage.setMinHeight(400);
		stage.setWidth(500); stage.setHeight(400);
		stage.setOnShowing(event -> onShowing()); stage.setOnHiding(event -> onHiding());
		accordion.setExpandedPane(titledPane);
		treeTableView_L.setRoot(rootItem_L);
		treeTableView_L.setEditable(true);
		treeTableView_R.setRoot(rootItem_R);
		treeTableView_R.setEditable(true);

		/* Внесение данных из объекта в таблицу */
		initTable(nameColumn_L, valueColumn_L);
		initTable(nameColumn_R, valueColumn_R);

		treeTableView_L.setPlaceholder(new Label("Входящие соединения отсутствуют"));
		treeTableView_R.setPlaceholder(new Label("Исходящие соединения отсутствуют"));

		/* Установка групповых CheckBox */
		rootCB_L.selectedProperty().addListener((o, ov, nv) -> {
			if(graphicNode==null) return;
			Double val = nv ? 1.0 : null;
			graphicNode.getConnectors().stream()
					.filter(c -> c.getPosition()==ConnectorPosition.left)
					.forEach(c -> { c.getIecObject().setLayoutX(val); c.getIecObject().setLayoutY(val); });
			treeTableView_L.refresh();
		});
		valueColumn_L.setText(null); valueColumn_L.setGraphic(rootCB_L);
		Platform.runLater(() -> { rootCB_L.setSelected(false); rootCB_L.fire(); });

		rootCB_R.selectedProperty().addListener((o, ov, nv) -> {
			if(graphicNode==null) return;
			Double val = nv ? 1.0 : null;
			graphicNode.getConnectors().stream()
					.filter(c -> c.getPosition()==ConnectorPosition.right)
					.forEach(c -> { c.getIecObject().setLayoutX(val); c.getIecObject().setLayoutY(val); });
			treeTableView_R.refresh();
		});
		valueColumn_R.setText(null); valueColumn_R.setGraphic(rootCB_R);
		Platform.runLater(() -> { rootCB_R.setSelected(false); rootCB_R.fire(); });
	}

	private void initTable(TreeTableColumn<Object,String> nameColumn, TreeTableColumn<Object,Boolean> valueColumn){
		/* Наполнение названиями */
		nameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));
		/* Наполнение Boolean, если DA/DO - true - есть координаты, false - координаты отсутствуют */
		valueColumn.setCellValueFactory(p-> new SimpleBooleanProperty(((IECObject) p.getValue().getValue()).getLayoutX()!=null));

		valueColumn.setCellFactory(p -> new CheckBoxTreeTableCell<Object, Boolean>(){
			@Override
			public void updateItem(Boolean item, boolean empty) {
				TreeItem<Object> currentItem = getTreeTableRow().getTreeItem();
				IECObject object = currentItem!=null && (currentItem.getValue().getClass()==DA.class || currentItem.getValue().getClass()==DO.class) ? (IECObject) currentItem.getValue() : null;
				/* Если это DA/DO - добавляем CheckBox */
				if(object!=null){
					SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(item);
					booleanProperty.addListener((o, ov, nv) -> {
						if(nv) { object.setLayoutX(1.0); object.setLayoutY(1.0); } else { object.setLayoutX(null); object.setLayoutY(null); }
					});
					setSelectedStateCallback(p -> booleanProperty);
					super.updateItem(item, empty);
				} else super.updateItem(null, true);
			}
		});
	}

	/** Вызов во время открытия */
	private void onShowing(){
		titledPane.setText("Коннекторы - " + graphicNode.getIecObject().getName());
		itemsDOBuffer = new ArrayList<>(itemsDOList);
		itemsDABuffer = new ArrayList<>(itemsDAList);

		rootItem_L.getChildren().clear();
		rootItem_R.getChildren().clear();
		rootItem_L.setExpanded(true);
		rootItem_R.setExpanded(true);

		/* Наполнение таблицы параметрами */
		for(Connector c: graphicNode.getConnectors()){
			TreeItem<Object> item = getItem(c.getIecObject());
			if(c.getPosition()== ConnectorPosition.left) rootItem_L.getChildren().add(item);
			else rootItem_R.getChildren().add(item);
		}
	}

	/** Вызов во время закрытия */
	private void onHiding(){ graphicNode.refreshConnectors(); graphicNode = null; }

	/** Взять элемент из буффера */
	public TreeItem<Object> getItem(Object dataObject){
		TreeItem<Object> item = null;
		if(dataObject.getClass()==DO.class) { item = itemsDOBuffer.get(0); itemsDOBuffer.remove(item); }
		else if(dataObject.getClass()==DA.class) { item = itemsDABuffer.get(0); itemsDABuffer.remove(item); }
		item.getChildren().clear();
		item.setValue(dataObject);
		return item;
	}

	/** Показать окно */
	public static void show(GraphicNode graphicNode) { self.graphicNode = graphicNode; self.stage.showAndWait(); }

	/** Скрыть окно */
	@FXML public void close(){ self.stage.hide(); }
}
