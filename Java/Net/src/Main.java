import crypto.ecdsa.ECKey;
import network.Peer;
import org.bouncycastle.util.encoders.Hex;

import static util.NetUtil.fromBitSet;
import static util.NetUtil.fromByteArray;

public class Main {
    public static void main(String[] args) {
        Peer p = new Peer(new ECKey());

        System.out.println(Hex.toHexString(p.getKey().getAddress()));
        System.out.println(fromBitSet(fromByteArray(p.getKey().getAddress())));
        System.out.println(fromBitSet(fromByteArray(p.getKey().getAddress())).indexOf('1'));
    }
}
