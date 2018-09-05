package org.levk.simpleBlob;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Blob {
    private static byte[][] partition(byte[] in, int partitionSize) {
        int partitionCount = (int)Math.ceil((double)in.length / (double)partitionSize);

        byte[][] temp = new byte[partitionCount][partitionSize];

        for (int i = 0; i < partitionCount; i++) {
            if (in.length < (partitionSize * (i + 1))) {
                temp[i] = new byte[(in.length - (partitionSize * i))];
            }

            for(int j = 0; (j < partitionSize && (partitionSize * i + j) < in.length); j++) {
                temp[i][j] = in[(partitionSize * i + j)];
            }
        }

        return temp;
    }

    public static byte[][] blobifyStep1(byte[] in) {
        int chunkCount = (int)Math.ceil((double)in.length / (double)31);

        byte[][] blob = new byte[chunkCount][];
        int processed = 0;
        for (int i = 0; i < chunkCount; i++) {
            byte[] chunk = new byte[32];

            for (int j = 0; j < 32; j++) {
                if (j == 0) {
                    if (in.length - 31 > i * 31) {
                        chunk[j] = (byte)0x00;
                    } else {
                        byte count = (byte)((byte)(in.length - processed) & (byte)0xFF);
                        chunk[j] = count;
                    }
                } else {
                    if (processed < in.length) {
                        chunk[j] = in[processed];
                        processed++;
                    } else {
                        chunk[j] = (byte)0x00;
                    }
                }
            }

            blob[i] = chunk;
        }

        return blob;
    }

    private static byte[] blobifyStep2(byte[] in) {
        try {
            byte[][] temp = blobifyStep1(in);

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            for (int i = 0; i < temp.length; i++) {
                out.write(temp[i]);
            }

            return out.toByteArray();
        } catch (IOException i) {
            i.printStackTrace();
            System.exit(150);
            return null;
        }
    }

    private static ArrayList<byte[]> deblobifyStep1(byte[] in) {
        byte[][] temp = partition(in, 32);
        ArrayList<byte[]> tempList = new ArrayList<>();

        for (int i = 0; i < temp.length; i++) {
            tempList.add(temp[i]);
        }

        return tempList;
    }

    private static byte[] deblobifyStep2(ArrayList<byte[]> in) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            for (int i = 0; i < in.size(); i++) {
                byte[] current = in.get(i);

                if (current[0] == 0x00) {
                    out.write(Arrays.copyOfRange(current, 1, 32));
                } else {
                    int dist = current[0];
                    out.write(Arrays.copyOfRange(current, 1, dist + 1));
                }
            }

            return out.toByteArray();
        } catch (IOException i) {
            i.printStackTrace();
            System.exit(150);
            return null;
        }
    }

    public static byte[] blobify(byte[] in) {
        return blobifyStep2(in);
    }

    public static byte[] deblobify(byte[] in) {
        return deblobifyStep2(deblobifyStep1(in));
    }

    public static boolean isBlobby(byte[] in) {
        if (in.length <= 0) {
            return false;
        }

        if (!(in.length % 32 == 0)) {
            return false;
        }

        byte[][] temp = partition(in, 32);

        for (int i = 0; i < temp.length - 1; i++) {
            if (temp[i][0] != 0x00) {
                return false;
            }
        }

        return true;
    }
}
