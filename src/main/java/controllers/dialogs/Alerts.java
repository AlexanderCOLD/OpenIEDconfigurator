package controllers.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.util.Optional;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Всплывающие окошки/подтверждения
 */
public class Alerts {

    private static Alert request, error;
    private static Alerts self;

    public Alerts(){
        request = new Alert(AlertType.CONFIRMATION);
        ((Stage) request.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/view/image/Icon.png")));

        error = new Alert(AlertType.ERROR);
        ((Stage) error.getDialogPane().getScene().getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/view/image/Icon.png")));
    }

    public static boolean requestDialog(String title, String info, String msg){
        if(self==null) self = new Alerts();
        request.setTitle(info);
        request.setHeaderText(info);
        request.setContentText(msg);
        Optional<ButtonType> result = request.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) { return true; }
        return false;
    }

    public static void errorDialog(String title, String info, String msg){
        if(self==null) new Alerts();
        error.setTitle(info);
        error.setHeaderText(info);
        error.setContentText(msg);
        error.showAndWait();
    }
}
