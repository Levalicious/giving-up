package util.wallet;

public class Signature {
    private byte[] r;
    private byte[] s;

    public Signature(byte[] r, byte[] s) {
        this.r = r;
        this.s = s;
    }

    public void setR(byte[] in) {
        r = in;
    }

    public void setS(byte[] in) {
        s = in;
    }

    public byte[] getR() {
        return r;
    }

    public byte[] getS() {
        return s;
    }
}
