package de.ugoe.cs.smartshark.mutaSHARK.util;

public class PathNode<T>
{
    private T source;
    private PathNode<T> prevNode;
    private double total;
    private double pathToNodeConst;
    private double heuristic;

    public PathNode(T s, PathNode<T> previousNode, double pathToNodeCost, double heuristic){
        source = s;
        prevNode = previousNode;
        this.pathToNodeConst = pathToNodeCost;
        this.heuristic = heuristic;
        total = pathToNodeCost + heuristic;
    }

    public double getPathToNodeCost()
    {
        return pathToNodeConst;
    }

    public double getHeuristic()
    {
        return heuristic;
    }

    public double getTotalCost()
    {
        return total;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof PathNode<?>){
            return equals((PathNode<?>) obj);
        }
        return super.equals(obj);
    }

    public boolean equals(PathNode<?> other){
        if(other == null)
            return false;
        return other.source.equals(source);
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
