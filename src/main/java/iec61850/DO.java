package iec61850;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import javax.xml.bind.annotation.*;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Data Object
 */
@Getter @Setter
@XmlRootElement(name = "DO")
@XmlAccessorType(XmlAccessType.FIELD)
public class DO  extends IECObject {

    /** Вложенные объекты */
    @XmlElement(name = "DO")
    private final ObservableList<DO> dataObjects = FXCollections.observableArrayList(); { dataObjects.addListener(this::listChanged); }

    /** Атрибуты объекта */
    @XmlElement(name = "DA")
    private final ObservableList<DA> dataAttributes = FXCollections.observableArrayList(); { dataAttributes.addListener(this::listChanged); }



    public String toString(){
        String type = this.type!=null ? this.type.replaceAll("iec_","") : "err";
        String name = this.name!=null ? this.name.replaceAll("in_","").replaceAll("out_","").replaceAll("set_","") : "err";
        return String.format("%s (%s)", name, type);
    }
}
