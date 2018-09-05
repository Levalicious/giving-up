package dev;


import util.byteUtils.ByteUtil;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;

public class FailHash {
    public static String hash(String x) {
        byte[] pArr = new byte[32];
        byte[] qArr = new byte[64];
        byte[] data = {(byte)0x80};

        try {
            data = x.getBytes("UTF-8");
            if(x.getBytes("UTF-8").length > 32) {
                data = Arrays.copyOf(data, 32);
            }


        }catch (Exception e) {
            System.out.println("Your computer does not support the necessary encodings.");
            System.exit(0);
        }

        byte[] temp = new byte[32 - data.length];

        for(int i = 0; i < temp.length; i++) {
            temp[i] = (byte)((data[i % data.length])^((byte)0x5e));
        }

        pArr = ByteUtil.concat(data, temp);
        qArr = Arrays.copyOf(data, 64);

        BigInteger p = genpr2(new BigInteger(pArr));
        BigInteger q = genpr2(new BigInteger(qArr));

        return new BigInteger(pArr).pow(2).mod(p.multiply(q)).toString(16).substring(0, 63);
    }

    private static BigInteger genpr2(BigInteger in)
    {
        BigInteger c = in;

        for (; ; )
        {
            if (c.isProbablePrime(1) == true) break;

            c = c.subtract(BigInteger.ONE);
        }
        return (c);
    }
}
