package resources;

import crypto.ECDSA.ECKey;
import kademlia.NodeInfo;

import java.math.BigInteger;

public class Constants {
    private static final BigInteger SECP256K1N = new BigInteger("fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141", 16);

    /**
     * Introduced in the Homestead release
     */
    public static BigInteger getSECP256K1N() {
        return SECP256K1N;
    }

    public static NodeInfo node = new NodeInfo();
}
