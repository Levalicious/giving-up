package org.levk.UDP2Pv3.util.datastore;

public interface Source<K, V> {
    void put(K key, V val);

    byte[] get(K key);

    void delete(K key);
}
