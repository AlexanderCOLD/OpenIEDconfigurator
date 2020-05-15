package controllers.dialogs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import application.GUI;
import application.Main;
import controllers.ResizeController;
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
	private final Image doIcon = new Image(Main.class.getResource("/view/image/DOIcon.png").toString());

	private boolean draggable = false;
	private double dragOffsetX, dragOffsetY; // поправка при перетаскивании на позицию мышки

	private final Stage stage = new Stage();

	@FXML private Accordion accordion;
	@FXML private TitledPane titledPane;
	@FXML private TreeTableView<DO> treeTableView;
	@FXML private TreeTableColumn<DO,String> doColumn, daColumn, valueColumn;

	private final TreeItem<DO> rootItem = new TreeItem<>();

	/* Буффер из вложений */
	private final ArrayList<TreeItem<DO>> itemsList = new ArrayList<>();
	private ArrayList<TreeItem<DO>> itemsBuffer;

	/* Логический узел и Уставки */
	private LN logicalNode;
	private ArrayList<DO> dataObjects;


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

		addEventFilter(MouseEvent.MOUSE_PRESSED,e->{
			dragOffsetX = e.getScreenX() - stage.getX(); dragOffsetY = e.getScreenY() - stage.getY();
			draggable = !(dragOffsetY<7 || dragOffsetY>25 || dragOffsetX<7 || dragOffsetX>(stage.getWidth()-7)); // Границы перетаскивания
		});
		addEventFilter(MouseEvent.MOUSE_DRAGGED, e->{ if(draggable){ stage.setX(e.getScreenX() - this.dragOffsetX); stage.setY(e.getScreenY() - this.dragOffsetY); } });
	}

	@FXML
	private void initialize() {
		stage.setMinWidth(500); stage.setMinHeight(250);
		stage.setWidth(550); stage.setHeight(250);
		stage.setOnShowing(event -> onShowing()); stage.setOnHiding(event -> onHiding());
		accordion.setExpandedPane(titledPane);
		treeTableView.setRoot(rootItem);
		treeTableView.setEditable(true);

		/* Наполнение буффера строк */
		IntStream.range(0, 100).forEach(i -> itemsList.add(new TreeItem<DO>(){{
			ImageView iv = new ImageView(doIcon){{ setFitHeight(20); setFitWidth(20); }}; setGraphic(iv); setExpanded(true);
		}} ));

		daColumn.setStyle("-fx-alignment: CENTER;");
		valueColumn.setStyle("-fx-alignment: CENTER;");

		/* Внесение данных из объекта в таблицу */
		doColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getDataObjectName().replaceAll("iec_", "").toUpperCase()));
		daColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getDataAttributeName().replaceAll("[in_-out_]","")));
		valueColumn.setCellValueFactory(new TreeItemPropertyValueFactory<DO, String>("value"));

		/* Тип объекта в таблице (TextField) */
//		valueColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
		valueColumn.setCellFactory(param -> new TextFieldTreeTableCell<DO, String>(new DefaultStringConverter()){
			@Override
			public void updateItem(String item, boolean empty) {
				TreeItem<DO> currentItem = getTreeTableRow().getTreeItem();
				String doN = null;
				if(currentItem != null) doN = currentItem.getValue().getDataObjectName();
				/* Редактируемы только простые типы данных */
				if(doN != null && (
						doN.contains("float") ||
						doN.contains("double") ||
						doN.contains("int") ||
						doN.contains("long") ||
						doN.contains("short") ||
						doN.contains("char"))
				) super.updateItem(item, empty);
				else super.updateItem(null, true);
			}
		});

		/* Вызывается при внесении изменений */
		valueColumn.setOnEditCommit(event -> {
			DO dataObject = event.getRowValue().getValue();
			String doN = dataObject.getDataObjectName();
			String value = event.getNewValue();
			if(doN.contains("float") || doN.contains("double")) value = parseNumber(Double.class, value);
			if(doN.contains("int") || doN.contains("long") || doN.contains("short")) value = parseNumber(Integer.class, value);
			if(doN.contains("char")) value = parseNumber(Byte.class, value);
			dataObject.setValue(value);
			event.getTreeTableView().refresh();
		});

		/* Редактирование по однократноу нажатию */
		treeTableView.getSelectionModel().selectedItemProperty().addListener((o, os, newSelection) -> {
			if (newSelection != null) {
				int row = treeTableView.getSelectionModel().getSelectedCells().get(0).getRow();
				Platform.runLater(() -> treeTableView.edit(row, valueColumn));
			}
		});
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
		itemsBuffer = new ArrayList<>(itemsList);
		for(TreeItem<DO> item:itemsBuffer) item.getChildren().clear();
		rootItem.getChildren().clear();

		/* Наполнение таблицы параметрами */
		for(DO dataObject:dataObjects){
			TreeItem<DO> localRoot = getItem(dataObject);
			rootItem.getChildren().add(localRoot);
			fillItem(localRoot, dataObject.getContent());
		}

	}

	/**
	 * Наполнить верку (рекурсия)
	 */
	private void fillItem(TreeItem<DO> root, ArrayList<DO> dataObjects){
		if(dataObjects.isEmpty()) return;
		for(DO dataObject:dataObjects){	TreeItem<DO> item = getItem(dataObject); item.setExpanded(true); root.getChildren().add(item); }
		for(TreeItem<DO> doItem:root.getChildren()) fillItem(doItem, doItem.getValue().getContent());
	}

	/**
	 * Вызов во время закрытия
	 */
	private void onHiding(){ }

	/**
	 * Взять элемент из буффера
	 */
	public TreeItem<DO> getItem(DO dataObject){ TreeItem<DO> item = itemsBuffer.get(0); itemsBuffer.remove(item); item.setValue(dataObject); return item; }

	/**
	 * Показать окно
	 */
	public static void show(LN logicalNode) {
		self.dataObjects = new ArrayList<>(logicalNode.getDataSetInput().getDataObject().stream()
				.filter(aDo -> aDo.getDataAttributeName().contains("set_"))
				.collect(Collectors.toList()));
		self.logicalNode = logicalNode;
		self.stage.showAndWait();
	}

	/**
	 * Скрыть окно
	 */
	@FXML public void close(){ self.stage.hide(); }
}
