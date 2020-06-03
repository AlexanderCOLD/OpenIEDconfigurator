package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import tools.Settings;

public class Main extends Application {

	public static void main(String[] args) { launch(args); }

	@Override
	public void start(Stage stage) { GUI.show(); Platform.runLater(Settings::load); }
}
