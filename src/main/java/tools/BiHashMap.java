package tools;

import java.util.*;

/**
 * @author Александр Холодов
 * @created 04/2020
 * @project OpenIEDconfigurator
 * @description - двойной словать (key-value, value-key)
 */
public class BiHashMap<K,V>  {

    private final HashMap<K,V> original = new HashMap<>(16, 0.05f);
    private final HashMap<V,K> reverse = new HashMap<>(16, 0.05f);

    public void put(K key, V value) {  original.put(key, value); reverse.put(value, key); }

    public int size(){ return original.size(); }

    public V getValue(Object key) { reHash(); return original.get(key);  }
    public K getKey(Object value) { return reverse.get(value); }

    public void removeByKey(K key){ if(original.containsKey(key)){ Object value = original.get(key); original.remove(key); reverse.remove(value); } }
    public void removeByValue(V value){ reHash(); if(reverse.containsKey(value)){ Object key = reverse.get(value); original.remove(key); reverse.remove(value); } }

    public void clear(){ original.clear(); reverse.clear(); }

    public Set<K> keySet() { reHash(); return original.keySet(); }
    public Set<V> valueSet() { reHash(); return reverse.keySet(); }

    public boolean containsKey(Object key) { reHash(); return original.containsKey(key); }
    public boolean containsValue(Object value) { reHash(); return reverse.containsKey(value); }

    public boolean contains(Object object){ reHash(); return containsKey(object) || containsValue(object); }

    public Set<Map.Entry<K,V>> entrySet() { return original.entrySet(); }
    public Set<Map.Entry<V,K>> entrySetReverse() { return reverse.entrySet(); }


    private void reHash(){
        ArrayList<K> keyList = new ArrayList<>(original.keySet());
        ArrayList<V> valueList = new ArrayList<>(original.values());

        original.clear();
        reverse.clear();

        for (int i=0; i<keyList.size(); i++){
            original.put(keyList.get(i), valueList.get(i));
            reverse.put(valueList.get(i), keyList.get(i));
        }

    }

}
