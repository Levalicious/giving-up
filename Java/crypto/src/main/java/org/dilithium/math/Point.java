package org.dilithium.math;

import java.math.BigInteger;

public class Point {
    private BigInteger x;
    private BigInteger y;

    public Point(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }

    public String toString(boolean compressed) {
        String xString = x.toString(16);
        String yString = y.toString(16);

        while (xString.length() < 64) {
            xString = "0" + xString;
        }

        while (yString.length() < 64) {
            yString = "0" + yString;
        }

        if (compressed) {
            if (y.getLowestSetBit() != 0) {
                return "02" + xString;
            } else {
                return "03" + xString;
            }
        } else {
            return "04" + xString + yString;
        }
    }

    public String toString() {
        return toString(false);
    }
}
