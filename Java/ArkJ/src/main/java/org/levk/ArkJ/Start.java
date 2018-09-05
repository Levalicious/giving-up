package org.levk.ArkJ;

import org.bouncycastle.util.encoders.Hex;
import org.levk.ArkJ.crypto.MuSig;
import org.levk.ArkJ.crypto.SchnorrKey;
import org.levk.ArkJ.crypto.SchnorrSig;

import java.util.ArrayList;
import java.util.List;

import static org.levk.ArkJ.util.HashUtil.blake2;

public class Start {
    public static void main(String[] args) throws Exception {
        byte[] dat = "Hello".getBytes();

        byte[] message = blake2(dat);

        MuSig muSig = new MuSig(3);

        SchnorrKey key1 = new SchnorrKey();
        SchnorrKey key2 = new SchnorrKey();
        SchnorrKey key3 = new SchnorrKey();

        List<byte[]> pubkeys = new ArrayList<>();

        pubkeys.add(key1.getPubkey());
        pubkeys.add(key2.getPubkey());
        pubkeys.add(key3.getPubkey());

        muSig.setMembers(pubkeys);

        List<byte[]> rPoints = new ArrayList<>();
        List<byte[]> precommits = new ArrayList<>();

        rPoints.add(muSig.genR(message, key1));
        rPoints.add(muSig.genR(message, key2));
        rPoints.add(muSig.genR(message, key3));

        precommits.add(blake2(rPoints.get(0)));
        precommits.add(blake2(rPoints.get(1)));
        precommits.add(blake2(rPoints.get(2)));

        muSig.precommit(precommits);

        System.out.println(muSig.testCommitments(rPoints));

        muSig.aggregateR(rPoints);

        List<byte[]> sValues = new ArrayList<>();

        sValues.add(muSig.genS(message, key1));
        sValues.add(muSig.genS(message, key2));
        sValues.add(muSig.genS(message, key3));

        muSig.aggregateS(sValues);

        byte[] aggregKey = muSig.getPubKey();

        SchnorrSig sig = muSig.getSig();

        System.out.println(MuSig.verify(sig, message, aggregKey));
    }
}
