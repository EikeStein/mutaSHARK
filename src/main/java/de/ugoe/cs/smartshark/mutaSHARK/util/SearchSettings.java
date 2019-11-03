package de.ugoe.cs.smartshark.mutaSHARK.util;

import java.util.ArrayList;
import java.util.List;

public class SearchSettings
{
    public final int maxFoundPaths;
    public final HeuristicBase heuristic;
    public final List<TreeMutationOperator> mutationOperators;

    public SearchSettings(int maxFoundPaths, TreeNode destinationNode, List<TreeMutationOperator> mutationOperators)
    {
        this.mutationOperators = mutationOperators;
        this.maxFoundPaths = maxFoundPaths;
        this.heuristic = new Heuristic(destinationNode, mutationOperators);
    }
}
