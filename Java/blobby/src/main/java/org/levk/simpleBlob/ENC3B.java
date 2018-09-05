package org.levk.simpleBlob;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;

public class ENC3B {
    public static byte[] enc3b(byte[] data) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            if (isEnc3b(data)) {
                if ((data[0] & 0xFF) < 128) {
                    return data;
                }
            }

            out.write(getLengthBytes(data.length, false));
            out.write(data);

            return out.toByteArray();
        } catch (IOException i) {
            i.printStackTrace();
            System.exit(150);
            return null;
        }
    }

    public static byte[] enc3b(byte[]... data) {
        List<byte[]> in= new ArrayList<>();

        for (int i = 0; i < data.length; i++) {
            in.add(data[i]);
        }

        return enc3b(in);
    }

    public static byte[] enc3b(List<byte[]> data) {
        if (data.size() == 1) {
            return enc3b(data.get(0));
        } else if (data.size() < 1) {
            return new byte[0];
        }

        try {
            ByteArrayOutputStream temp = new ByteArrayOutputStream();

            for (byte[] piece : data) {
                temp.write(enc3b(piece));
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            out.write(getLengthBytes(temp.toByteArray().length, true));
            out.write(temp.toByteArray());

            return out.toByteArray();
        } catch (IOException i) {
            i.printStackTrace();
            System.exit(150);
            return null;
        }
    }

    public static ENCList dec3b(byte[] in) {
        return dec3b(in, 0).x;
    }

    private static Tuple<ENCList, Integer> dec3b(byte[] in, int pos) {
        int startpos = pos;

        int L = (((in[pos] & 0xFF) % 128) << 16) + ((in[pos + 1] & 0xFF) << 8) + (in[pos + 2] & 0xFF);

        ENCList o = new ENCList();

        if ((in[pos] & 0xFF) < 128) {
            assert in.length >= pos + 3 + L;
            o.add(new ENCItem(Arrays.copyOfRange(in, pos + 3, pos + 3 + L)));
            return new Tuple(o, pos + 3 + L);
        } else {
            pos += 3;

            while (pos < startpos + 3 + L) {
                Tuple<ENCList, Integer> temp = dec3b(in, pos);
                o.add(temp.x.get(0));
                pos = temp.y;
            }

            assert pos == startpos + 3 + L;

            return new Tuple(o, pos);
        }
    }

    public static boolean isEnc3b(byte[] in) {
        int length = (((in[0] & 0xFF) % 128) << 16) + ((in[1] & 0xFF) << 8) + (in[2] & 0xFF);

        return (in.length == (length + 3));
    }

    public static byte[] getLengthBytes(int i, boolean isList) {
        int listState = (isList) ? 1 : 0;
        try {
            if (i > 8388607) {
                System.out.println(i);
                throw new Exception("Attempted to encode too large of an object.");
            } else {
                byte[] result = new byte[3];

                result[0] = (byte) ((i >> 16) + (128 * listState)) ;
                result[1] = (byte) (i >> 8);
                result[2] = (byte) (i);

                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(150);
            return null;
        }
    }

    public static int fromBytes(byte[] in) {
        return Integer.parseUnsignedInt((new ENCItem(in)).toString(), 16);
    }

    private static class Tuple<K, V> {
        private K x;
        private V y;

        private Tuple(K x, V y) {
            this.x = x;
            this.y = y;
        }
    }
}
