package iec61850;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description - Адресс объекта МЭК61850
 */
public class IECCppAddress {

    private final IECObject iecObject;

    public IECCppAddress(IECObject iecObject){ this.iecObject = iecObject; }

    /**
     * Получить выборочный адрес объекта (с точками)
     * get(LD.class, DS class, ...)
     * @param list - перечень объектов из которых будет состояеть адрес
     * @return "ied.ld.ln.do"
     */
    public String withDots(Class<?>... list){ return get(".", list); }

    /**
     * Получить полный адрес объекта (с точками)
     * get(LD.class, DS class, ...)
     * @return "ied.ld.ln.do"
     */
    public String fullWithDots(){ return get(".", getParentsClass()); }

    /**
     * Получить выборочный адрес объекта (с слэшами)
     * get(LD.class, DS class, ...)
     * @param list - перечень объектов из которых будет состояеть адрес
     * @return "ied/ld/ln/do"
     */
    public String withSlash(Class<?>... list) { return get("/", list); }

    /**
     * Получить выборочный адрес объекта (с слэшами)
     * get(LD.class, DS class, ...)
     * @return "ied/ld/ln/do"
     */
    public String fullWithSlash() { return get("/", getParentsClass()); }

    /**
     * Получить адрес объекта
     * get("/", LD.class, DS class, ...)
     * @param list - перечень объектов из которых будет состояеть адрес
     */
    public String get(String divider, Class<?>... list){
        StringBuilder address = new StringBuilder();
        for(Class<?> c:list){
            IECObject object = parent(c);
            if(object!=null && object.getCppName()!=null) address.append(object.getCppName()).append(divider);
        }
        address.append(iecObject.getName());
        return address.toString();
    }

    /** Перечень родительских классов */
    private Class<?>[] getParentsClass(){
        ArrayList<Class<?>> classList = new ArrayList<>();
        IECObject parent = iecObject.getParent();
        while(parent!=null){ classList.add(parent.getClass()); parent = parent.getParent(); }
        Collections.reverse(classList);
        Class<?>[] result = new Class<?>[classList.size()];
        IntStream.range(0, classList.size()).forEach(v -> result[v] = classList.get(v));
        return result;
    }

    /**
     * Взять родительский объект
     * @param classType - тип объекта
     * @return - родительский объект
     */
    private IECObject parent(Class<?> classType){
        IECObject iecObject = this.iecObject;
        if(iecObject.getClass()==classType) return (IECObject) iecObject;
        while(iecObject!=null){ iecObject = iecObject.getParent(); if(iecObject!=null && iecObject.getClass()==classType) return (IECObject) iecObject; }
        return null;
    }
}
