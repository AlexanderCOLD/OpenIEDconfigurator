package tools;

import java.util.HashMap;
import java.util.Set;

/**
 * @author Александр Холодов
 * @created 04/2020
 * @project OpenIEDconfigurator
 * @description
 */
public class BiHashMap<K,V>  {

    private HashMap<K,V> original = new HashMap<>();
    private HashMap<V,K> reverse = new HashMap<>();

    public void put(K key, V value) {  original.put(key, value); reverse.put(value, key); }

    public int size(){ return original.size(); }

    public V getValue(Object key) { return original.get(key); }
    public K getKey(Object value) { return reverse.get(value); }

    public void removeByKey(K key){ if(original.containsKey(key)){ Object value = original.get(key); original.remove(key); reverse.remove(value); } }
    public void removeByValue(V value){ if(reverse.containsKey(value)){ Object key = reverse.get(value); original.remove(key); reverse.remove(value); } }

    public void clear(){ original.clear(); reverse.clear(); }

    public Set<K> keySet() { return original.keySet(); }
    public Set<V> valueSet() { return reverse.keySet(); }
}
