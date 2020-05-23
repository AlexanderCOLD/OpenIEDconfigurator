package iec61850;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import java.util.UUID;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Data Attribute
 */
@Getter @Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class DA extends IECObject {

    /** Значение атрибута */
    private String value;

    /** Минимальное значение атрибута */
    private String minValue;

    /** Максимальное значение атрибута */
    private String maxValue;

    /** Значение по умолчанию */
    private String defaultValue;

    /** Шаг изменения значения */
    private String step;



    public String toString(){
        String type = this.type!=null ? "{"+this.type.replaceAll("iec_","")+"}":"(err)";
        String name = this.name!=null ? this.name.replaceAll("in_","").replaceAll("out_",""):"(err)";
        return String.format("%s %s", type, name);
    }
}
