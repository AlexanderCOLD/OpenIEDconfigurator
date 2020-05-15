package application;
import controllers.dialogs.TripPointDialog;
import iec61850.DO;
import iec61850.LN;
import javafx.application.Application;
import javafx.stage.Stage;
import tools.SaveLoadObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {

	public static void main(String[] args) { launch(args); }

	@Override
	public void start(Stage stage) { GUI.show(); }
	
}
