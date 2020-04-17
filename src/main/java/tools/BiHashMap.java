package tools;

import lombok.Data;
import java.util.*;

/**
 * @author Александр Холодов
 * @created 04/2020
 * @project OpenIEDconfigurator
 * @description - двойной словать на основе ArrayList (key-value, value-key)
 */
public class BiHashMap<K,V>  {

    private final ArrayList<Set<K,V>> list = new ArrayList<>();

    public void put(K key, V value) {
        boolean found = false;
        for(Set<K,V> s:list) if(s.getKey().equals(key)) { s.setValue(value); found = true; }
        if(!found) list.add(new Set<K,V>(key,value));
    }

    public int size(){ return list.size(); }

    public V getValue(Object key) { for(Set<K,V> s:list) if(s.getKey().equals(key)) return s.getValue(); return null; }
    public K getKey(Object value) { for(Set<K,V> s:list) if(s.getValue().equals(value)) return s.getKey(); return null; }

    public void removeByKey(K key){ for(Set<K,V> s:list) if(s.getKey().equals(key)) { list.remove(s); return; } }
    public void removeByValue(V value){ for(Set<K,V> s:list) if(s.getValue().equals(value)) { list.remove(s); return; } }

    public void clear(){ list.clear(); }

    public ArrayList<K> keySet() { return new ArrayList<K>(){{ for(Set<K,V> s:list) add(s.getKey()); }}; }
    public ArrayList<V> valueSet() { return new ArrayList<V>(){{ for(Set<K,V> s:list) add(s.getValue()); }}; }

    public boolean containsKey(Object key) { for(Set<K,V> s:list) if(s.getKey().equals(key)) return true; return false; }
    public boolean containsValue(Object value) { for(Set<K,V> s:list) if(s.getValue().equals(value)) return true; return false; }

    public boolean contains(Object object){ return containsKey(object) || containsValue(object); }

    public ArrayList<Set<K,V>> entrySet() { return new ArrayList<>(list); }

    @Data
    private class Set<K,V>{
        private K key;
        private V value;

        public Set(K key, V value) { this.key = key; this.value = value; }
    }

}
