package org.dilithium.math.Edwards;

import org.dilithium.math.Point;

import java.math.BigInteger;

import static org.dilithium.math.ModArithmetic.*;

public class ECurve {
    private final BigInteger p;
    private final BigInteger a;
    private final BigInteger d;
    private final Point G;
    private final BigInteger n;

    public ECurve(BigInteger a, BigInteger d, Point G, BigInteger p, BigInteger n) {
        this.a = a;
        this.d = d;
        this.G = G;
        this.p = p;
        this.n = n;
    }

    public BigInteger getA() {
        return a;
    }

    public BigInteger getD() {
        return d;
    }

    public BigInteger getP() {
        return p;
    }

    public Point getG() {
        return G;
    }

    public boolean isOnCurve(Point p) {
        return (add(multiply(this.a, exponentiate(p.getX(), BigInteger.TWO, this.p), this.p), exponentiate(p.getY(), BigInteger.TWO, this.p), this.p).compareTo(add(BigInteger.ONE, multiply(this.d, multiply(exponentiate(p.getX(), BigInteger.TWO, this.p), exponentiate(p.getY(), BigInteger.TWO, this.p), this.p), this.p), this.p)) == 0);
    }

    public Point pointAdd(Point a, Point b) {
        BigInteger x = divide((add((multiply(a.getX(), b.getY(), p)), (multiply(a.getY(), b.getX(), p)), p)), (add(BigInteger.ONE, (multiply(d, (multiply(a.getX(), (multiply(b.getX(), (multiply(a.getY(), b.getY(), p)), p)), p)), p)), p)), p);
        BigInteger y = divide(subtract(multiply(a.getY(), b.getY(), p), multiply(this.a, multiply(a.getX(), b.getX(), p), p), p), subtract(BigInteger.ONE, multiply(d, multiply(a.getX(), multiply(b.getX(), multiply(a.getY(), b.getY(), p), p), p), p), p), p);

        return new Point(x, y);
    }

    public Point pointDouble(Point a) {
        return pointAdd(a, a);
    }

    public Point pointMultiply(BigInteger kin, Point a) {
        BigInteger k = kin.mod(n);
        int length = k.bitLength();

        if (length != 0) {
            Point r = a;
            for (int i = length - 2; i >= 0; i--) {
                r = this.pointDouble(r);

                if (k.testBit(i)) {
                    r = pointAdd(r, a);
                }
            }

            return r;
        }

        return null;
    }
}
