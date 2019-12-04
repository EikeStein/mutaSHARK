package de.ugoe.cs.smartshark.mutaSHARK.util;

import java.util.Objects;

public class SearchNode
{
    private final TreeNode currentTreeNode;
    private final SearchEdge edgeToPreviousNode;
    private final HeuristicBase heuristic;

    private Double heuristicCost;

    public SearchNode(TreeNode currentTreeNode, SearchEdge edgeToPreviousNode, HeuristicBase heuristic)
    {
        this.currentTreeNode = currentTreeNode;
        this.edgeToPreviousNode = edgeToPreviousNode;
        this.heuristic = heuristic;
        edgeToPreviousNode.setToSearchNode(this);
    }

    public double getTotalCost()
    {
        return getPreviousCost() + getHeuristicCost();
    }

    public SearchEdge getEdgeToPreviousNode()
    {
        return edgeToPreviousNode;
    }

    public TreeNode getCurrentTreeNode()
    {
        return currentTreeNode;
    }

    private double getPreviousCost()
    {
        double cost = edgeToPreviousNode.getCost();
        SearchNode fromSearchNode = edgeToPreviousNode.getFromSearchNode();
        if (fromSearchNode != null)
            cost += fromSearchNode.getPreviousCost();
        return cost;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchNode that = (SearchNode) o;
        return Objects.equals(currentTreeNode, that.currentTreeNode);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(currentTreeNode);
    }

    private double getHeuristicCost()
    {
        if (heuristicCost == null)
            heuristicCost = heuristic.getHeuristic(currentTreeNode);
        return heuristicCost;
    }

    public int getTotalHopCount()
    {
        int count = 0;
        SearchNode current = this;
        while (current != null)
        {
            count++;
            SearchEdge edgeToPreviousNode = current.getEdgeToPreviousNode();
            if (edgeToPreviousNode != null)
                current = edgeToPreviousNode.getFromSearchNode();
            else
                current = null;
        }
        return count;
    }
}
