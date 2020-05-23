package iec61850;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import javax.xml.bind.annotation.*;
import java.io.File;

/**
 * @author Александр Холодов
 * @created 04/2020
 * @project OpenIEDconfigurator
 * @description Configured Logic Description
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CLD")
@Getter @Setter
public class CLD extends IECObject{

    @XmlTransient
    private File cidFile;

    /** Все IED содержащиеся в проекте */
    @XmlElement(name = "IED")
    private final ObservableList<IED> iedList = FXCollections.observableArrayList(); { iedList.addListener(this::listChanged); }



    @Override
    public String toString() { if(cidFile==null) return null; else return cidFile.getAbsolutePath(); }
}


