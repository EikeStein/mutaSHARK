package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.assignment;

import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;

import java.util.List;

public abstract class AssignmentOperatorReplacementMutator extends TreeMutationOperator
{
    public abstract List<TreeNode> getPossibleMutations(TreeNode treeNode, TreeNode target);
}
