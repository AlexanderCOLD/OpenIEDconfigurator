package iec61850;

import tools.saveload.SaveLoadObject;
import java.io.File;

/**
 * @author Александр Холодов
 * @created 04/2020
 * @project OpenIEDconfigurator
 * @description
 */
public class SaveTest {

    public static void save(CLD object){
        File test = new File("testSave.xml");
        SaveLoadObject.save(object,test);
    }

    public static void main(String[] args) {
        File test = new File("testSave.xml");
        CLD cld = new CLD();

        IED ied = new IED();
        ied.setName("TEST");

        LD ld = new LD();
        ld.setName("TEST");
        ied.getLogicalDeviceList().add(ld);

        LN ln = new LN();
        ln.setName("TEST");
        ld.getLogicalNodeList().add(ln);

        cld.getIedList().add(ied);

        SaveLoadObject.save(cld,test);
    }

}
