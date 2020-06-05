package controllers.dialogs;

import application.GUI;
import application.Main;
import controllers.ResizeController;
import controllers.graphicNode.ConnectorPosition;
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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Редактор сигналов осциллограммы
 */
public class OscillDialog extends AnchorPane{

	private static final OscillDialog self = new OscillDialog();
	private static final Image iedIcon, ldIcon, lnIcon, dsIcon, doIcon, daIcon, dpIcon;

	private boolean draggable = false;
	private double dragOffsetX, dragOffsetY;
	private final Stage stage = new Stage();

	@FXML private Accordion accordion;
	@FXML private TitledPane titledPane;
	@FXML private TreeTableView<Object> treeTableView;
	@FXML private TreeTableColumn<Object,String> nameColumn;
	@FXML private TreeTableColumn<Object,Boolean> valueColumn;

	private final TreeItem<Object> rootItem = new TreeItem<>();
	private final CheckBox rootCB = new CheckBox();

	private static final HashMap<String, ArrayList<TreeItem<Object>>> branchBuffer = new HashMap<>();
	private static HashMap<String, ArrayList<TreeItem<Object>>> branchTemp;

	static {
		iedIcon = new Image(Main.class.getResource("/view/image/IEDIcon.png").toString()); fillBuffer(10, "IED", iedIcon);
		ldIcon = new Image(Main.class.getResource("/view/image/LDIcon.png").toString());   fillBuffer(20, "LD", ldIcon);
		lnIcon = new Image(Main.class.getResource("/view/image/LNIcon.png").toString());   fillBuffer(500, "LN", lnIcon);
		dsIcon = new Image(Main.class.getResource("/view/image/DSIcon.png").toString());   fillBuffer(500, "DS", dsIcon);
		doIcon = new Image(Main.class.getResource("/view/image/DOIcon.png").toString());   fillBuffer(10000, "DO", doIcon);
		daIcon = new Image(Main.class.getResource("/view/image/DAIcon.png").toString());   fillBuffer(10000, "DA", daIcon);
		dpIcon = new Image(Main.class.getResource("/view/image/DPIcon.png").toString());   fillBuffer(10000, "IECProperty", dpIcon);
	}

	/** Создание буффера */
	static void fillBuffer(int numb, String name, Image icon){
		ArrayList<TreeItem<Object>> buffer = new ArrayList<>();
		IntStream.range(0, numb).forEach(v ->{
			ImageView iv = new ImageView(icon); iv.setFitWidth(20); iv.setFitHeight(20);
			TreeItem<Object> item = new TreeItem<>(); item.setGraphic(iv);
			buffer.add(item);
		});
		branchBuffer.put(name, buffer);
	}

	/* Текущий корневой элемент (LD) */
	private IECObject iecObject;

	public OscillDialog() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/view/FXML/OscillDialog.fxml"));
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
		stage.setMinWidth(400); stage.setMinHeight(350);
		stage.setWidth(400); stage.setHeight(350);
		stage.setOnShowing(event -> onShowing()); stage.setOnHiding(event -> onHiding());
		accordion.setExpandedPane(titledPane);
		treeTableView.setRoot(rootItem);
		treeTableView.setEditable(true);

		treeTableView.setPlaceholder(new Label("Данные отсутствуют"));

		/* Установка групповых CheckBox */
		rootCB.selectedProperty().addListener((o, ov, nv) -> {
			if(iecObject ==null) return;
			CLDUtils.objectListOf(iecObject).stream()
					.filter(obj-> obj.getClass()==DA.class)
					.forEach(obj-> { if(nv) addTag(obj); else removeTag(obj); } );
			treeTableView.refresh();
		});
		valueColumn.setText(null); valueColumn.setGraphic(rootCB);
//		Platform.runLater(() -> { rootCB.setSelected(false); rootCB.fire(); });


		/* Наполнение названиями */
		nameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));
		/* Наполнение Boolean, если DA/DO - true - есть координаты, false - координаты отсутствуют */
		valueColumn.setCellValueFactory(p-> new SimpleBooleanProperty(((IECObject) p.getValue().getValue()).getTags().contains("oscillogram")));

		valueColumn.setCellFactory(p -> new CheckBoxTreeTableCell<Object, Boolean>(){
			@Override
			public void updateItem(Boolean item, boolean empty) {
				TreeItem<Object> currentItem = getTreeTableRow().getTreeItem();
				IECObject object = currentItem!=null && (currentItem.getValue().getClass()==DA.class) ? (IECObject) currentItem.getValue() : null;
				/* Если это DA - добавляем CheckBox */
				if(object!=null){
					SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(item);
					booleanProperty.addListener((o, ov, nv) -> { if(nv) addTag(object); else removeTag(object); });
					setSelectedStateCallback(p -> booleanProperty);
					super.updateItem(item, empty);
				} else super.updateItem(null, true);
			}
		});

	}

	private static void addTag(IECObject iecObject){ if(!iecObject.getTags().contains("oscillogram")) iecObject.getTags().add("oscillogram"); }
	private static void removeTag(IECObject iecObject){ iecObject.getTags().remove("oscillogram"); }

	/** Вызов во время открытия */
	private void onShowing(){
		branchTemp = new HashMap<>();
		for(Map.Entry<String, ArrayList<TreeItem<Object>>> entry: branchBuffer.entrySet())
			branchTemp.put(entry.getKey(), new ArrayList<>(entry.getValue()));

		titledPane.setText("Сигналы - " + iecObject.getName());

		rootItem.getChildren().clear();
		rootItem.setExpanded(true);
		fillTree(rootItem, iecObject.getChildren());
	}

	/** Вызов во время закрытия */
	private void onHiding(){ }

	/** Наполнить дерево объектами */
	private static void fillTree(TreeItem<Object> root, ObservableList<IECObject> children){
		for(IECObject iecObject: children){
			TreeItem<Object> item = takeBranch(iecObject); root.getChildren().add(item); item.setExpanded(false);
			if(!iecObject.getChildren().isEmpty()) fillTree(item, iecObject.getChildren());
		}
	}

	/** Взять ветку из буффера */
	public static TreeItem<Object> takeBranch(Object iecObject){
		ArrayList<TreeItem<Object>> itemList = branchTemp.get(iecObject.getClass().getSimpleName());
		TreeItem<Object> item = itemList.get(0); item.setValue(iecObject); item.getChildren().clear();
		itemList.remove(0);
		return item;
	}

	/** Показать окно */
	public static void show(IECObject iecObject) {
		self.iecObject = CLDUtils.parentOf(LD.class, iecObject);
		if(self.iecObject!=null) self.stage.showAndWait();
	}

	/** Скрыть окно */
	@FXML public void close(){ self.stage.hide(); }
}
