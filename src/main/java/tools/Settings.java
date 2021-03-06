package tools;

import application.GUI;
import controllers.dialogs.IECInfoDialog;
import controllers.dialogs.InfoDialog;
import controllers.dialogs.library.LibraryDialog;
import javafx.application.Platform;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Настройки программы
 */

@XmlRootElement
public class Settings {

	public static String lastCIDPath;
	public static String lastCLDPath;

	public static double[] mainResolution; // размеры главного окна
	public static double[] mainLayout; // положение главного окна

	public static double[] infoResolution; // размеры информационного окна
	public static double[] infoLayout; // положение информационного окна
	public static boolean infoVisible; // видимость информационного окна
	public static boolean infoLock; // закремление информационного окна

	public static double[] libResolution; // размеры окна библиотеки
	public static double[] libLayout; // положение окна библиотеки
	public static boolean libVisible; // видимость окна библиотеки
	public static boolean libLock; // закрепление окна библиотеки

	public static double spHdivide; // положение горизонтальных окон
	public static double spVdivide; // положение вертикальных окон


	/**
	 * Загрузить настройки
	 * (При стерте программы)
	 */
	public static void loadSettings(){
		Platform.runLater(Settings::load);
//		new Thread(() -> { try { Thread.sleep(500); } catch (Exception ignored) {} 	}) {{ start(); }};
	}


	public static void load() {
		File sttng = new File("settings.xml");
		if(sttng.exists()){
			SaveLoadObject.load(Settings.class, new File("settings.xml"));

			GUI.getStage().setWidth(Settings.mainResolution[0]); GUI.getStage().setHeight(Settings.mainResolution[1]);
			GUI.getStage().setX(Settings.mainLayout[0]); GUI.getStage().setY(Settings.mainLayout[1]);

			IECInfoDialog.setResolution(infoResolution[0], infoResolution[1]);
			IECInfoDialog.setLayout(infoLayout[0], infoLayout[1]);
			IECInfoDialog.setShowing(infoVisible);
			IECInfoDialog.setLock(infoLock);

			LibraryDialog.setResolution(libResolution[0], libResolution[1]);
			LibraryDialog.setLayout(libLayout[0], libLayout[1]);
			LibraryDialog.setShowing(libVisible);
			LibraryDialog.setLock(libLock);

			GUI.get().getSplitPaneH().setDividerPositions(spHdivide);
			GUI.get().getSplitPaneV().setDividerPositions(spVdivide);
		}
	}

	public static void save() {
		mainResolution = new double[] { GUI.getStage().getWidth(), GUI.getStage().getHeight() };
		mainLayout = new double[] { GUI.getStage().getX(), GUI.getStage().getY() };

		infoResolution = IECInfoDialog.getResolution();
		infoLayout = IECInfoDialog.getLayout();
		infoVisible = IECInfoDialog.isShowing();
		infoLock = IECInfoDialog.isLock();

		libResolution = LibraryDialog.getResolution();
		libLayout = LibraryDialog.getLayout();
		libVisible = LibraryDialog.isShowing();
		libLock = LibraryDialog.isLock();

		spHdivide = GUI.get().getSplitPaneH().getDividerPositions()[0];
		spVdivide = GUI.get().getSplitPaneV().getDividerPositions()[0];

		SaveLoadObject.save(new Settings(), new File("settings.xml"));
	}

	public double[] getMainResolution() {return mainResolution;	}
	public void setMainResolution(double[] mainResolution) { Settings.mainResolution = mainResolution; }

	public double[] getMainLayout() { return mainLayout; }
	public void setMainLayout(double[] mainLayout) { Settings.mainLayout = mainLayout; }

	public double[] getInfoResolution() { return infoResolution; }
	public void setInfoResolution(double[] infoResolution) { Settings.infoResolution = infoResolution; }

	public double[] getInfoLayout() { return infoLayout; }
	public void setInfoLayout(double[] infoLayout) { Settings.infoLayout = infoLayout; }

	public boolean isInfoVisible() { return infoVisible; }
	public void setInfoVisible(boolean infoVisible) { Settings.infoVisible = infoVisible; }

	public boolean isInfoLock() { return infoLock; }
	public void setInfoLock(boolean infoLock) { Settings.infoLock = infoLock; }

	public double[] getLibResolution() {	return libResolution; }
	public void setLibResolution(double[] libResolution) { Settings.libResolution = libResolution; }

	public double[] getLibLayout() { return libLayout; }
	public void setLibLayout(double[] libLayout) { Settings.libLayout = libLayout; }

	public boolean isLibVisible() {	return libVisible; }
	public void setLibVisible(boolean libVisible) { Settings.libVisible = libVisible; }

	public boolean isLibLock() {	return libLock;	}
	public void setLibLock(boolean libLock) { Settings.libLock = libLock; }

	public double getSpHdivide() { return spHdivide; }
	public void setSpHdivide(double spHdivide) { Settings.spHdivide = spHdivide; }

	public double getSpVdivide() { return spVdivide; }
	public void setSpVdivide(double spVdivide) { Settings.spVdivide = spVdivide; }

	public String getLastCIDPath() { return lastCIDPath; }
	public void setLastCIDPath(String lastCIDPath) { Settings.lastCIDPath = lastCIDPath; }

	public String getLastCLDPath() { return lastCLDPath; }
	public void setLastCLDPath(String lastCLDPath) { Settings.lastCLDPath = lastCLDPath; }
}
