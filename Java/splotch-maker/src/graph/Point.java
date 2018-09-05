package graph;

public class Point {
    public final double length;
    public final double x;
    public final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        this.length = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
}
