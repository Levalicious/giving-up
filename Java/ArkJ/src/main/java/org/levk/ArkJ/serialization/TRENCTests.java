package org.levk.ArkJ.serialization;

import org.bouncycastle.util.encoders.Hex;

import java.security.SecureRandom;

import static org.levk.ArkJ.util.ByteUtils.merge;

public class TRENCTests {
    public static void main(String[] args) {
        byte[] data = new byte[10];
        SecureRandom rand = new SecureRandom();
        rand.nextBytes(data);
        byte[] dat = null;

        byte[] enc = TRENC.encode(data, dat, data);

        byte[] randByteToMessShitUp = {(byte)0x01};

        byte[] messedUpEnc = merge(enc, randByteToMessShitUp);

        System.out.println(Hex.toHexString(enc));
        ENCList list = TRENC.decode(enc);

        System.out.println(Hex.toHexString(list.get(0).getEncData()));
        System.out.println(Hex.toHexString(list.get(1).getEncData()));
        System.out.println(Hex.toHexString(list.get(2).getEncData()));

        System.out.println(TRENC.isEncoded(enc));
        System.out.println(TRENC.isEncoded(messedUpEnc));
    }
}
