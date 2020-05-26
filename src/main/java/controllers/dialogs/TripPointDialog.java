package controllers.dialogs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import application.GUI;
import application.Main;
import controllers.ResizeController;
import iec61850.DA;
import iec61850.DO;
import iec61850.LN;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Редактор уставок
 */
public class TripPointDialog extends AnchorPane{

	private static final TripPointDialog self = new TripPointDialog();
	private final Image lnIcon = new Image(Main.class.getResource("/view/image/LNIcon.png").toString());
	private final Image doIcon = new Image(Main.class.getResource("/view/image/DOIcon.png").toString());
	private final Image daIcon = new Image(Main.class.getResource("/view/image/DAIcon.png").toString());

	private boolean draggable = false;
	private double dragOffsetX, dragOffsetY; // поправка при перетаскивании на позицию мышки

	private final Stage stage = new Stage();

	@FXML private Accordion accordion;
	@FXML private TitledPane titledPane;
	@FXML private TreeTableView<Object> treeTableView;
	@FXML private TreeTableColumn<Object,String> nameColumn, typeColumn, descColumn, valueColumn, minColumn, maxColumn, stepColumn;

	/* Корневой элемент LogicalNode */
	private final TreeItem<Object> rootItem = new TreeItem<Object>(){{
		setGraphic(new ImageView(lnIcon){{ setFitWidth(20); setFitHeight(20); }});
	}};

	/* Буффер из вложений DO / DA */
	private ArrayList<TreeItem<Object>> itemsDOBuffer, itemsDABuffer;
	private final ArrayList<TreeItem<Object>> itemsDOList = new ArrayList<TreeItem<Object>>(){{
		IntStream.range(0, 50).forEach(i -> this.add(new TreeItem<Object>(){{
			ImageView iv = new ImageView(doIcon){{ setFitHeight(20); setFitWidth(20); }}; setGraphic(iv); setExpanded(true);
		}} ));
	}};
	private final ArrayList<TreeItem<Object>> itemsDAList = new ArrayList<TreeItem<Object>>(){{
		IntStream.range(0, 200).forEach(i -> this.add(new TreeItem<Object>(){{
			ImageView iv = new ImageView(daIcon){{ setFitHeight(20); setFitWidth(20); }}; setGraphic(iv); setExpanded(true);
		}} ));
	}};
	/* Логический узел и Уставки */
	private LN logicalNode;
	private final ArrayList<Object> objectList = new ArrayList<>();

	public TripPointDialog() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/view/FXML/TripPointDialog.fxml"));
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

		/* Редактирование по однократноу нажатию */
		treeTableView.setOnMouseClicked(e -> { editCurrentCell(); e.consume(); });

		/* Редактирование по изменению фокуса */
		treeTableView.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> editCurrentCell());

		/* Фокусировка по нажатию кнопок */
		scene.addEventFilter(KeyEvent.KEY_PRESSED, e-> {
			if(e.getCode()==KeyCode.UP || e.getCode()==KeyCode.DOWN || e.getCode()==KeyCode.ENTER) treeTableView.requestFocus();
		});
	}

	@FXML
	private void initialize() {
		stage.setMinWidth(900); stage.setMinHeight(300);
		stage.setWidth(900); stage.setHeight(300);
		stage.setOnShowing(event -> onShowing()); stage.setOnHiding(event -> onHiding());
		accordion.setExpandedPane(titledPane);
		treeTableView.setRoot(rootItem);
		treeTableView.setEditable(true);

		/* Внесение данных из объекта в таблицу */
		nameColumn.setCellValueFactory(p -> {
			if(p.getValue().getValue().getClass()==LN.class) return new SimpleStringProperty(((LN) p.getValue().getValue()).getName());
			if(p.getValue().getValue().getClass()==DO.class) return new SimpleStringProperty(((DO) p.getValue().getValue()).getName().replaceAll("set_",""));
			if(p.getValue().getValue().getClass()==DA.class) return new SimpleStringProperty(((DA) p.getValue().getValue()).getName().replaceAll("set_",""));
			return null;
		});
		typeColumn.setCellValueFactory(p -> {
			if(p.getValue().getValue().getClass()==LN.class) return new SimpleStringProperty(((LN) p.getValue().getValue()).getType());
			if(p.getValue().getValue().getClass()==DO.class) return new SimpleStringProperty(((DO) p.getValue().getValue()).getType().replaceAll("iec_",""));
			if(p.getValue().getValue().getClass()==DA.class) return new SimpleStringProperty(((DA) p.getValue().getValue()).getType().replaceAll("iec_",""));
			return null;
		});
		descColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
		valueColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("value"));
		minColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("minValue"));
		maxColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("maxValue"));
		stepColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("step"));

//		nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getName()));

		/* Тип объекта в таблице (TextField) */
//		valueColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
		valueColumn.setCellFactory(param -> new TextFieldTreeTableCell<Object, String>(new DefaultStringConverter()){
			@Override
			public void updateItem(String item, boolean empty) {
				TreeItem<Object> currentItem = getTreeTableRow().getTreeItem();

				/* Если это DA - делаем ячейку редактируемой */
				if(currentItem != null && currentItem.getValue().getClass()==DA.class) super.updateItem(item, empty);
				else super.updateItem(null, true);
			}

			{
				/* Горячие клавиши для навигации */
				addEventFilter(KeyEvent.KEY_PRESSED, event -> {
					if (event.getCode() == KeyCode.ESCAPE) { cancelEdit(); event.consume(); }
					else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.LEFT) {
						cancelEdit();	treeTableView.getSelectionModel().selectAboveCell(); editCurrentCell(); event.consume();
					}
					else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.RIGHT) {
						cancelEdit(); treeTableView.getSelectionModel().selectBelowCell(); editCurrentCell(); event.consume();
					}
				});
			}

		});

		/* Вызывается при внесении изменений */
		valueColumn.setOnEditCommit(event -> {
			DA da = (DA) event.getRowValue().getValue();
			String doN = da.getCppType();
			String value = event.getNewValue();
			if(doN.contains("float") || doN.contains("double")) value = parseNumber(Double.class, value);
			if(doN.contains("int") || doN.contains("long") || doN.contains("short")) value = parseNumber(Integer.class, value);
//			if(doN.contains("char")) value = parseNumber(Byte.class, value);
			da.setValue(value);
			event.getTreeTableView().refresh();
		});
	}

	/**
	 * Редактировать выделенный элемент
	 */
	private void editCurrentCell(){
		if(!treeTableView.getSelectionModel().getSelectedCells().isEmpty()){
			int row = treeTableView.getSelectionModel().getSelectedCells().get(0).getRow();
			Platform.runLater(() -> treeTableView.edit(row, valueColumn));
		}
	}

	/**
	 * Парсинг чисел
	 */
	private static <T> String parseNumber(Class<T> type, String value){
		value = value.replaceAll(" ","").replaceAll(",",".");
		try {
			if(type==Double.class) return String.valueOf(Double.parseDouble(value));
			if(type==Integer.class) return String.valueOf(Integer.parseInt(value));
			if(type==Byte.class) return String.valueOf(Byte.parseByte(value));
		}
		catch (Exception e) {
			if(type==Double.class) return String.valueOf(new Double(0.0));
			if(type==Integer.class) return String.valueOf(new Integer(0));
			if(type==Byte.class) return String.valueOf(new Byte("0"));
		}
		return "error";
	}

	/**
	 * Вызов во время открытия
	 */
	private void onShowing(){
		titledPane.setText("Параметры - " + logicalNode.getName());
		itemsDOBuffer = new ArrayList<>(itemsDOList);
		itemsDABuffer = new ArrayList<>(itemsDAList);
		for(TreeItem<Object> item: itemsDOBuffer) item.getChildren().clear();
		for(TreeItem<Object> item: itemsDABuffer) item.getChildren().clear();
		rootItem.getChildren().clear();

		rootItem.setValue(logicalNode); rootItem.setExpanded(true);

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
		for(DO doCont:dataObject.getContent()){ TreeItem<Object> doItem = getDOItem(doCont); doItem.setExpanded(true); root.getChildren().add(doItem); }
		for(DA daCont:dataObject.getAttributes()){ TreeItem<Object> daItem = getDAItem(daCont); root.setExpanded(true); root.getChildren().add(daItem); }

		for(TreeItem<Object> doItem:root.getChildren())
			if(doItem.getValue().getClass()==DO.class) {
				DO doCont = (DO) doItem.getValue();
				if(!doCont.getAttributes().isEmpty() || !doCont.getContent().isEmpty()) fillItem(doItem, doCont);
			}
	}

	/**
	 * Вызов во время закрытия
	 */
	private void onHiding(){ }

	/**
	 * Взять элемент из буффера
	 */
	public TreeItem<Object> getDOItem(Object dataObject){ TreeItem<Object> item = itemsDOBuffer.get(0); itemsDOBuffer.remove(item); item.setValue(dataObject); return item; }
	public TreeItem<Object> getDAItem(Object dataObject){ TreeItem<Object> item = itemsDABuffer.get(0); itemsDABuffer.remove(item); item.setValue(dataObject); return item; }

	/**
	 * Показать окно
	 */
	public static void show(LN logicalNode) {

		List<DO> doList = logicalNode.getDataObjects().stream()
				.filter(aDo -> aDo.getCppName().contains("set_"))
				.collect(Collectors.toList());
		List<DA> daList = logicalNode.getAttributes().stream()
				.filter(aDo -> aDo.getCppName().contains("set_"))
				.collect(Collectors.toList());

		self.objectList.clear(); self.objectList.addAll(doList); self.objectList.addAll(daList);
		self.logicalNode = logicalNode;
		self.stage.showAndWait();
	}

	/**
	 * Скрыть окно
	 */
	@FXML public void close(){ self.stage.hide(); }
}
