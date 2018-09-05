package user;

import util.wallet.Public;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Wallet extends Public {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Wallet() {
        newWallet();
    }

    private Wallet(String wif) {
        setPriv(wif);
    }

    public static Wallet importWallet(String wif) {
        Wallet wallet = new Wallet(wif);
        return wallet;
    }

    public String sign(String in) {
        return signData(privateKey, in);
    }

    public boolean verify(String pubKey, String data, String sig) {
        return verifySig(setPublic(pubKey), data, sig);
    }

    @Override
    public String toString() {
        String s = "+====+ Public Address +====+\n";
        s = s + getAddress(setPub(this.privateKey)) + "\n";
        s = s + "\n";
        s = s + "+====+ WIF +====+\n";
        s = s + getWIF(this.privateKey) + "\n";
        s = s + "\n";
        return s;
    }

    public String getKeys() {
        String s = "+====+ Public Key +====+\n";
        s = s + getPubKey() + "\n";
        s = s + "\n";
        s = s + "+====+ Private Key +====+\n";
        s = s + getPrivKey(this.privateKey) + "\n";
        s = s + "\n";
        return s;
    }

    public void newWallet() {
        this.privateKey = generateKey();
    }

    public String getPubKey() {
        this.publicKey = setPub(this.privateKey);
        return getPubKey(this.publicKey);
    }
}
