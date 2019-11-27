package de.ugoe.cs.smartshark.mutaSHARK.util.mutators;

import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;

import java.util.List;

public abstract class TreeMutationOperator
{
    public abstract List<TreeNode> getPossibleMutations(TreeNode treeNode, TreeNode target);

    public abstract String getSourceName();

    public abstract MutatorType getMutatorType();
}

