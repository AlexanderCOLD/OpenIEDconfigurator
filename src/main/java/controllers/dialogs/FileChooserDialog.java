package controllers.dialogs;

import application.GUI;
import javafx.stage.FileChooser;
import tools.Settings;

import java.io.File;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Окна открыть/сохранить
 */
public class FileChooserDialog {

    private static final FileChooser fileChooser = new FileChooser();
    private static final FileChooser.ExtensionFilter extCIDFilter = new FileChooser.ExtensionFilter("CID IEC61850 files (*.cid)", "*.cid");
    private static final FileChooser.ExtensionFilter extCLDFilter = new FileChooser.ExtensionFilter("OpenIEDconfigurator files (*.cld)", "*.cld");

    public static File openCIDFile(){
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(extCIDFilter);

        if(Settings.lastCIDPath != null){
            File initDir = new File(Settings.lastCIDPath);
            if(initDir.exists()) fileChooser.setInitialDirectory(new File(initDir.getParent()));
        }
        File file = fileChooser.showOpenDialog(GUI.getStage()); if(file!=null) Settings.lastCIDPath = file.toString();

        return file;
    }
    public static File saveCLDFile(){
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(extCLDFilter);

        if(Settings.lastCLDPath!=null){
            File initDir = new File(Settings.lastCLDPath);
            if(initDir.exists()) fileChooser.setInitialDirectory(new File(initDir.getParent()));
        }
        File file = fileChooser.showSaveDialog(GUI.getStage());
        if(file!=null) Settings.lastCLDPath = file.toString();

        return file;
    }

    public static File openCLDFile(){
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(extCLDFilter);

        if(Settings.lastCLDPath!=null){
            File initDir = new File(Settings.lastCLDPath);
            if(initDir.exists()) fileChooser.setInitialDirectory(new File(initDir.getParent()));
        }
        File file = fileChooser.showOpenDialog(GUI.getStage());
        if(file!=null) Settings.lastCLDPath = file.toString();

        return file;
    }
    public static File saveCIDFile(){
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(extCIDFilter);

        if(Settings.lastCIDPath != null){
            File initDir = new File(Settings.lastCIDPath);
            if(initDir.exists()) fileChooser.setInitialDirectory(new File(initDir.getParent()));
        }
        File file = fileChooser.showSaveDialog(GUI.getStage()); if(file!=null) Settings.lastCIDPath = file.toString();

        return file;
    }

}
