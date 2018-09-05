package org.dilithium.math.Weierstrass;

import org.dilithium.math.Point;

import java.math.BigInteger;

import static org.dilithium.math.ModArithmetic.*;

public class WCurve {
    /* Modulus on which the curve operates */
    private final BigInteger p;

    /* a variable from y^2 = x^3 + ax + b curve equation */
    private final BigInteger a;
    
    /* b variable from y^2 = x^3 + ax + b curve equation */
    private final BigInteger b;

    /* Base point/generator */
    private final Point G;
    
    /* Order of the generator point G */
    private final BigInteger n;
    
    /* Cofactor of the curve */
    private final BigInteger h;

    public WCurve(BigInteger p, BigInteger a, BigInteger b, Point G, BigInteger n, BigInteger h) {
        this.p = p;
        this.a = a;
        this.b = b;
        this.G = G;
        this.n = n;
        this.h = h;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getA() {
        return a;
    }

    public BigInteger getB() {
        return b;
    }

    public Point getG() {
        return G;
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getH() {
        return h;
    }

    public boolean isOnCurve(Point point) {
        return (exponentiate(point.getY(), BigInteger.TWO, p).compareTo(add(add(exponentiate(point.getX(), BigInteger.valueOf(3), p), multiply(a, point.getX(), p), p), b, p)) == 0);
    }

    public Point pointAdd(Point a, Point b) {
        BigInteger s;

        if(!a.equals(b)) {
            s = getSlope(a, b);
        } else {
            s = getDoubleSlope(a);
        }

        BigInteger x = subtract(subtract(exponentiate(s, BigInteger.TWO, this.p), a.getX(), this.p), b.getX(), this.p);
        BigInteger y = subtract(multiply(subtract(a.getX(), x, this.p), s, this.p), a.getY(), this.p);

        return new Point(x, y);
    }

    public Point pointMultiply(BigInteger k, Point a) {
        k = k.mod(this.n);
        int length = k.bitLength();

        if (length != 0) {
            Point r = a;
            for (int i = length - 2; i >= 0; i--) {
                r = this.pointAdd(r, r);

                if (k.testBit(i)) {
                    r = pointAdd(r, a);
                }
            }

            return r;
        }

        return null;
    }

    private BigInteger getSlope(Point a, Point b) {
        return divide(subtract(b.getY(), a.getY(), this.p), subtract(b.getX(), a.getX(), this.p), this.p);
    }

    private BigInteger getDoubleSlope(Point a) {
        return divide(add(multiply(BigInteger.valueOf(3), exponentiate(a.getX(), BigInteger.TWO, p), p), this.a, p), multiply(BigInteger.TWO, a.getY(), p), p);
    }

    public Point sign(BigInteger privKey, BigInteger k, BigInteger message) {
        BigInteger r = pointMultiply(k, this.getG()).getX();
        BigInteger s = divide(add(message, multiply(privKey, r, this.p), this.p), k, this.p);

        return new Point(r, s);
    }
}
