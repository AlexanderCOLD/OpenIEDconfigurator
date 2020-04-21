package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

import controllers.*;
import controllers.dialogs.AssistDialog;
import controllers.dialogs.FileChooserDialog;
import controllers.dialogs.InfoDialog;
import controllers.library.LibraryDialog;
import controllers.tree.TreeController;
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
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tools.ProjectLogger;
import tools.Settings;
import tools.SaveLoadObject;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Основное окно программы
 */
public class GUI extends AnchorPane{

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MMMM.yyyy HH.mm.ss");
	public static final String colorStyle = "blueStyle"; //Name of .css (blackStyle, blueStyle,...)
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
		FXMLLoader loader = new FXMLLoader();
//		loader.setResources(ResourceBundle.getBundle("i18n/oic", new UTF8Control()));
		loader.setLocation(Main.class.getResource("/view/FXML/MainWindow.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try { loader.load(); } catch (IOException e) { e.printStackTrace(); }
		Scene scene = new Scene(this);
		scene.getStylesheets().add("view/CSS/" + colorStyle + ".css");
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

		Platform.runLater(Settings::load);
		openLastProject();
	}

	/**
	 * Открыть окно программы
	 */
	public static void show(){
		if(self==null) new GUI();
		self.stage.setMinWidth(1200);
		self.stage.setMinHeight(750);
		self.stage.show();
	}

	@FXML private void initialize() {
		PanelsController.setTabPane(tabPane);
		ContextMenuController.initializeContextMenu();
		TreeController.setTree(tree);

		PanelsController.createTab("Project");

		structAccord.setExpandedPane(structAccord.getPanes().get(0));
		messageArea.setStyle("-fx-background-color: -fx-fourth-color; -fx-padding: 0 20 0 20");
		messageArea.heightProperty().addListener((o) -> messageScrollPane.setVvalue(0.99));

		writeMessage("Конфигуратор запущен");
	}

	public static void writeMessage(String message){ Text text = self.textBuffer(message); text.setFill(Color.WHITE);  self.messageArea.getChildren().add(text); ProjectLogger.fine(message); }
	public static void writeErrMessage(String message){ Text text = self.textBuffer(message); text.setFill(Color.web("#dc4b48")); self.messageArea.getChildren().add(text); ProjectLogger.warning(message); }
	private Text textBuffer(String message){ Text text; if(messageArea.getChildren().size()>500) { text = (Text) messageArea.getChildren().get(0); messageArea.getChildren().remove(text); } else text = new Text(); text.setText(String.format("%s    %s\n",self.dateFormat.format(new Date()), message)); return text; }

	private boolean exitRequest(){ return AssistDialog.requestConfirm("Подтверждение закрытия OpenIEDconfigurator", "Выйти из OpenIEDconfigurator?\nНесохраненные данные могут быть утеряны"); }

	/**
	 * Загрузить предудущий проект
	 */
	private void openLastProject(){
		new Thread(() -> {
			try { Thread.sleep(2000); } catch (Exception ignored) {}
			if(Settings.lastCIDPath!=null) {
				File file = new File(Settings.lastCIDPath);
				if(file.exists()){
					Platform.runLater(() -> {
						writeMessage("Найден предыдущий проект: " + file.getPath());
						if(!AssistDialog.requestConfirm("Найден предыдущий проект", String.format("Хотите открыть предыдущий проект? \n%s", file.getPath()))) return;
						SCL scl = SaveLoadObject.load(SCL.class, file);
						if(scl!=null) { ProjectController.setScl(scl); ProjectController.setFileCID(file); }
					});
				}
			}
		}){{ start(); }};
	}

	@FXML private void handleNew() {
		if(!AssistDialog.requestConfirm("Подтвер","Создать новый проект?\nНесохраненные данные будут утеряны")) return;
		handleOpen();
	}

	@FXML private void handleOpen(){
		File file = FileChooserDialog.openCIDFile();
		if (file != null) {
			SCL scl = SaveLoadObject.load(SCL.class, file);
			if(scl!=null) {
				ProjectController.setScl(scl);
				ProjectController.setFileCID(file);
			}
			else{
				AssistDialog.requestError("Ошибка", "Невозможно открыть SCL\nВерсия SCL отличается от 2006");
				writeErrMessage("Невозможно открыть SCL, версия SCL отличается от 2006");
			}
		}
	}

	@FXML private void handleSave() {
		if(ProjectController.getCld()==null) { GUI.writeErrMessage("Нечего сохранять"); return; }
		File cldFile = ProjectController.getFileCLD();
		if (cldFile != null) SaveLoadObject.save(ProjectController.getFileCLD(), cldFile); else handleSaveAs();
	}

	@FXML public void handleSaveAs() {
		if(ProjectController.getCld()==null) { GUI.writeErrMessage("Нечего сохранять"); return; }
		File file = FileChooserDialog.saveCLDFile();
		if (file != null) {
			String path = file.getParent();
			String name = file.getName().replaceAll(".cld","");
			File newDir = new File(path+"\\"+name); if(!newDir.exists()) newDir.mkdirs();

			File newCIDFile = new File(path + "\\" + name + "\\" + name + ".cid");
			File oldCIDFile = ProjectController.getFileCID();
			try { Files.copy(oldCIDFile.toPath(), newCIDFile.toPath(), StandardCopyOption.REPLACE_EXISTING); } catch (IOException e) { e.printStackTrace();	}

			File newCLDFile = new File(path + "\\" + name + "\\" + name + ".cld");
			SaveLoadObject.save(ProjectController.getCld(), newCLDFile);

			ProjectController.setFileCID(newCIDFile);
			ProjectController.setFileCLD(newCLDFile);
		}
	}

	@FXML private void switchInfo(){ InfoDialog.switchVisibility();	}
	@FXML private void switchLibrary(){ LibraryDialog.switchVisibility(); }
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
