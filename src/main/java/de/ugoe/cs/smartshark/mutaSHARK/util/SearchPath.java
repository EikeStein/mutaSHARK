package de.ugoe.cs.smartshark.mutaSHARK.util;

import java.util.ArrayList;
import java.util.List;

public class SearchPath
{
    public final List<SearchEdge> edges = new ArrayList<>();
    public final double totalCost;

    public SearchPath(SearchNode finalNode) throws TooManyActionsException
    {
        SearchNode currentNode = finalNode;
        while (currentNode != null)
        {
            SearchNode previousSearchNode = currentNode.getPreviousSearchNode();
            if (previousSearchNode == null)
            {
                currentNode = null;
                continue;
            }
            edges.add(0, new SearchEdge(previousSearchNode, currentNode.getCostToPrevious(), currentNode));
            currentNode = previousSearchNode;
        }
        totalCost = finalNode.getTotalCost();
    }

    @Override
    public String toString()
    {
        StringBuilder edgeString = new StringBuilder();

        for (SearchEdge edge : edges)
        {
            edgeString.append(edge.getToSearchNode().getCurrentTreeNode().toString());
            edgeString.append(" | ");
        }

        return "SearchPath{" + "edges=" + edgeString + ", totalCost=" + totalCost + '}';
    }
}
