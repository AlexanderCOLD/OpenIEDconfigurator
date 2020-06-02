package application;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import controllers.*;
import controllers.dialogs.*;
import controllers.dialogs.library.LibraryDialog;
import controllers.tree.TreeController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tools.ProjectLogger;
import tools.Settings;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Основное окно программы
 */
public class GUI extends AnchorPane{

	public static final String colorStyle = "blueStyle"; //Name of .css (blackStyle, blueStyle,...)
	private static GUI self = new GUI();
	private static ContextMenu currentContextMenu;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MMMM.yyyy HH.mm.ss");
	private final Stage stage = new Stage(StageStyle.UNDECORATED);
	private double xOffset, yOffset;

	@FXML private Accordion structAccord;
	@FXML private MenuBar menuBar;
	@FXML private Label zoomLabel;
	@FXML private TabPane tabPane;
	@FXML private TreeView tree;
	@FXML private SplitPane splitPaneH, splitPaneV;
	@FXML private TextFlow messageArea;
	@FXML private ScrollPane messageScrollPane;
	@FXML private FlowPane buttonsPane;

	public GUI() {
		self = this;
		ProjectLogger.enable();
		FXMLLoader loader = new FXMLLoader();
//		loader.setResources(ResourceBundle.getBundle("i18n/oic", new UTF8Control()));
		loader.setLocation(Main.class.getResource("/view/FXML/MainWindow.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try { loader.load(); } catch (IOException e) { e.printStackTrace(); }
		Scene scene = new Scene(this);
		scene.getStylesheets().add("view/CSS/" + colorStyle + ".css");
		scene.getStylesheets().add("view/CSS/stylesheet.css");
		scene.setOnKeyPressed(e -> {
			if(e.isControlDown()){
				if(e.getCode()==KeyCode.O) handleOpen();
				if(e.getCode()==KeyCode.I) handleImportCLD();
				if(e.getCode()==KeyCode.S) handleSave();
				if(e.getCode()==KeyCode.W) close();
				if(e.getCode()==KeyCode.P) switchInfo();
				if(e.getCode()==KeyCode.L) switchLibrary();
			}
		});

		stage.setTitle("OpenIEDconfigurator");
		stage.setScene(scene);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/view/image/Icon.png")));
		ImageView icon = new ImageView(new Image(Main.class.getResource("/view/image/Icon.png").toString())); icon.setFitWidth(20); icon.setFitHeight(20); getChildren().add(icon); icon.setLayoutX(6); icon.setLayoutY(5);
		ResizeController.addStage(stage);
		menuBar.setOnMousePressed(e -> { xOffset = stage.getX() - e.getScreenX(); yOffset = stage.getY() - e.getScreenY(); });
		menuBar.setOnMouseDragged(e -> { stage.setX(e.getScreenX() + xOffset); stage.setY(e.getScreenY() + yOffset); });
		menuBar.setOnMouseClicked(e->{ if(e.getClickCount()==2) maximize(); });
		stage.maximizedProperty().addListener((o, ov, nv) -> { IECInfoDialog.setShowing(!nv); LibraryDialog.setShowing(!nv); });

		LibraryDialog.get();
		IECInfoDialog.get();

		ProjectController.openLastProject();
	}

	/**
	 * Открыть окно программы
	 */
	public static void show(){ self.stage.setMinWidth(1200); self.stage.setMinHeight(750); self.stage.show();	}

	@FXML private void initialize() {
		addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {if(currentContextMenu!=null) { currentContextMenu.hide(); currentContextMenu = null; }});
		addEventFilter(MouseEvent.MOUSE_ENTERED, e-> { stage.requestFocus(); });
		PanelsController.initialize(tabPane);
		TreeController.initialize(tree);
		ButtonsController.initialize(buttonsPane);

		PanelsController.createNewTab("Project", "PROJECT");

		structAccord.setExpandedPane(structAccord.getPanes().get(0));
		messageArea.setStyle("-fx-background-color: -fx-fourth-color; -fx-padding: 0 20 0 20");
		messageArea.heightProperty().addListener((o) -> messageScrollPane.setVvalue(1.99));

		writeMessage("Конфигуратор запущен");
	}

	public static void writeMessage(String message){ Text text = self.textBuffer(message); text.setFill(Color.WHITE);  self.messageArea.getChildren().add(text); ProjectLogger.fine(message); }
	public static void writeErrMessage(String message){ Text text = self.textBuffer(message); text.setFill(Color.web("#dc4b48")); self.messageArea.getChildren().add(text); ProjectLogger.warning(message); }
	private Text textBuffer(String message){ Text text; if(messageArea.getChildren().size()>500) { text = (Text) messageArea.getChildren().get(0); messageArea.getChildren().remove(text); } else text = new Text(); text.setText(String.format("%s    %s\n",self.dateFormat.format(new Date()), message)); return text; }

	private boolean exitRequest(){ return AssistantDialog.requestConfirm("Подтверждение закрытия OpenIEDconfigurator", "Выйти из OpenIEDconfigurator?\nНесохраненные данные могут быть утеряны"); }

	@FXML public void compilePRJ(){ System.out.println("Compile"); }
	@FXML public void handleOpen(){ if(!AssistantDialog.requestConfirm("Подтверждение","Создать новый проект?\nНесохраненные данные будут утеряны")) return;  ProjectController.openNewCID(FileChooserDialog.openCIDFile()); }
	@FXML public void handleImportCLD(){ if(ProjectController.fileCID==null) { GUI.writeErrMessage("CID отсутствует"); return; } if(!AssistantDialog.requestConfirm("Подтверждение","Импортировать CLD?\nНесохраненные данные будут утеряны")) return; ProjectController.importCLD(FileChooserDialog.openCLDFile()); }
	@FXML public void handleSave() { if(ProjectController.cld==null) { GUI.writeErrMessage("Проект отсутствует"); return; } if(ProjectController.fileCLD != null) ProjectController.saveProject(ProjectController.fileCLD); else handleSaveAs(); }
	@FXML public void handleSaveAs() { if(ProjectController.cld==null) { GUI.writeErrMessage("Проект отсутствует"); return; } ProjectController.saveProject(FileChooserDialog.saveCLDFile()); }
	@FXML public void switchInfo(){ IECInfoDialog.switchVisibility();	}
	@FXML public void switchLibrary(){ LibraryDialog.switchVisibility(); }
	@FXML public void aboutProgram(){ AboutProgramDialog.show(); }
	@FXML public void minimize(){ stage.setIconified(true); }
	@FXML public void maximize(){ stage.setMaximized(!stage.isMaximized()); }
	@FXML public void close() { if(exitRequest()){ Settings.save(); Platform.exit(); System.exit(0); } }

	public static GUI get() { return self; }
	public static Label getZoomLabel() { return self.zoomLabel; }
	public static Stage getStage() { return self.stage; }
	public SplitPane getSplitPaneH() { return splitPaneH; }
	public SplitPane getSplitPaneV() { return splitPaneV; }
	public TabPane getTabPane() { return tabPane; }
	public static ContextMenu getCurrentContextMenu() { return currentContextMenu; }
	public static void setCurrentContextMenu(ContextMenu currentContextMenu) { GUI.currentContextMenu = currentContextMenu; }
}
