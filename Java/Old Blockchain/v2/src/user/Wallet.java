package user;

import java.io.IOException;
import java.security.*;

import utils.WalletUtils;

import static utils.Base58.decode;
import static utils.Base58.encode;
import static utils.SigUtils.*;
import static utils.HexUtils.*;

public class Wallet extends WalletUtils{
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Wallet() {
        newWallet();
    }

    private Wallet(String wif) throws GeneralSecurityException {
        importWIF(wif);
    }

    public static Wallet importWallet(String wif) throws GeneralSecurityException {
        try{
            Wallet wallet = new Wallet(wif);
            return wallet;
        }
        catch(Exception e){
            System.out.println("Errors were experienced while processing this WIF.");
            System.out.println("Either it contains a formatting error, or is invalid.");
            return null;
        }
    }

    public String sign(String in) throws GeneralSecurityException {
        byte[] temp = signData(privateKey, in.getBytes());
        System.out.println(getHex(temp));
        return encode(temp);
    }

    public boolean verify(String publicKey, String message, String sig) throws GeneralSecurityException, IOException {
        System.out.println(getHex(decode(sig)));
        return verifySig(setPublic(publicKey), message.getBytes(), decode(sig));
    }

    @Override
    public String toString() {
        try{
            String s = "+====+ Public Address +====+\n";
            s = s + getAddress() + "\n";
            s = s + "\n";
            s = s + "+====+ WIF +====+\n";
            s = s + getWIF() + "\n";
            s = s + "\n";
            return s;
        }
        catch(Exception e){
            System.out.println("Errors were experienced while processing this wallet.");
        }
        return "ERROR";
    }

    public String getKeys() {
        try{
            String s = "+====+ Public Key +====+\n";
            s = s + getPubKey() + "\n";
            s = s + "\n";
            s = s + "+====+ Private Key +====+\n";
            s = s + getPrivKey() + "\n";
            s = s + "\n";
            return s;
        }
        catch(Exception e){
            System.out.println("Errors were experienced while processing this wallet.");
        }
        return "ERROR";
    }

    public void newWallet(){
        this.privateKey = generateKeyPair();
    }

    public String getPubKey() throws GeneralSecurityException{
        this.publicKey = setPublic(this.privateKey);
        return getPublicKey(this.publicKey);
    }

    public String getPrivKey() {
        return getPrivateKey(this.privateKey);
    }

    public String getAddress() throws GeneralSecurityException{
        this.publicKey = setPublic(this.privateKey);
        return getPublicAddress(this.publicKey);
    }

    public String getWIF(){
        return getWIF(this.privateKey);
    }

    public void importWIF(String wif) throws GeneralSecurityException{
        this.privateKey = setPrivate(wif);
    }
}
