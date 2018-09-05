import com.google.common.primitives.Bytes;
import dev.ECKey;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import user.Wallet;

import java.io.IOException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static crypto.hash.Hash.blake256;
import static dev.Tools.recover;
import static resources.Config.curve;
import static util.Hex.fromHex;
import static util.Hex.getHex;

public class Main {
    public static void main(String[] args) throws Exception{
        ECKey wal = new ECKey();
        System.out.println("Public Address : " + "0x" + getHex(wal.getAddress()));
        System.out.println("Message Signed : \"Hello\"");
        ECKey.ECDSASignature signed = wal.sign(fromHex(blake256("Hello")));
        byte[] r = signed.r.toByteArray();
        byte[] s = signed.s.toByteArray();
        byte v = signed.v;

        System.out.println();
        System.out.println("Signature :");
        System.out.println("R : " + getHex(r));
        System.out.println("S : " + getHex(s));
        System.out.println("V : " + Byte.toString(v));
        ECKey.ECDSASignature signature = ECKey.ECDSASignature.fromComponents(r,s,v);
        String recoveredPubKey = getHex(ECKey.signatureToKey(fromHex(blake256("Hello")), signature).getAddress());
        System.out.println();
        System.out.println("Recovered Public Address: " + "0x" + recoveredPubKey);

        /*
        System.out.println(sigBytes.length);
        byte[] r = Arrays.copyOfRange(sigBytes ,0,32);
        byte[] s = Arrays.copyOfRange(sigBytes,32,sigBytes.length);
        System.out.println("0x" + getHex(r));
        System.out.println("0x" + getHex(s));
        */


        /*
        Blockchain blockchain = new Blockchain();

        Miner forger = new Miner();

        long start = System.currentTimeMillis();
        System.out.println();
        System.out.println();
        for(int i = 0; i < 200; i++){
            forger.forgeRandom(blockchain);
        }

        if(blockchain.checkValid()){
            System.out.println("Blockchain is valid.");
        }else{
            System.out.println("Blockchain is invalid.");
        }

        long end = System.currentTimeMillis();
        Files.write(Paths.get("out.txt"), blockchain.toJson().getBytes());
        System.out.println("Blockchain written to out.txt.");
        System.out.println("Took " + (end - start) + "ms");
        */
    }

    public static byte[] encode(byte[] in) {
        byte[] one = new byte[1];
        one[0] = (byte) 0x02;
        return Bytes.concat(one, in);
    }
}
