package iec61850;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;

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


    public String toString(){
        String type = this.description!=null ? this.description : this.type;
        String name = this.name!=null ? this.name : "err";
        return String.format("%s (%s)", name, type);
    }
}

