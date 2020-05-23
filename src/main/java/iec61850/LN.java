package iec61850;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Logical node
 */

@Getter @Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "LN")
public class LN extends IECObject {

    /** Датасет с входящими объектами */
    private final ObservableList<DS> dataSetInput = FXCollections.observableArrayList(); { dataSetInput.addListener(this::listChanged); }

    /** Датасет с исходящими объектами */
    private final ObservableList<DS> dataSetOutput = FXCollections.observableArrayList(); { dataSetOutput.addListener(this::listChanged); }



    public String toString(){
        return String.format("%s %s", (name!=null && !name.equals("unknown"))?name: type, (type !=null && !type.equals("unknown"))?("("+ type +")"):"");
    }
}
