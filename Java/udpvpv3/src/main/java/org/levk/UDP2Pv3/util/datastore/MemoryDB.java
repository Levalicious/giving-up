package org.levk.UDP2Pv3.util.datastore;

import org.levk.UDP2Pv3.util.ByteArrayWrapper;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MemoryDB<V> {
    protected final ConcurrentMap<ByteArrayWrapper, V> storage;

    public MemoryDB() {
        this("defaultMap");
    }

    public MemoryDB(String name) {
        storage = new ConcurrentHashMap<>();
    }

    public void put(byte[] key, V val) {
        if (val == null) {
            delete(key);
        } else {
            storage.put(new ByteArrayWrapper(key), val);
        }
    }

    public V get(byte[] key) {
        return storage.get(new ByteArrayWrapper(key));
    }

    public boolean contains(byte[] key) {
        return storage.containsKey(new ByteArrayWrapper(key));
    }

    public V delete(byte[] key) {
        return storage.remove(new ByteArrayWrapper(key));
    }

    public Collection<V> values() {
        return storage.values();
    }

    public void putAll(Collection<Map.Entry<ByteArrayWrapper, V>> entries) {
        for (Map.Entry<ByteArrayWrapper, V> e : entries) {
            storage.put(e.getKey(), e.getValue());
        }
    }
}
