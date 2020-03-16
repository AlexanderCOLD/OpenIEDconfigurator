package controllers.dialogs;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Редактор параметров элемента
 */
public class EditDialog extends AnchorPane{

	private static EditDialog self;
	private Stage stage;

	@FXML
	private TableView<Object> tableOptions;
	@FXML private TableColumn<Object, String> name;
	@FXML private TableColumn<Object, String> description;
	@FXML private TableColumn<Object, String> value;
	@FXML private TableColumn<Object, String> unit;

	public EditDialog() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/view/FXML/EditDialog.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try { loader.load(); } catch (IOException e) { e.printStackTrace();	}
		stage = new Stage();
		stage.setTitle("Параметры элемента... ");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/view/image/Icon.png")));
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setMinWidth(700);
		stage.setMinHeight(330);
		stage.setResizable(false);
		Scene scene = new Scene(this);
		scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> { if(key.getCode()==KeyCode.ENTER) saveAndClose(); if(key.getCode()==KeyCode.ESCAPE) cancelAndClose(); });
		stage.setScene(scene);
	}


	@FXML
	private void initialize() {

	}

	@FXML public void okHandle(){ saveAndClose(); }
	@FXML public void cancelHandle(){ cancelAndClose(); }


	public static void show(Object obj){
		if(self==null) self = new EditDialog();


		self.stage.showAndWait();
	}

	public void cancelAndClose(){


		stage.hide();
	}

	public void saveAndClose(){


		stage.hide();
	}
}
