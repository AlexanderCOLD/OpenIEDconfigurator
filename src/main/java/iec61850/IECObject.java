package iec61850;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Александр Холодов
 * @created 05/2020
 * @project OpenIEDconfigurator
 * @description - Общий класс
 */
@Getter @Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class IECObject {

    /** Уникальный ID */
    @XmlTransient
    protected final String UID = UUID.randomUUID().toString();

    /** Родтьельский объект
     * CLD -> IED -> LD -> LN -> DS -> DO -> DA */
    @XmlTransient
    protected IECObject parent;

    /** Лист с вложенными объектами */
    @XmlTransient
    protected final ObservableList<IECObject> children = FXCollections.observableArrayList();

    /** Адрес объекта */
    @XmlTransient
    protected final IECAddress address = new IECAddress(this);

    /** Уникальное имя */
    protected String name;

    /** Тип данных */
    protected String type;

    /** Описание объекта */
    protected String description;

    /** Название С++ */
    protected String cppName;

    /** Класс С++ */
    protected String cppType;

    /** Номер экземпляра */
    private Long instance;

    /** Координаты для GUI (null - значит не отрисован) */
    protected Double layoutX, layoutY; // Координаты


    /**
     * Дополнительные теги:
     *  "additional" - дополнительный элемент (LN) (если объекта нет в CID)
     *  "oscillogram" - объект пишется в осциллограмму
     */
    @XmlList
    protected final ArrayList<String> tags = new ArrayList<>();



    /** Изменение листа объектов (parent-children) */
    protected void listChanged(ListChangeListener.Change<? extends IECObject> c){ while(c.next()){
        if(c.wasAdded()) for(IECObject iecObject:c.getAddedSubList()) { iecObject.setParent(this); children.add(iecObject); }
        if(c.wasRemoved()) for(IECObject iecObject:c.getRemoved()) { iecObject.setParent(null); children.remove(iecObject); }}
    }
}
