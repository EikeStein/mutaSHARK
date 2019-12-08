package de.ugoe.cs.smartshark.mutaSHARK.util;

import java.util.ArrayList;
import java.util.List;

public class SearchPath
{
    public final List<SearchEdge> edges = new ArrayList<>();
    public final double totalCost;

    public SearchPath(SearchNode finalNode)
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
}
