package iec61850;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 04/2020
 * @project OpenIEDconfigurator
 * @description Configured Logic Description
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CLD")
@Data
public class CLD {

    @XmlElement(name = "IED")
    private ArrayList<IED> iedList = new ArrayList<>();

}

