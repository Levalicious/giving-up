package graph;

import java.util.Vector;

public class Center {
    public int index;

    public Point point;
    public boolean water;
    public boolean ocean;
    public boolean coast;
    public boolean border;
    public String biome;
    public double elevation;
    public double moisture;

    public Vector<Center> neighbors;
    public Vector<Edge> borders;
    public Vector<Corner> corners;
}
