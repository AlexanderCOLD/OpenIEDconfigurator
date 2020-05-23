package iec61850;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;
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
 * @description - Intelligent electronic device
 */

@Getter @Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class IED extends IECObject{

    /** Список логических устройств */
    @XmlElement(name = "LD")
    private ObservableList<LD> logicalDeviceList = FXCollections.observableArrayList(); { logicalDeviceList.addListener(this::listChanged); }

    /** Список внутренних соединений */
    @XmlElement(name = "Connection")
    private ArrayList<Connection> connectionList = new ArrayList<>(); // Пока не используется



    public String toString(){ if(description!=null && !description.equals("unknown")) return String.format("%s (%s)", name, description); else return name; }
}

