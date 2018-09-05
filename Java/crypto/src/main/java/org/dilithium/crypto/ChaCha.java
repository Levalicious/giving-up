package org.dilithium.crypto;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ChaCha {
    private final static short ROUNDS = 20;

    private static int ROTL(int a, int b) {
        return (((a) << (b) | ((a) >>> (32 - (b)))));
    }

    public static void QR(int[] arr, int a, int b, int c, int d) {
        arr[a] += arr[b];
        arr[d] ^= arr[a];
        arr[d] = ROTL(arr[d], 16);
        arr[c] += arr[d];
        arr[b] ^= arr[c];
        arr[b] = ROTL(arr[b], 12);
        arr[a] += arr[b];
        arr[d] ^= arr[a];
        arr[d] = ROTL(arr[d], 8);
        arr[c] += arr[d];
        arr[b] ^= arr[c];
        arr[b] = ROTL(arr[b], 7);
    }

    public static void chacha_block(int[] in) {
        for (int i = 0; i < ROUNDS; i += 2) {
            /* Odd round */
            QR(in, 0, 4, 8, 12);
            QR(in, 1, 5, 9, 13);
            QR(in, 2, 6, 10, 14);
            QR(in, 3, 7, 11, 15);

            /* Even round */
            QR(in, 0, 5, 10, 15);
            QR(in, 1, 6, 11, 12);
            QR(in, 2, 7, 8, 13);
            QR(in, 3, 4, 9, 14);
        }
    }

    public static int[] ChaCha20(byte[] key, byte[] nonce) {
        int[] matrix = new int[16];

        matrix[0] = 0x61707865;
        matrix[1] = 0x3320646e;
        matrix[2] = 0x79622d32;
        matrix[3] = 0x6b206574;
        matrix[4] = (key[0] & 0xff) | ((key[1] & 0xff) << 8) | ((key[2] & 0xff) << 16) | ((key[3] & 0xff) << 24);
        matrix[5] = (key[4] & 0xff) | ((key[5] & 0xff) << 8) | ((key[6] & 0xff) << 16) | ((key[7] & 0xff) << 24);
        matrix[6] = (key[8] & 0xff) | ((key[9] & 0xff) << 8) | ((key[10] & 0xff) << 16) | ((key[11] & 0xff) << 24);
        matrix[7] = (key[12] & 0xff) | ((key[13] & 0xff) << 8) | ((key[14] & 0xff) << 16) | ((key[15] & 0xff) << 24);
        matrix[8] = (key[16] & 0xff) | ((key[17] & 0xff) << 8) | ((key[18] & 0xff) << 16) | ((key[19] & 0xff) << 24);
        matrix[9] = (key[20] & 0xff) | ((key[21] & 0xff) << 8) | ((key[22] & 0xff) << 16) | ((key[23] & 0xff) << 24);
        matrix[10] = (key[24] & 0xff) | ((key[25] & 0xff) << 8) | ((key[26] & 0xff) << 16) | ((key[27] & 0xff) << 24);
        matrix[11] = (key[28] & 0xff) | ((key[29] & 0xff) << 8) | ((key[30] & 0xff) << 16) | ((key[31] & 0xff) << 24);

        if (nonce.length == 8) {
            matrix[12] = 0;
            matrix[13] = 0;
            matrix[14] = (nonce[0] & 0xff) | ((nonce[1] & 0xff) << 8) | ((nonce[2] & 0xff) << 16) | ((nonce[3] & 0xff) << 24);
            matrix[15] = (nonce[4] & 0xff) | ((nonce[5] & 0xff) << 8) | ((nonce[6] & 0xff) << 16) | ((nonce[7] & 0xff) << 24);
        }

        return matrix;
    }

    public static byte[] getMatrix(byte[] key, byte[] nonce) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int[] matrix = ChaCha20(key, nonce);

        for (int i = 0; i < 16; i++) {
            out.write(intToBytes(matrix[i]));
        }

        return out.toByteArray();
    }

    public static byte[] intToBytes(int val){
        return ByteBuffer.allocate(Integer.BYTES).putInt(val).array();
    }

    public static byte[] encrypt(byte[] key, byte[] message) throws Exception {
        byte[][] messageParts = partition(message);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        for (int i = 0; i < messageParts.length; i++) {
            byte[] temp = new byte[messageParts[i].length];
            encrypt(ChaCha20(key, bigIntegerToBytes(BigInteger.valueOf(i), 8)), temp, messageParts[i], messageParts[i].length);
            out.write(temp);
        }

        return out.toByteArray();
    }

    public static void encrypt(int[] matrix, byte[] out, byte[] src, int len) {
        int[] x = new int[16];
        byte[] output = new byte[64];
        int i = 0, opos = 0, spos = 0;

        while (len > 0) {
            for (i = 0; i < 16; i++) x[i] = matrix[i];
            int[] temp = new int[16];
            chacha_block(x);

            for (i = 0; i < 16; i++) x[i] += matrix[i];
            for (i = 0; i < 16; i++) intToLittleEndian(x[i], output, 4 * i);

            matrix[12] += 1;

            if (matrix[12] <= 0) {
                matrix[13] += 1;
            }

            if (len <= 64) {
                for (i = len; i-- > 0;) {
                    out[i + opos] = (byte) (src[i + spos] ^ output[i]);
                }

                return;
            }

            for (i = 64; i-- > 0;) {
                out[i + opos] = (byte) (src[i + spos] ^ output[i]);
            }

            len -= 64;
            spos += 64;
            opos += 64;
        }
    }

    private static void intToLittleEndian (int n, byte[] temp, int off) {
        temp[off] = (byte)n;
        temp[off + 1] = (byte)(n >>> 8);
        temp[off + 2] = (byte)(n >>> 16);
        temp[off + 3] = (byte)(n >>> 24);
    }

    public static int[][] decompress(int[] in) {
        int[][] out = new int[16][1];

        for (int i = 0; i < 16; i++) {
            out[i][0] = in[i];
        }

        return out;
    }

    public static int[] compress(int[][] in) {
        int[] out = new int[16];

        for (int i = 0; i < 16; i++) {
            out[i] = in[i][0];
        }

        return out;
    }

    private static byte[][] partition(byte[] in) {
        int partitionCount =  (int)Math.ceil((double)in.length / (double) 64);

        byte[][] temp = new byte[partitionCount][64];

        for (int i = 0; i < partitionCount; i++) {
            if (in.length < (64 * (i + 1))) {
                temp[i] = new byte[(in.length - (64 * i))];
            }

            for(int j = 0; (j < 64 && (64 * i + j) < in.length); j++) {
                temp[i][j] = in[(64 * i + j)];
            }
        }

        return temp;
    }

    /**
     * The regular {@link BigInteger#toByteArray()} method isn't quite what we often need:
     * it appends a leading zero to indicate that the number is positive and may need padding.
     *
     * @param b the integer to format into a byte array
     * @param numBytes the desired size of the resulting byte array
     * @return numBytes byte long array.
     */
    public static byte[] bigIntegerToBytes(BigInteger b, int numBytes) {
        if (b == null)
            return null;
        byte[] bytes = new byte[numBytes];
        byte[] biBytes = b.toByteArray();
        int start = (biBytes.length == numBytes + 1) ? 1 : 0;
        int length = Math.min(biBytes.length, numBytes);
        System.arraycopy(biBytes, start, bytes, numBytes - length, length);
        return bytes;
    }

    /**
     * Cast hex encoded value from byte[] to BigInteger
     * null is parsed like byte[0]
     *
     * @param bb byte array contains the values
     * @return unsigned positive BigInteger value.
     */
    public static BigInteger bytesToBigInteger(byte[] bb) {
        return (bb == null || bb.length == 0) ? BigInteger.ZERO : new BigInteger(1, bb);
    }
}
