package de.ugoe.cs.smartshark.mutaSHARK.util;

import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;

import java.util.Objects;

public class SearchNode
{
    private final TreeNode currentTreeNode;
    private final SearchNode previousSearchNode;
    private final double costToPrevious;
    private final TreeMutationOperator mutationOperator;
    private final HeuristicBase heuristic;

    private Double heuristicCost;

    public SearchNode(TreeNode currentTreeNode, SearchNode previousSearchNode, double costToPrevious, TreeMutationOperator mutationOperator, HeuristicBase heuristic)
    {
        this.currentTreeNode = currentTreeNode;
        this.previousSearchNode = previousSearchNode;
        this.costToPrevious = costToPrevious;
        this.mutationOperator = mutationOperator;
        this.heuristic = heuristic;
    }


    public double getCostToPrevious()
    {
        return costToPrevious;
    }

    public SearchNode getPreviousSearchNode()
    {
        return previousSearchNode;
    }

    public double getTotalCost()
    {
        return getPreviousCost() + getHeuristicCost();
    }

    public TreeNode getCurrentTreeNode()
    {
        return currentTreeNode;
    }

    private double getPreviousCost()
    {
        double cost = costToPrevious;
        if (previousSearchNode != null)
            cost += previousSearchNode.getPreviousCost();
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
            SearchNode previousTreeNode = current.getPreviousSearchNode();
            if (previousTreeNode != null)
                current = previousTreeNode.getPreviousSearchNode();
            else
                current = null;
        }
        return count;
    }

    public TreeMutationOperator getMutationOperator()
    {
        return mutationOperator;
    }
}
