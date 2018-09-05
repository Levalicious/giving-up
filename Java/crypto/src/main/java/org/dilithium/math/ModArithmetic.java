package org.dilithium.math;

import java.math.BigInteger;

public class ModArithmetic {
    public static BigInteger add(BigInteger a, BigInteger b, BigInteger mod) {
        return a.add(b).mod(mod);
    }

    public static BigInteger subtract(BigInteger a, BigInteger b, BigInteger mod) {
        return a.subtract(b).mod(mod);
    }

    public static BigInteger multiply(BigInteger a, BigInteger b, BigInteger mod) {
        return a.multiply(b).mod(mod);
    }

    public static BigInteger divide(BigInteger a, BigInteger b, BigInteger mod) {
        return a.multiply(b.modInverse(mod)).mod(mod);
    }

    public static BigInteger exponentiate(BigInteger a, BigInteger b, BigInteger mod) {
        return a.modPow(b, mod);
    }
}
