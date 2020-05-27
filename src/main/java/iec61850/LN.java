package iec61850;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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

    /** Вложенные объекты */
    @XmlElement(name = "DO")
    private final ObservableList<DO> dataObjects = FXCollections.observableArrayList(); { dataObjects.addListener(this::listChanged); }

    /** Атрибуты объекта */
    @XmlElement(name = "DA")
    private final ObservableList<DA> dataAttributes = FXCollections.observableArrayList(); { dataAttributes.addListener(this::listChanged); }


    public String toString(){
        String type = this.type!=null ? this.type : "err";
        String name = this.name!=null ? this.name : "err";
        return String.format("%s (%s)", name, type);
    }
}
