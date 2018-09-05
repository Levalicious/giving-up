package utils;

import com.sun.org.apache.xml.internal.security.algorithms.implementations.SignatureECDSA;
import org.bouncycastle.asn1.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECPoint;

import static utils.Base58.decode;
import static utils.Base58.encode;
import static utils.HexUtils.getHex;

public class SigUtils {
    public static byte[] signData(PrivateKey privateKey, byte[] in) {
        Security.addProvider(new BouncyCastleProvider());
        byte[] sigFinal;
        Signature sig;
        try {
            sig = Signature.getInstance("SHA256withECDSA","BC");
            sig.initSign(privateKey);
            sig.update(in);
            sigFinal = sig.sign();
            //getSigInts(sigFinal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sigFinal;
    }

    public static void getSigInts(byte[] signature) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        ASN1Primitive asn1 = toAsn1Primitive(signature);

        if (asn1 instanceof ASN1Sequence) {
            ASN1Sequence asn1Sequence = (ASN1Sequence) asn1;
            ASN1Encodable[] asn1Encodables = asn1Sequence.toArray();
            for (ASN1Encodable asn1Encodable : asn1Encodables) {
                ASN1Primitive asn1Primitive = asn1Encodable.toASN1Primitive();
                if (asn1Primitive instanceof ASN1Integer) {
                    ASN1Integer asn1Integer = (ASN1Integer) asn1Primitive;
                    BigInteger integer = asn1Integer.getValue();
                    System.out.println(integer.toString());
                }
            }
        }
    }

    public static boolean verifySig(PublicKey publicKey, byte[] in, byte[] signature) throws GeneralSecurityException {
        Signature verify = Signature.getInstance("ECDSA","BC");
        verify.initVerify(publicKey);
        verify.update(in);
        return verify.verify(signature);
    }

    private static ASN1Primitive toAsn1Primitive(byte[] data) throws Exception
    {
        try (ByteArrayInputStream inStream = new ByteArrayInputStream(data);
             ASN1InputStream asnInputStream = new ASN1InputStream(inStream);)
        {
            return asnInputStream.readObject();
        }
    }

    /*
    public static boolean verify(String data, String sig) {
        Security.addProvider(new BouncyCastleProvider());
        byte[] in = decode(sig);
        try {
            in = Signature.getInstance("ECDSA","BC");
            sig.
                    verify = Signature.getInstance("ECDSA","BC");
            verify.;
        }
    }
    */
}
