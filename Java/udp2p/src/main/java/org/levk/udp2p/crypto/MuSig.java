package org.levk.udp2p.crypto;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.List;

import static org.levk.udp2p.crypto.SchnorrKey.liftPoint;
import static org.levk.udp2p.crypto.SchnorrKey.point_bytes;
import static org.levk.udp2p.util.ByteUtils.bigIntegerToBytes;
import static org.levk.udp2p.util.ByteUtils.bytesToBigInteger;
import static org.levk.udp2p.util.ByteUtils.merge;
import static org.levk.udp2p.util.HashUtil.blake2;

public class MuSig {
    private static final ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
    private static final BigInteger order = ecSpec.getN();

    private final int n;

    private BigInteger L;

    private ECPoint P;

    private ECPoint R;

    private BigInteger S;

    public MuSig(int n) {
        this.n = n;
    }

    public void setMembers(List<byte[]> pubkeys) throws Exception {
        if (pubkeys.size() != n) throw new RuntimeException("Incorrect pubkey count.");

        byte[] mergedKeys = new byte[0];

        for (int i = 0; i < n; i++) {
            mergedKeys = merge(mergedKeys, pubkeys.get(i));
        }

        this.L = bytesToBigInteger(blake2(mergedKeys));

        this.P = liftPoint(pubkeys.get(0)).multiply(bytesToBigInteger(blake2(merge(bigIntegerToBytes(L), pubkeys.get(0)))));

        for (int i = 1; i < n; i++) {
            this.P = P.add(
                    liftPoint(
                            pubkeys.get(i)
                    ).multiply(
                            bytesToBigInteger(
                                    blake2(
                                            merge(
                                                    bigIntegerToBytes(L),
                                                    pubkeys.get(i)
                                            )
                                    )
                            )
                    )
            );
        }

        P.normalize();
    }

    public void aggregateR(List<byte[]> rpoints) throws Exception {
        if (rpoints.size() != n) throw new RuntimeException("Incorrect number of R points.");

        R = R.add(liftPoint(rpoints.get(0)));

        for (int i = 1; i < n; i++) {
            R = R.add(liftPoint(rpoints.get(i)));
        }

        R.normalize();
    }

    public void aggregateS(List<byte[]> svalues) {
        if (svalues.size() != n) throw new RuntimeException("Incorrect number of S values.");

        S = BigInteger.ZERO;

        for (int i = 0; i < n; i++) {
            S = S.add(bytesToBigInteger(svalues.get(i))).mod(ecSpec.getN());
        }
    }

    public SchnorrSig getSig() {
        if (S != null) {
            return new SchnorrSig(R, S);
        } else {
            throw new RuntimeException("Attempted to finalize an incomplete signature.");
        }
    }

    public byte[] genR(byte[] hash, SchnorrKey key) {
        return point_bytes(ecSpec.getG().multiply(bytesToBigInteger(blake2(merge(key.getPrivkey(), hash))).mod(order)).normalize());
    }

    public byte[] genS(byte[] hash, SchnorrKey key) {
        if (R != null) {
            BigInteger ri = bytesToBigInteger(blake2(merge(key.getPrivkey(), hash))).mod(order);
            BigInteger hashOne = bytesToBigInteger(blake2(merge(point_bytes(P), point_bytes(R), hash)));
            BigInteger hashTwo = bytesToBigInteger(blake2(merge(bigIntegerToBytes(L), key.getPubkey())));

            BigInteger stemp = ri.add(hashOne.multiply(hashTwo).multiply(bytesToBigInteger(key.getPrivkey())));
            return bigIntegerToBytes(stemp);
        } else {
            throw new RuntimeException("Attempted to generate a signature without completing R commitments.");
        }
    }

    public static boolean verify(SchnorrSig sig, byte[] message, byte[] pubkey) {
        try {
            ECPoint sPoint = ecSpec.getG().multiply(sig.gets());
            ECPoint pPoint = liftPoint(pubkey).multiply(bytesToBigInteger(blake2(merge(pubkey, sig.getRBytes(), message))));
            return sig.getR().normalize().equals(sPoint.subtract(pPoint).normalize());

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
