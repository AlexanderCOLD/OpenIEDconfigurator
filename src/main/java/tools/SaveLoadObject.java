package tools;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * @author Александр Холодов
 * @created 02/2020
 * @project OpenIEDconfigurator
 * @description Сохранение/загрузка проектов
 *
 * @XmlRootElement определяет корневой элемент для содержимого XML.
 * @XmlType используется для упорядочения элементов в XML.
 * @XmlTransient помечается то, что не будет не записано в XML.
 * @XmlAttribute помечается то, что будет использовано в качестве атрибута.
 * @XmlElement(name="qwerty") — создаст элемент с именем «qwerty».
 *
 * @XmlElementWrapper — обертка вокруг коллекций для читабельности сгенерированного XML
 * @XmlSeeAlso Объекты другого класса помечаются аннотацией .
 * @XmlEnum и @XmlEnumValue — для Enum и значений
 * @XmlElements — создание контейнеров для нескольких @XmlElement
 */

public class SaveLoadObject {

	public static <T> void save (T object, File file) {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(object.getClass());
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(object, file);
		}
		catch (JAXBException e) { e.printStackTrace(); }
	}


	@SuppressWarnings("unchecked")
	public static <T> T load(Class<T> classType, File file){
	    T object=null;
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(classType);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Object obj = jaxbUnmarshaller.unmarshal(file);
			try { object = (T) obj;	} catch (ClassCastException cce) { object = null; cce.printStackTrace(); }
		}
		catch (JAXBException e ) { e.printStackTrace(); }
		return  object;
	}

}
