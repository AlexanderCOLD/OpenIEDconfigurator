package iec61850;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Logical device
 */
@Getter @Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class LD extends IECObject {

    /** Список логических узлов */
    @XmlElement(name = "LN")
    private final ObservableList<LN> logicalNodeList = FXCollections.observableArrayList(); { logicalNodeList.addListener(this::listChanged); }

    /** Список датасетов (GOOSE, SV, MMS) */
    @XmlElement(name = "DS")
    private final ObservableList<DS> dataSets = FXCollections.observableArrayList(); { dataSets.addListener(this::listChanged); }

    /** Вложенные объекты */
    @XmlElement(name = "DO")
    private final ObservableList<DO> dataObjects = FXCollections.observableArrayList(); { dataObjects.addListener(this::listChanged); }

    /** Атрибуты объекта */
    @XmlElement(name = "DA")
    private final ObservableList<DA> attributes = FXCollections.observableArrayList(); { attributes.addListener(this::listChanged); }


    /** Список соединений */
    @XmlElement(name = "Connection")
    private final ObservableList<Connection> connectionList = FXCollections.observableArrayList();


    public String toString(){
        String type = this.type!=null ? this.type : this.description;
        String name = this.name!=null ? this.name : "err";
        return String.format("%s (%s)", name, type);
    }
}
