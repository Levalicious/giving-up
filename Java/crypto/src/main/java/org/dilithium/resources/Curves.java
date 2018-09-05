package org.dilithium.resources;

import org.dilithium.Hex;
import org.dilithium.math.Edwards.ECurve;
import org.dilithium.math.Point;
import org.dilithium.math.Weierstrass.WCurve;

import java.math.BigInteger;

import static org.dilithium.math.ModArithmetic.divide;

public class Curves {
    public static WCurve secp256k1 = new WCurve(BigInteger.TWO.pow(256).subtract(BigInteger.TWO.pow(32)).subtract(BigInteger.valueOf(977)), BigInteger.ZERO, BigInteger.valueOf(7), new Point(new BigInteger(Hex.fromHex("0079be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798")), new BigInteger(Hex.fromHex("00483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8"))), new BigInteger(Hex.fromHex("00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141")), BigInteger.ONE);

    // public static ECurve e521 = new ECurve(BigInteger.valueOf(1), BigInteger.valueOf(-376014), new Point(new BigInteger(Hex.fromHex("00752cb45c48648b189df90cb2296b2878a3bfd9f42fc6c818ec8bf3c9c0c6203913f6ecc5ccc72434b1ae949d568fc99c6059d0fb13364838aa302a940a2f19ba6c")), new BigInteger(Hex.fromHex("00C"))), BigInteger.TWO.pow(521).subtract(BigInteger.ONE), new BigInteger(Hex.fromHex("007ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffd15b6c64746fc85f736b8af5e7ec53f04fbd8c4569a8f1f4540ea2435f5180d6b")));

    public static ECurve edwards25519 = new ECurve(BigInteger.valueOf(-1), divide(BigInteger.valueOf(-121665), BigInteger.valueOf(121666), BigInteger.TWO.pow(255).subtract(BigInteger.valueOf(19))), new Point(new BigInteger("15112221349535400772501151409588531511454012693041857206046113283949847762202"), new BigInteger("46316835694926478169428394003475163141307993866256225615783033603165251855960")), BigInteger.TWO.pow(255).subtract(BigInteger.valueOf(19)), BigInteger.TWO.pow(252).add(new BigInteger("27742317777372353535851937790883648493")));
    /* (BigInteger a, BigInteger d, Point G, BigInteger p, BigInteger n) */
}
