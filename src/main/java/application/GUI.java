package application;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import controllers.*;
import controllers.dialogs.Alerts;
import controllers.dialogs.FileChooserDialog;
import controllers.dialogs.LibraryDialog;
import controllers.elements.GraphicNode;
import iec61850.IED;
import iec61850.objects.SCL;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import iec61850.IEDExtract;
import tools.saveload.SaveLoad;
import tools.saveload.SaveLoadObject;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Основное окно программы
 */
public class GUI extends AnchorPane{

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MMMM.yyyy HH.mm.ss");
	private Stage stage;
	private boolean ctrl = false;
	private static GUI self;
	private double xOffset, yOffset;
	@FXML private MenuBar menuBar;
	@FXML private AnchorPane infoPane, libraryPane;
	@FXML private Label zoomLabel;
	@FXML private TabPane tabPane;
	@FXML private TextArea messageArea;
	@FXML private TreeView iecTree;

	public GUI() {
		self = this;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/view/FXML/MainWindow.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try { loader.load(); } catch (IOException e) { e.printStackTrace(); }
		Scene mainScene = new Scene(this);
		mainScene.getStylesheets().add("view/CSS/stylesheet.css");
		mainScene.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.CONTROL) ctrl = true; });
		mainScene.setOnKeyReleased(e->{ if (e.getCode() == KeyCode.CONTROL) ctrl = false; });
		stage = new Stage(StageStyle.UNDECORATED);
		stage.setTitle("OpenIEDconfigurator");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/view/image/Icon.png")));
		stage.setAlwaysOnTop(false);
		stage.setScene(mainScene);
		stage.setOnCloseRequest(e -> { if(exitRequest()){ Platform.exit(); System.exit(0); } else e.consume(); });
		StageResize.addResizeListener(stage);
		menuBar.setOnMousePressed(e -> { xOffset = stage.getX() - e.getScreenX(); yOffset = stage.getY() - e.getScreenY(); });
		menuBar.setOnMouseDragged(e -> { stage.setX(e.getScreenX() + xOffset); stage.setY(e.getScreenY() + yOffset); });
		menuBar.setOnMouseClicked(e->{ if(e.getClickCount()==2) maximize(); });
	}

	/**
	 * Открыть окно программы
	 */
	public static void showGUI(){
		if(self==null) new GUI();
		self.stage.setMinWidth(1200); self.stage.setWidth(1200);
		self.stage.setMinHeight(750); self.stage.setHeight(750);
		self.stage.show();
	}


	@FXML private void initialize() {
		PanelsController.setTabPane(tabPane);
		ContextMenuController.initializeContextMenu();
		LibraryController.get().initialize();
		TreeController.setTree(iecTree);

		PanelsController.addTab("Test 1");
		PanelsController.addTab("Test 2");

		GraphicNode gn = new GraphicNode();
		PanelsController.getCurrentPanel().getChildren().add(gn);
		gn.relocate(100,100);
	}

	@FXML private void handleNew() {
		if(!Alerts.requestDialog("Подтверждение создания нового проекта","Создать новый проект?","Несохраненные данные будут утеряны")) return;
//		CurrentProject.clear();
		SaveLoad.setFilePath(null);
	}
	@FXML private void handleOpen() {
		File file = FileChooserDialog.openCLDFile();
		if (file != null) { SaveLoad.loadProjectDataFromFile(file);	}
	}
	@FXML private void handleOpenCID(){
		File file = FileChooserDialog.openCIDFile();
		if (file != null) {
			SCL scl = SaveLoadObject.load(SCL.class, file);
			if(scl!=null){
				ArrayList<IED> ieds = IEDExtract.extractIEDList(scl);
				TreeController.updateTree(ieds);
			}
			else{
				Alerts.errorDialog("Ошибка", "Невозможно открыть SCL", "Версия SCL отличается от 2006");
			}
		}
	}
	@FXML private void handleSave() {
		File projectFile = SaveLoad.getFilePath();
		if (projectFile != null) { SaveLoad.saveProjectDataToFile(projectFile); }
		else { handleSaveAs(); }
	}
	@FXML public void handleSaveAs() {
		File file = FileChooserDialog.saveCLDFile();
		if (file != null) {
			if (!file.getPath().endsWith(".cld")) { file = new File(file.getPath() + ".cld"); }
			SaveLoad.saveProjectDataToFile(file);
		}
	}
	@FXML private void showLibrary(){ LibraryDialog.show(); }
	@FXML private void minimize(){ stage.setIconified(true); }
	@FXML private void maximize(){ stage.setMaximized(!stage.isMaximized()); }
	@FXML private void close() { if(exitRequest()){ Platform.exit(); System.exit(0); } }


	public static void writeText(String text){ Platform.runLater(() -> { self.messageArea.appendText(self.dateFormat.format(new Date())+"    Сообщение:    "+text+"\n"); }); }
	private boolean exitRequest(){ return Alerts.requestDialog("Подтверждение закрытия OpenIEDconfigurator", "Выйти из OpenIEDconfigurator?", "Несохраненные данные могут быть утеряны"); }

	public static GUI get() { return self; }
	public static boolean isCtrl(){ return self.ctrl; }
	public static Label getZoomLabel() { return self.zoomLabel; }
	public static Stage getStage() { return self.stage; }
}
