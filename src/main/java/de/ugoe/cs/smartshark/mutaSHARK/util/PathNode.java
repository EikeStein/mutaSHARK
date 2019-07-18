package de.ugoe.cs.smartshark.mutaSHARK.util;

import java.util.HashMap;

public class PathNode<T>
{
    private T source;
    private PathNode<T> prevNode;
    private double f;
    private double g;
    private double h;
    private int nt;

    public PathNode(T s, PathNode<T> p, double pg, double ph, int pnt, HashMap<T,Double> bestF){
        source = s;
        prevNode = p;
        g = pg;
        h = ph;
        f = g + h;
        nt = pnt;
        bestF.put(s, f);
    }

    public double getG()
    {
        return g;
    }

    public double getH()
    {
        return h;
    }

    public int getNt()
    {
        return nt;
    }

    public double getF()
    {
        return f;
    }

    public T getSource()
    {
        return source;
    }

    public PathNode<T> getPrevNode()
    {
        return prevNode;
    }
}
