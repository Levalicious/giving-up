package graph;

import java.util.Vector;

public class Corner {
    public int index;

    public Point point;
    public boolean ocean;
    public boolean water;
    public boolean coast;
    public boolean border;
    public double elevation;
    public double moisture;

    public Vector<Center> touches;
    public Vector<Edge> protrudes;
    public Vector<Corner> adjacent;

    public int river;
    public Corner downslope;
    public Corner watershed;
    public int wateshed_size;
}
