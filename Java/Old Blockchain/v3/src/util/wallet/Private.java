package util.wallet;

import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import resources.Config;

import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;

import static crypto.Hash.*;
import static util.Base58.*;
import static util.Hex.*;

public class Private extends Sign implements Config {
    private static X9ECParameters x9Params = CustomNamedCurves.getByName(curve);

    public PrivateKey generateKey() {
        Security.addProvider(new BouncyCastleProvider());

        SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        }
        catch (Exception e) {
            System.out.println("The random number generator failed to initialize.");
            throw new RuntimeException(e);
        }

        KeyPairGenerator keyGen;
        try {
            keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
        }
        catch (Exception e) {
            System.out.println("The key generator algorithm was not found.");
            throw new RuntimeException(e);
        }

        ECParameterSpec ecSpec = new ECParameterSpec(x9Params.getCurve(), x9Params.getG(), x9Params.getN(), x9Params.getH(), x9Params.getSeed());

        try {
            keyGen.initialize(ecSpec, random);
        }
        catch (Exception e) {
            System.out.println("The key generator failed to initialize.");
            throw new RuntimeException(e);
        }

        KeyPair keyPair = keyGen.generateKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();
        return privateKey;
    }

    public String getWIF(PrivateKey privateKey) {
        Security.addProvider(new BouncyCastleProvider());

        String tempKey = getHex(((ECPrivateKey)privateKey).getS().toByteArray());

        while(tempKey.startsWith("0")) {
            tempKey = tempKey.substring(1);
        }

        tempKey = WIF_PREFIX + tempKey;

        String hash = sha256(sha256(tempKey));
        hash = hash.substring(0,8);

        String wif = tempKey + hash;
        return encode(fromHex(wif));
    }

    public String getPrivKey(PrivateKey privateKey) {
        Security.addProvider(new BouncyCastleProvider());
        return getHex(((ECPrivateKey)privateKey).getS().toByteArray());
    }

    public PrivateKey setPriv(String wif) {
        Security.addProvider(new BouncyCastleProvider());

        validateWIF(wif);

        String temp = getHex(decode(wif));
        while(temp.startsWith("0")) {
            temp = temp.substring(1);
        }

        temp = temp.substring(2, temp.length() - 8);

        BigInteger s = new BigInteger(temp, 16);
        ECParameterSpec ecParameterSpec = new ECNamedCurveParameterSpec(curve, x9Params.getCurve(), x9Params.getG(), x9Params.getN(), x9Params.getH(), x9Params.getSeed());
        ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(s, ecParameterSpec);
        KeyFactory factory;
        try {
            factory = KeyFactory.getInstance("ECDSA","BC");
        }
        catch(Exception e) {
            System.out.println("The key generator algorithm was not found.");
            throw new RuntimeException(e);
        }
        PrivateKey privateKey;
        try {
            privateKey = factory.generatePrivate(privateKeySpec);
        }
        catch (Exception e) {
            System.out.println("The keyspec on the private key string is invalid.");
            throw new RuntimeException(e);
        }
        return privateKey;
    }

    public void validateWIF(String wif) {
        Security.addProvider(new BouncyCastleProvider());
        wif = getHex(decode(wif));

        String prefix = wif.substring(0, 2);
        String key = wif.substring(2, wif.length() - 8);
        String chksum = wif.substring(wif.length() - 8);
        try{
            if(getHex(fromHex(prefix)).equals(getHex(fromHex(WIF_PREFIX)))) {
                String keyHash = sha256(sha256((prefix + key)));
                String checkSum = keyHash.substring(0,8);

                if(!chksum.equals(checkSum)) {
                    throw new RuntimeException("WIF Invalid: Checksum is invalid.");
                }
            }else {
                throw new RuntimeException("WIF Invalid: Prefix does not match.");
            }
        }
        catch(Exception e) {
            System.out.println("Wallet import failed.");
            throw new RuntimeException(e);
        }
    }
}
