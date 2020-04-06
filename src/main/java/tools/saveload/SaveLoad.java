package tools.saveload;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import application.GUI;
import controllers.dialogs.AssistDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Класс для сохранения/загрузки проекта
 */
public class SaveLoad {
	private static File filePath=null;

	public static void loadProjectDataFromFile(File file) {
		if(!AssistDialog.requestConfirm("Подтверждение загрузки проекта","Загрузить новый проект?\nНесохраненные данные будут утеряны")) return;
	    try {
	        JAXBContext context = JAXBContext.newInstance(ProjectWrapper.class);
	        Unmarshaller um = context.createUnmarshaller();

	        ProjectWrapper p = (ProjectWrapper) um.unmarshal(file);
	        p.setCurrentProject();
	        GUI.writeMessage("Загрузка проекта "+file);

	        setFilePath(file);
	    } catch (Exception e) {
	    	if(file.exists()){
	    		Alert alert = new Alert(AlertType.ERROR);
	    		alert.setTitle("Ошибка");
	    		alert.setHeaderText("Невозможно загрузить проект");
	    		alert.setContentText("Невозможно загрузить проект из файла:" + file.getPath());
	    		alert.showAndWait();
	    	}
	    }
	}

	public static void saveProjectDataToFile(File file) {
	    try {
	        JAXBContext context = JAXBContext.newInstance(ProjectWrapper.class);
	        Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	        // Маршаллируем и сохраняем XML в файл.
	        ProjectWrapper p = new ProjectWrapper();
	        p.getCurrentProject();
	        m.marshal(p, file);

	        setFilePath(file);
	        GUI.writeMessage("Проект успешно сохранен:  "+file);
	    } catch (Exception e) { e.printStackTrace();
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Ошибка");
	        alert.setHeaderText("Невозможно сохранить проект");
	        alert.setContentText("Невозможно сохранить проект в файл:" + file.getPath());
	        alert.showAndWait();
	    }
	}

	public static File getFilePath() {
		return filePath;
	}
	public static void setFilePath(File file) {
		filePath = file;
	}

}
