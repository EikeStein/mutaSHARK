package de.ugoe.cs.smartshark.mutaSHARK.util;

public class SearchEdge
{
    private final SearchNode fromSearchNode;
    private SearchNode toSearchNode;
    private final double cost;

    public SearchEdge(SearchNode fromSearchNode, double cost)
    {
        this.fromSearchNode = fromSearchNode;
        this.cost = cost;
    }

    public void setToSearchNode(SearchNode searchNode)
    {
        if (toSearchNode != null)
            toSearchNode = searchNode;
    }

    public SearchNode getToSearchNode()
    {
        return toSearchNode;
    }

    public double getCost()
    {
        return cost;
    }

    public SearchNode getFromSearchNode()
    {
        return fromSearchNode;
    }
}
