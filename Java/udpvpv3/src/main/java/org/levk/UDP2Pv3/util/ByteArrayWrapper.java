package org.levk.UDP2Pv3.util;

import java.util.Arrays;

public class ByteArrayWrapper {
    private final byte[] data;

    public ByteArrayWrapper(byte[] data) {
        if (data == null) {
            throw new NullPointerException();
        }

        this.data = data;
    }

    public byte[] bytes() {
        return data;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ByteArrayWrapper)) return false;

        return Arrays.equals(data, ((ByteArrayWrapper) other).data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
