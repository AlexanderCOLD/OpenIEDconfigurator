package controllers.dialogs;

import application.GUI;
import javafx.stage.FileChooser;
import java.io.File;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Окна открыть/сохранить
 */
public class FileChooserDialog {

    private static FileChooserDialog self;
    private FileChooser fileChooser;
    private FileChooser.ExtensionFilter extCIDFilter, extCLDFilter;

    public FileChooserDialog(){
        fileChooser = new FileChooser();
        extCIDFilter = new FileChooser.ExtensionFilter("CID IEC61850 files (*.cid)", "*.cid");
        extCLDFilter = new FileChooser.ExtensionFilter("OpenIEDconfigurator files (*.cld)", "*.cld");
    }

    public static File openCIDFile(){
        if(self==null) self = new FileChooserDialog();
        self.fileChooser.getExtensionFilters().add(self.extCIDFilter);
        return self.fileChooser.showOpenDialog(GUI.getStage());
    }
    public static File openCLDFile(){
        if(self==null) self = new FileChooserDialog();
        self.fileChooser.getExtensionFilters().add(self.extCLDFilter);
        return self.fileChooser.showOpenDialog(GUI.getStage());
    }
    public static File saveCIDFile(){
        if(self==null) self = new FileChooserDialog();
        self.fileChooser.getExtensionFilters().add(self.extCIDFilter);
        return self.fileChooser.showSaveDialog(GUI.getStage());
    }
    public static File saveCLDFile(){
        if(self==null) self = new FileChooserDialog();
        self.fileChooser.getExtensionFilters().add(self.extCLDFilter);
        return self.fileChooser.showSaveDialog(GUI.getStage());
    }
}
