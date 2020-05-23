package iec61850;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.UUID;

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

    /** Список исходящих GOOSE датасетов */
    @XmlElement(name = "GOOSEOutputDataSet")
    private final ObservableList<DS> gooseOutputDS = FXCollections.observableArrayList(); { gooseOutputDS.addListener(this::listChanged); }

    /** Список входящих GOOSE датасетов */
    @XmlElement(name = "GOOSEInputDataSet")
    private final ObservableList<DS> gooseInputDS = FXCollections.observableArrayList(); { gooseInputDS.addListener(this::listChanged); }

    /** Список входящих MMS сообщений */
    @XmlElement(name = "MMSOutputDataSet")
    private final ObservableList<DS> mmsOutputDS = FXCollections.observableArrayList(); { mmsOutputDS.addListener(this::listChanged); }

    /** Список соединений */
    @XmlElement(name = "Connection")
    private final ObservableList<Connection> connectionList = FXCollections.observableArrayList();



    public String toString(){ if(description!=null && !description.equals("unknown")) return String.format("%s (%s)", name, description); else return name; }
}
