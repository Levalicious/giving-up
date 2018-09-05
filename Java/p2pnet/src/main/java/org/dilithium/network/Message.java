package org.dilithium.network;

public class Message {
    private byte[] rlpEncoded;
    /* Address to which this message
     * should be routed */
    private final byte[] target;

    /* First byte is an identifier
     * to determine the contents
     * of the payload, followed by
     * the encoded payload
     * (provided that message type
     * includes a payload */
    private final byte[] payload;

    /* keccak256 hash of the
     * payload field, including
     * the identifier byte */
    private final byte[] hash;

    /* R value of the secp256k1
     * signature of the hash */
    private final byte[] r;

    /* S value of the secp256k1
     * signature of the hash */
    private final byte[] s;

    /* V byte used to
     * reconstruct the
     * sender address from
     * the signature */
    private final byte v;

    public Message(byte[] target, byte[] payload, byte[] privkey) {
        this.target = target;
        this.payload = payload;
        this.hash =
    }

    public Message(byte[] payload, byte[] privkey) {

    }

    private void genSig(byte[] privKey) {

    }

    private void sign(byte[] privKeyBytes) {
        sign(ECKey.fromPrivate(privKeyBytes));
    }
}
