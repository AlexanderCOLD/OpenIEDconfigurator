package controllers.dialogs;

import application.GUI;
import javafx.stage.FileChooser;
import tools.Settings;

import java.io.File;
import java.util.Set;

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
    private File file;

    public FileChooserDialog(){
        fileChooser = new FileChooser();
        extCIDFilter = new FileChooser.ExtensionFilter("CID IEC61850 files (*.cid)", "*.cid");
        extCLDFilter = new FileChooser.ExtensionFilter("OpenIEDconfigurator files (*.cld)", "*.cld");
    }

    public static File openCIDFile(){
        if(self==null) self = new FileChooserDialog(); self.fileChooser.getExtensionFilters().clear(); self.fileChooser.getExtensionFilters().add(self.extCIDFilter); show(); return self.file;
    }
    public static File openCLDFile(){
        if(self==null) self = new FileChooserDialog(); self.fileChooser.getExtensionFilters().clear(); self.fileChooser.getExtensionFilters().add(self.extCLDFilter); show(); return self.file;
    }
    public static File saveCIDFile(){
        if(self==null) self = new FileChooserDialog(); self.fileChooser.getExtensionFilters().clear(); self.fileChooser.getExtensionFilters().add(self.extCIDFilter); show(); return self.file;
    }
    public static File saveCLDFile(){
        if(self==null) self = new FileChooserDialog(); self.fileChooser.getExtensionFilters().clear(); self.fileChooser.getExtensionFilters().add(self.extCLDFilter); show(); return self.file;
    }

    private static void show(){
        if(Settings.lastPath!=null){
            File initDir = new File(Settings.lastPath);
            if(initDir.exists()) self.fileChooser.setInitialDirectory(new File(Settings.lastPath));
        }
        File file = self.fileChooser.showOpenDialog(GUI.getStage());
        if(file!=null) Settings.lastPath = file.getParent();
        self.file = file;
    }
}
