package controllers.dialogs;

import iec61850.LN;
import org.junit.jupiter.api.Test;
import tools.SaveLoadObject;
import java.io.File;

class TripPointDialogTest {

    @Test
    void showDialog() {
		LN ln = SaveLoadObject.load(LN.class, new File("library/LN/PDIF.xml"));
		EditorDialog.show(ln);
    }
}