package org.levk.UDP2Pv2.network.torrenty;

import org.bouncycastle.util.encoders.Hex;
import org.levk.UDP2Pv2.util.ByteArrayWrapper;
import org.levk.UDP2Pv2.util.datastore.Source;

import java.util.*;

import static org.levk.UDP2Pv2.util.ByteUtil.longToBytes;
import static org.levk.UDP2Pv2.util.ByteUtil.merge;
import static org.levk.UDP2Pv2.util.HashUtil.sha2B;

public class MetaData {
    public final static int MINIMUM_PIECE_SIZE = 256;
    public final static byte[] one = {0x01};
    public final static byte[] zero = {0x00};

    public byte[] encoded;
    public boolean parsed;

    public final byte[] length;

    public final byte[] pieceLength;

    public final byte[][] pieces;

    public MetaData(long length, long pieceLength, byte[][] pieces) {
        this.length = longToBytes(length, 8);
        this.pieceLength = longToBytes(pieceLength, 8);
        this.pieces = pieces;
    }

    public static MetaData generateMeta(byte[] in) {
        int[] pieceData = choosePieceSize(in.length);

        return new MetaData(pieceData[2], pieceData[0], generatePieceHashes(padData(in, pieceData[2]), pieceData[1], pieceData[0]));
    }

    public static List<Map.Entry<ByteArrayWrapper, byte[]>> prepStore(byte[] in) {
        List<Map.Entry<ByteArrayWrapper, byte[]>> out = new ArrayList<>();

        int[] dat = choosePieceSize(in.length);

        in = padData(in, dat[2]);

        int pieceCount = dat[1];
        int pieceSize = dat[0];

        for (int i = 0; i < pieceCount; i++) { ;
            byte[] tempPiece = new byte[pieceSize];
            System.arraycopy(in, (i) * pieceSize, tempPiece, 0, pieceSize);
            byte[] pieceHash = sha2B(tempPiece);

            Map.Entry<ByteArrayWrapper, byte[]> temp = new AbstractMap.SimpleEntry<>(new ByteArrayWrapper(pieceHash), tempPiece);
            out.add(temp);
        }

        return out;
    }

    public static byte[] getMessage(Source<byte[], byte[]> db, MetaData info) {
        byte[] message = new byte[0];

        long pieceCount = Long.parseUnsignedLong(Hex.toHexString(info.length)) / Long.parseUnsignedLong(Hex.toHexString(info.pieceLength));

        for (int i = 0; i < pieceCount; i++) {
            message = merge(message, db.get(info.pieces[i]));
        }

        return unpad(message);
    }

    public static int[] choosePieceSize(int length) {
        /* Returns array {pieceSize, pieceCount, and arraySize} */
        int naiveSize = (int)Math.ceil((double) length / (double) 1023);

        if (naiveSize > MINIMUM_PIECE_SIZE) {
            int[] out = {naiveSize, 1024, 1024 * (naiveSize)};

            return out;
        } else {
            int naiveCount = (int) Math.ceil((double) length / (double) MINIMUM_PIECE_SIZE);
            int[] out = {MINIMUM_PIECE_SIZE, naiveCount + 1, (naiveCount + 1) * 256};

            return out;
        }
    }

    public static byte[] padData(byte[] in, int desiredLength) {
        /* Uses the Keccak padding scheme */
        in = merge(in, one);

        while (in.length < (desiredLength - 1)) {
            in = merge(in, zero);
        }

        return merge(in, one);
    }

    public static byte[] unpad(byte[] in) {
        if (in[in.length - 1] == 0x01) {
            byte[] temp = new byte[in.length - 1];
            System.arraycopy(in, 0, temp, 0, temp.length);

            in = temp;
        } else {
            throw new RuntimeException("Padding should end with 0x01");
        }


        while (in[in.length - 1] == 0x00) {
            byte[] temp = new byte[in.length - 1];
            System.arraycopy(in, 0, temp, 0, temp.length);

            in = temp;
        }

        if (in[in.length - 1] == 0x01) {
            byte[] temp = new byte[in.length - 1];
            System.arraycopy(in, 0, temp, 0, temp.length);

            in = temp;
        } else {
            throw new RuntimeException("Padding should being with 0x01");
        }

        return in;
    }

    public static byte[][] generatePieceHashes(byte[] in, int pieceCount, int pieceSize) {
        byte[][] out = new byte[pieceCount][];

        for (int i = 0; i < pieceCount; i++) {
            byte[] temp = new byte[pieceSize];
            System.arraycopy(in, (i) * pieceSize, temp, 0, pieceSize);
            out[i] = sha2B(temp);
        }

        return out;
    }
}
