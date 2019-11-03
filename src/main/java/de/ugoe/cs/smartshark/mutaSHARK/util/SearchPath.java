package de.ugoe.cs.smartshark.mutaSHARK.util;

import java.util.ArrayList;
import java.util.List;

public class SearchPath
{
    public final List<SearchEdge> edges = new ArrayList<>();

    public SearchPath(SearchNode finalNode)
    {
        SearchEdge edge = finalNode.getEdgeToPreviousNode();
        while (edge != null)
        {
            edges.add(0, edge);
            edge = edge.getFromSearchNode().getEdgeToPreviousNode();
        }
    }

    public double getTotalCost()
    {
        return edges.stream().mapToDouble(SearchEdge::getCost).sum();
    }
}
