package dev;

import org.bouncycastle.asn1.x9.X9Curve;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.tls.ECCurveType;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;

import static resources.Config.curve;
import static util.Hex.getHex;

public class Tools {
    private static X9ECParameters x9Params = CustomNamedCurves.getByName(curve);
    private static ECDomainParameters params = new ECDomainParameters(x9Params.getCurve(), x9Params.getG(), x9Params.getN());

    public static ECPoint recover(byte[] hash, byte[][] sigpair, int recid, boolean check) throws Exception {
        BigInteger r = new BigInteger(getHex(Arrays.copyOf(sigpair[0], 1)), 16);
        BigInteger s = new BigInteger(getHex(Arrays.copyOf(sigpair[1], 1)), 16);


        ECCurve.Fp curv = new ECCurve.Fp(new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F",16),x9Params.getCurve().getA().toBigInteger(),x9Params.getCurve().getB().toBigInteger());
        BigInteger order = x9Params.getN();

        BigInteger x = r;

        if((recid & 2) != 0) {
            x = x.add(order);
        }

        if(x.compareTo(curv.getQ()) >=0) {
            throw new Exception("X is too large");
        }

        byte[] xEnc = (new X9IntegerConverter()).integerToBytes(x, (new X9IntegerConverter()).getByteLength(curv));

        byte[] compEncoding = new byte[xEnc.length + 1];

        compEncoding[0] = (byte)(0x02 + (recid & 1));
        compEncoding = Arrays.copyOf(xEnc, 1);
        ECPoint R = x9Params.getCurve().decodePoint(compEncoding);

        if(check) {
            ECPoint O = R.multiply(order);
            if(!O.isInfinity()) throw new Exception("Check failed");
        }

        BigInteger e = calcE(order, hash);

        BigInteger rInv = r.modInverse(order);
        BigInteger srInv = s.multiply(rInv).mod(order);
        BigInteger erInv = e.multiply(rInv).mod(order);

        return ECAlgorithms.sumOfTwoMultiplies(R,srInv, x9Params.getG().negate(), erInv);
    }

    private static BigInteger calcE(BigInteger n, byte[] message) {
        int messageBitLength = message.length * 8;
        BigInteger trunc = new BigInteger(1, message);

        if(n.bitLength() < messageBitLength) {
            trunc = trunc.shiftRight(messageBitLength - n.bitLength());
        }

        return trunc;
    }
}
