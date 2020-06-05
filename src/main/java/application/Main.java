package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import tools.Settings;

public class Main extends Application {

	public static void main(String[] args) { launch(args); }

	@Override
	public void start(Stage stage) {

//		CLD cld = SaveLoadObject.load(CLD.class, new File("C:\\Users\\ALEXCOLD\\Desktop\\PTOCtest2\\PTOCtest2.cld"));
//		OscillDialog.show(cld.getIedList().get(0).getLogicalDeviceList().get(0).getLogicalNodeList().get(0));
		GUI.show(); Platform.runLater(Settings::load);
		}
}
