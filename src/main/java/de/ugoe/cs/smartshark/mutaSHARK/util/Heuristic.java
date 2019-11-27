package de.ugoe.cs.smartshark.mutaSHARK.util;

import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;

import java.util.List;

public class Heuristic extends HeuristicBase
{
    protected Heuristic(TreeNode destinationNode, List<TreeMutationOperator> mutationOperators)
    {
        super(destinationNode, mutationOperators);
    }

    @Override
    protected double getHeuristicInternal(TreeNode fromNode)
    {
        return 0;
    }
}
