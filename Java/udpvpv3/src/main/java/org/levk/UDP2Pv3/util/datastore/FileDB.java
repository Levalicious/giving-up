package org.levk.UDP2Pv3.util.datastore;

import org.bouncycastle.util.encoders.Hex;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class FileDB implements Source<byte[], byte[]> {
    private final DB db;

    public FileDB(String name) throws IOException {
        Options options = new Options();
        options.createIfMissing();
        options.writeBufferSize(8 * 1024 * 1024);
        options.maxOpenFiles(1024 * 1024);
        options.compressionType(CompressionType.SNAPPY);

        db =  new Iq80DBFactory().open(new File(name), options);
    }

    public void put(byte[] key, byte[] val) {
        try {
            if (val == null) {
                delete(copy(key));
            } else {
                db.put(copy(key), copy(val));
            }
        } catch (Exception e) {
            System.out.println("Failed to insert " + Hex.toHexString(key));
        }
    }

    public byte[] get(byte[] key) {
        return db.get(copy(key));
    }

    @Override
    public void delete(byte[] key) {
        db.delete(copy(key));
    }

    public boolean flush() {
        return true;
    }

    public void close() throws IOException {
        db.close();
    }

    private static byte[] copy(byte[] in) {
        return Arrays.copyOf(in, in.length);
    }
}
