package org.dilithium;

import org.bouncycastle.util.encoders.Hex;
import org.dilithium.crypto.ecdsa.ECKey;

public class Start {
    public static void main(String[] args) {
        System.out.println(Hex.toHexString((new ECKey()).getAddress()));
    }
}
