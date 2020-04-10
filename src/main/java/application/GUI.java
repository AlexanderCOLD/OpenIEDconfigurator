package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import controllers.*;
import controllers.dialogs.AssistDialog;
import controllers.dialogs.FileChooserDialog;
import controllers.dialogs.InfoDialog;
import controllers.dialogs.LibraryDialog;
import iec61850.IED;
import iec61850.objects.SCL;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import iec61850.IEDExtractor;
import tools.ProjectLogger;
import tools.Settings;
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

	@FXML private Accordion structAccord;
	@FXML private MenuBar menuBar;
	@FXML private Label zoomLabel;
	@FXML private TabPane tabPane;
	@FXML private TreeView tree;
	@FXML private SplitPane splitPaneH, splitPaneV;
	@FXML private TextFlow messageArea;
	@FXML private ScrollPane messageScrollPane;


	public GUI() {
		ProjectLogger.enable();
		self = this;

		URL url = Main.class.getResource("/");

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/view/FXML/MainWindow.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try { loader.load(); } catch (IOException e) { e.printStackTrace(); }
		Scene scene = new Scene(this);
		scene.getStylesheets().add("view/CSS/stylesheet.css");
		scene.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.CONTROL) ctrl = true; });
		scene.setOnKeyReleased(e->{ if (e.getCode() == KeyCode.CONTROL) ctrl = false; });
		stage = new Stage(StageStyle.UNDECORATED);
		stage.setTitle("OpenIEDconfigurator");
		stage.setScene(scene);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/view/image/Icon.png")));
		ImageView icon = new ImageView(new Image(Main.class.getResource("/view/image/Icon.png").toString())); icon.setFitWidth(20); icon.setFitHeight(20); getChildren().add(icon); icon.setLayoutX(6); icon.setLayoutY(5);
		ResizeController.addStage(stage);
		menuBar.setOnMousePressed(e -> { xOffset = stage.getX() - e.getScreenX(); yOffset = stage.getY() - e.getScreenY(); });
		menuBar.setOnMouseDragged(e -> { stage.setX(e.getScreenX() + xOffset); stage.setY(e.getScreenY() + yOffset); });
		menuBar.setOnMouseClicked(e->{ if(e.getClickCount()==2) maximize(); });

		Platform.runLater(() -> Settings.load());
	}

	/**
	 * Открыть окно программы
	 */
	public static void show(){
		if(self==null) new GUI();
		self.stage.setMinWidth(1200);
		self.stage.setMinHeight(750);
		self.stage.show();

		SCL scl = SaveLoadObject.load(SCL.class, new File("Project.cid"));
		Optional.ofNullable(scl).ifPresent(ProjectController::setCID);
	}

	@FXML private void initialize() {
		PanelsController.setTabPane(tabPane);
		ContextMenuController.initializeContextMenu();
		ProjectController.setTree(tree);

		for (int i=0; i<15; i++) PanelsController.createTab("Вкладка "+i);

		structAccord.setExpandedPane(structAccord.getPanes().get(0));
		messageArea.setStyle("-fx-background-color: -fx-fourth-color; -fx-padding: 0 20 0 20");
		messageArea.heightProperty().addListener((o) -> messageScrollPane.setVvalue(0.99));

		writeMessage("Конфигуратор запущен");
	}

	public static void writeMessage(String message){ Text text = self.textBuffer(message); text.setFill(Color.WHITE);  self.messageArea.getChildren().add(text); ProjectLogger.fine(message); }
	public static void writeErrMessage(String message){ Text text = self.textBuffer(message); text.setFill(Color.web("#dc4b48")); self.messageArea.getChildren().add(text); ProjectLogger.warning(message); }
	private Text textBuffer(String message){ Text text; if(messageArea.getChildren().size()>500) { text = (Text) messageArea.getChildren().get(0); messageArea.getChildren().remove(text); } else text = new Text(); text.setText(String.format("%s    %s\n",self.dateFormat.format(new Date()), message)); return text; }

	private boolean exitRequest(){ return AssistDialog.requestConfirm("Подтверждение закрытия OpenIEDconfigurator", "Выйти из OpenIEDconfigurator?\nНесохраненные данные могут быть утеряны"); }

	@FXML private void handleNew() {
		if(!AssistDialog.requestConfirm("Подтвер","Создать новый проект?\nНесохраненные данные будут утеряны")) return;
//		CurrentProject.clear();
		SaveLoad.setFilePath(null);
	}
//	@FXML private void handleOpen() {
//		File file = FileChooserDialog.openCLDFile();
//		if (file != null) { SaveLoad.loadProjectDataFromFile(file);	}
//	}
	@FXML private void handleOpen(){
		File file = FileChooserDialog.openCIDFile();
		if (file != null) {
			SCL scl = SaveLoadObject.load(SCL.class, file);
			if(scl!=null){
//				SCL scl = SaveLoadObject.load(SCL.class, new File("Project.cid"));
//				Optional.ofNullable(scl).ifPresent(ProjectController::setSCL);
				ArrayList<IED> ieds = IEDExtractor.extractIEDList(scl);
				ProjectController.updateTree(ieds);
			}
			else{
				AssistDialog.requestError("Ошибка", "Невозможно открыть SCL\nВерсия SCL отличается от 2006");
				writeErrMessage("Невозможно открыть SCL, версия SCL отличается от 2006");
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
	@FXML private void switchInfo(){
		InfoDialog.switchVisibility();
	}
	@FXML private void switchLibrary(){
		LibraryDialog.switchVisibility();
	}
	@FXML private void minimize(){ stage.setIconified(true); }
	@FXML private void maximize(){ stage.setMaximized(!stage.isMaximized()); }

	@FXML private void close() { if(exitRequest()){ Settings.save(); Platform.exit(); System.exit(0); } }

	public static GUI get() { return self; }
	public static boolean isCtrl(){ return self.ctrl; }
	public static Label getZoomLabel() { return self.zoomLabel; }
	public static Stage getStage() { return self.stage; }
	public SplitPane getSplitPaneH() { return splitPaneH; }
	public SplitPane getSplitPaneV() { return splitPaneV; }
	public TabPane getTabPane() { return tabPane; }
}
