package org.dilithium.network.messages;

import org.bouncycastle.util.BigIntegers;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.dilithium.crypto.ecdsa.ECKey;
import org.dilithium.util.ByteUtil;
import org.dilithium.util.serialization.RLP;
import org.dilithium.util.serialization.RLPElement;
import org.dilithium.util.serialization.RLPItem;
import org.dilithium.util.serialization.RLPList;

import static com.cedarsoftware.util.ArrayUtilities.isEmpty;
import static org.dilithium.crypto.Hash.keccak256;
import static org.dilithium.util.ByteUtil.EMPTY_BYTE_ARRAY;

public class Message {
    /* Wether this message has a target or not */
    private boolean targetted;
    
    /* The RLP encoding for this message */
    private byte[] rlpEncoded;

    /* The raw RLP encoding for this message
     * (empty r and s values) */
    private byte[] rlpRaw;

    /* Whether the non-encoded fields have
     * had values assigned to them yet */
    private boolean parsed = false;

    /* The target recipient of the payload */
    private byte[] target;

    /* An identifier as to what type of
     * message this message contains. */
    private int messageType;

    /* The payload of this message */
    private byte[] payload;

    /* The r value of the signature
     * on this message */
    private byte[] r;

    /* The s value of the signature
     * on this message */
    private byte[] s;

    /* The v byte to reconstruct
     * the sender address of this
     * message */
    private byte v;

    /* Hash of message. For validation, bloom
     * filters, and spam protection. */
    private byte[] hash;

    /* Reconstruct message from RLP encoding */
    public Message(@NonNull byte[] rlpEncoded) {
        this.rlpEncoded = rlpEncoded;
    }

    /* Construct new message */
    public Message(@NonNull byte[] target, @NonNull int messageType, @NonNull byte[] payload, @NonNull byte[] privkey) {
        this.targetted = true;
        this.target = target;
        this.messageType = messageType;
        this.payload = payload;

        parsed = true;

        this.sign(privkey);

        this.hash = getHash();
    }

    public Message(int messageType, byte[] payload, byte[] privkey) {
        this.targetted = false;
        this.messageType = messageType;
        this.payload = payload;

        parsed = true;

        this.sign(privkey);

        this.hash = getHash();
    }

    /* TODO: Set up constructors to construct based on messageType - Completely ignore payload in certain cases */

    public synchronized void rlpParse() {
        if (parsed) return;
        
        try {
            RLPList decodedMessageList = RLP.decode2(rlpEncoded);
            RLPList message = (RLPList) decodedMessageList.get(0);
            
            if(message.size() == 5) {
                urlpParse(message);
            } else {
                trlpParse(message);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error on parsing RLP", e);
        }
        
    }
    
    /* RLP parse for targetted messages */
    private synchronized void trlpParse(RLPList message) {
        try {
            if (message.size() > 6) throw new RuntimeException("Too many RLP elements");
            for (RLPElement rlpElement : message) {
                if(!(rlpElement instanceof RLPItem)) throw new RuntimeException("tMessage RLP elements shouldn't be lists");
            }

            this.targetted = true;
            this.target = message.get(0).getRLPData();
            this.messageType = ByteUtil.byteArrayToInt(message.get(1).getRLPData());
            this.payload = message.get(2).getRLPData();
            this.r = message.get(3).getRLPData();
            this.s = message.get(4).getRLPData();
            this.v = (byte)ByteUtil.byteArrayToInt(message.get(5).getRLPData());
            this.parsed = true;
            this.hash = getHash();
        } catch (Exception e) {
            throw new RuntimeException("Error on parsing RLP", e);
        }
    }
    
    /*RLP parse for untargetted messages */
    private synchronized void urlpParse(RLPList message) {
        try {
            if (message.size() > 5) throw new RuntimeException("Too many RLP elements");
            for (RLPElement rlpElement : message) {
                if(!(rlpElement instanceof RLPItem)) throw new RuntimeException("uMessage RLP elements shouldn't be lists");
            }

            this.targetted = false;
            this.messageType = ByteUtil.byteArrayToInt(message.get(0).getRLPData());
            this.payload = message.get(1).getRLPData();
            this.r = message.get(2).getRLPData();
            this.s = message.get(3).getRLPData();
            this.v = (byte)ByteUtil.byteArrayToInt(message.get(4).getRLPData());
            this.parsed = true;
            this.hash = getHash();
        } catch (Exception e) {
            throw new RuntimeException("Error on parsing RLP", e);
        }
    }

    public byte[] getTarget() {
        if (targetted) {
            return target;
        } else {
            return null;
        }
    }

    public int getMessageType() {
        return messageType;
    }

    public byte[] getPayload() {
        rlpParse();
        return this.payload;
    }

    public ECKey.ECDSASignature getSig() {
        return ECKey.ECDSASignature.fromComponents(r,s,v);
    }

    private void sign(byte[] privKeyBytes) {
        this.sign(ECKey.fromPrivate(privKeyBytes));
    }

    private void sign(ECKey key) {
        ECKey.ECDSASignature temp = key.sign(this.getRawHash());
        this.r = BigIntegers.asUnsignedByteArray(temp.r);
        this.s = BigIntegers.asUnsignedByteArray(temp.s);
        this.v = temp.v;
    }

    public byte[] getRawHash() {
        rlpParse();
        byte[] plainMsg = this.getEncodedRaw();
        return keccak256(plainMsg);
    }

    public byte[] getHash() {
        if(!isEmpty(hash)) return hash;

        rlpParse();
        byte[] plainMsg = this.getEncoded();
        return keccak256(plainMsg);
    }

    public byte[] getEncodedRaw() {
        rlpParse();
        if(rlpRaw != null) return rlpRaw;

        if(targetted) {
            return tgetEncodedRaw();
        } else {
            return ugetEncodedRaw();
        }

    }

    private byte[] tgetEncodedRaw() {
        byte[] target = RLP.encodeElement(this.target);
        byte[] messageType = RLP.encodeInt(this.messageType);
        byte[] payload = RLP.encodeElement(this.payload);
        byte[] r = RLP.encodeElement(EMPTY_BYTE_ARRAY);
        byte[] s = RLP.encodeElement(EMPTY_BYTE_ARRAY);
        byte[] v = RLP.encodeByte((byte)0x50);

        rlpRaw = RLP.encodeList(target, messageType, payload, r, s, v);

        return rlpRaw;
    }

    private byte[] ugetEncodedRaw() {
        byte[] messageType = RLP.encodeInt(this.messageType);
        byte[] payload = RLP.encodeElement(this.payload);
        byte[] r = RLP.encodeElement(EMPTY_BYTE_ARRAY);
        byte[] s = RLP.encodeElement(EMPTY_BYTE_ARRAY);
        byte[] v = RLP.encodeByte((byte)0x50);

        rlpRaw = RLP.encodeList(messageType, payload, r, s, v);

        return rlpRaw;
    }

    public byte[] getEncoded() {
        if (rlpEncoded != null) return rlpEncoded;

        if (targetted) {
            return tgetEncoded();
        } else {
            return ugetEncoded();
        }
    }

    private byte[] tgetEncoded() {
        byte[] target = RLP.encodeElement(this.target);
        byte[] messageType = RLP.encodeInt(this.messageType);
        byte[] payload = RLP.encodeElement(this.payload);
        byte[] r = RLP.encodeElement(this.r);
        byte[] s = RLP.encodeElement(this.s);
        byte[] v = RLP.encodeByte(this.v);

        this.rlpEncoded = RLP.encodeList(target, messageType, payload, r, s, v);

        this.hash = this.getHash();

        return rlpEncoded;
    }

    private byte[] ugetEncoded() {
        byte[] messageType = RLP.encodeInt(this.messageType);
        byte[] payload = RLP.encodeElement(this.payload);
        byte[] r = RLP.encodeElement(this.r);
        byte[] s = RLP.encodeElement(this.s);
        byte[] v = RLP.encodeByte(this.v);

        this.rlpEncoded = RLP.encodeList(messageType, payload, r, s, v);

        this.hash = this.getHash();

        return rlpEncoded;
    }
    
    
}
