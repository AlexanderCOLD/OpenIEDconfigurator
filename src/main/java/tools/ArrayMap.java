package tools;

import lombok.Data;
import java.util.*;

/**
 * @author Александр Холодов
 * @created 04/2020
 * @project OpenIEDconfigurator
 * @description - двойной словать на основе ArrayList (key-value, value-key)
 */
public class ArrayMap<K,V>  {

    private final ArrayList<Set<K,V>> list = new ArrayList<>();

    public void put(K key, V value) {
        for(Set<K,V> s:list) if(s.getKey().equals(key)) { s.setValue(value); return; }
        list.add(new Set<K,V>(key,value));
    }

    public void put(int index, K key, V value) {
        Set<K,V> temp = null;
        for(Set<K,V> s:list) if(s.getKey().equals(key)) { temp = s; list.remove(temp); }
        if(temp!=null) list.add(index, temp); else list.add(index, new Set<K,V>(key,value));
    }

    public int size(){ return list.size(); }

    public V getValue(Object key) { for(Set<K,V> s:list) if(s.getKey().equals(key)) return s.getValue(); return null; }
    public K getKey(Object value) { for(Set<K,V> s:list) if(s.getValue().equals(value)) return s.getKey(); return null; }
    public Set<K,V> get(int index) { return list.get(index); }

    public void removeByKey(K key){ for(Set<K,V> s:list) if(s.getKey().equals(key)) { list.remove(s); return; } }
    public void removeByValue(V value){ for(Set<K,V> s:list) if(s.getValue().equals(value)) { list.remove(s); return; } }
    public void remove(int index){ list.remove(index); }

    public void clear(){ list.clear(); }

    public ArrayList<K> keys() { return new ArrayList<K>(){{ for(Set<K,V> s:list) add(s.getKey()); }}; }
    public ArrayList<V> values() { return new ArrayList<V>(){{ for(Set<K,V> s:list) add(s.getValue()); }}; }

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
