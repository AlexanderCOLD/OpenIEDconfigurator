package iec61850;

import iec61850.objects.SCL;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.UUID;

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
    private final ObservableList<DO> content = FXCollections.observableArrayList(); { content.addListener(this::listChanged); }

    /** Атрибуты объекта */
    @XmlElement(name = "DA")
    private final ObservableList<DA> attributes = FXCollections.observableArrayList(); { attributes.addListener(this::listChanged); }



    public String toString(){
        String type = this.type!=null ? "{"+this.type.replaceAll("iec_","")+"}":"(err)";
        String name = this.name!=null ? this.name.replaceAll("in_","").replaceAll("out_",""):"(err)";
        return String.format("%s %s", type, name);
    }
}
