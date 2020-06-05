package iec61850;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Адресс объекта МЭК61850
 */
public class IECAddress {

    private final IECObject iecObject;

    public IECAddress(IECObject iecObject){ this.iecObject = iecObject; }

    /**
     * Получить полный адрес объекта
     * get(LD.class, DS class, ...)
     * @return "ied/ld/ln/do"
     */
    public String get() {
        StringBuilder address = new StringBuilder(iecObject.getName());
        IECObject parent = iecObject.getParent();
        while(parent!=null){
            if(parent.getName() != null)
                address.insert(0, "/").insert(0, parent.getName());
            parent = parent.getParent();
        }
        return address.toString();
    }

    /**
     * Получить адрес объекта (с фильтрацией)
     * get("/", LD.class, DS class, ...)
     * @param list - перечень объектов которые не будут включены в адрес
     */
    public String get(Class<?>... list){
        StringBuilder address = new StringBuilder(iecObject.getName());
        List<Class<?>> filter = Arrays.asList(list);
        IECObject parent = iecObject.getParent();
        while(parent!=null){
            if(parent.getName() != null && !filter.contains(parent.getClass()))
                address.insert(0, "/").insert(0, parent.getName());
            parent = parent.getParent();
        }
        return address.toString();
    }

    /**
     * Получить c++ полный адрес объекта
     * get(LD.class, DS class, ...)
     * @return "ied/ld/ln/do"
     */
    public String getCpp() {
        StringBuilder address = new StringBuilder(iecObject.getCppName());
        IECObject parent = iecObject.getParent();
        while(parent!=null){
            if(parent.getCppName() != null)
                address.insert(0, "/").insert(0, parent.getCppName());
            parent = parent.getParent();
        }
        return address.toString();
    }

    /**
     * Получить c++ адрес объекта (с фильтрацией)
     * get("/", LD.class, DS class, ...)
     * @param list - перечень объектов которые не будут включены в адрес
     */
    public String getCpp(Class<?>... list){
        if(iecObject.getCppName()==null) return "error";
        StringBuilder address = new StringBuilder(iecObject.getCppName());
        List<Class<?>> filter = Arrays.asList(list);
        IECObject parent = iecObject.getParent();
        while(parent!=null){
            if(parent.getCppName() != null && !filter.contains(parent.getClass()))
                address.insert(0, "/").insert(0, parent.getCppName());
            parent = parent.getParent();
        }
        return address.toString();
    }
}
