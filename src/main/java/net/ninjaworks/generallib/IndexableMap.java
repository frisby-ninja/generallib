package net.ninjaworks.generallib;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A map whose elements each have an index.
 * @param <K> The map's key-type
 * @param <V> The map's value-type
 */
public class IndexableMap<K, V> extends HashMap<K, V> {

    private final LinkedList<K> keyList = new LinkedList<>();

    @Override
    public V put(K key, V value) {
        if (!keyList.contains(key))
            keyList.add(key);
        return super.put(key, value);
    }


    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        keyList.clear();
        super.clear();
    }

    /**
     * Gets all the keys currently present in the map.
     * @return A list of the keys currently within the map
     */
    public List<K> getKeys() {
        return keyList;
    }

    /**
     * Gets the index of the given key.
     * @param key The key to get the index of
     * @return The index of the key, -1 if the key is not present
     */
    public int getKeyIndex(K key) {
        return keyList.indexOf(key);
    }

    /**
     * Gets the key at the given index.
     * @param index The index of the key to get
     * @return The key at that index, null if it fails
     */
    public K getKeyAt(int index) {
        if(keyList.size() > index)
            return keyList.get(index);
        return null;
    }

    /**
     * Gets the value at the given index.
     * @param index The index of the value to get
     * @return The value at that index, null if it fails
     */
    public V getValueAt(int index) {
        K key = getKeyAt(index);
        if (key != null)
            return get(key);
        return null;
    }
}