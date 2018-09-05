package org.levk.UDP2Pv2.util.datastore;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class MemoryDB implements Source<byte[], byte[]> {
    protected final DB db;
    protected final ConcurrentMap<byte[], byte[]> storage;

    public MemoryDB() {
        db = DBMaker.memoryDirectDB().transactionEnable().make();
        storage = db.hashMap("map", Serializer.BYTE_ARRAY, Serializer.BYTE_ARRAY).createOrOpen();
    }

    public MemoryDB(ConcurrentMap<byte[], byte[]> storage) {
        db = DBMaker.memoryDirectDB().transactionEnable().make();
        this.storage = storage;
    }

    public void put(byte[] key, byte[] val) {
        if (val == null) {
            delete(key);
        } else {
            storage.put(key, val);
        }
    }

    public void putAll(Collection<Map.Entry<byte[], byte[]>> entries) {
        for (Map.Entry<byte[], byte[]> e : entries) {
            this.put(e.getKey(), e.getValue());
        }
    }

    public byte[] get(byte[] key) {
        return storage.get(key);
    }

    public void delete(byte[] key) {
        storage.remove(key);
    }

    public boolean flush() {
        return true;
    }

    public void setName(String name) {}

    public String getNAme() {
        return "in-memory";
    }

    public void init() {}

    public boolean isAlive() {
        return true;
    }

    public void close() {}

    public Set<byte[]> keys() {
        return getStorage().keySet();
    }

    public void updateBatch(Map<byte[], byte[]> rows) {
        for (Map.Entry<byte[], byte[]> entry : rows.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public Map<byte[], byte[]> getStorage() {
        return storage;
    }
}
