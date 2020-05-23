package iec61850;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.UUID;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description DataSet
 */

@Getter @Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class DS extends IECObject {

    /** Название датасета из CID (список всех датасетов)*/
    private String datSetName;

    /** ID датасета (appID - для GOOSE, rptID - для ММС)*/
    private String ID;

    /** Параметры ММС */
    private int intgPd, bufTime;

    /** Вложенные объекты */
    @XmlElement(name = "DO")
    private final ObservableList<DO> dataObject = FXCollections.observableArrayList(); { dataObject.addListener(this::listChanged); }

    /** Атрибуты датасета */
    @XmlElement(name = "DA")
    private final ObservableList<DA> attributes = FXCollections.observableArrayList(); { attributes.addListener(this::listChanged); }



    public String toString(){ return String.format("{%s} %s", type, name); }
}
