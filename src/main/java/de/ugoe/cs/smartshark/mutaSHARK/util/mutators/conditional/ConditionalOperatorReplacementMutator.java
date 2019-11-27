package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.conditional;

import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatorType;

import java.util.List;

public class ConditionalOperatorReplacementMutator extends ConditionalOperatorMutator
{

    @Override
    public List<TreeNode> getPossibleMutations(TreeNode treeNode, TreeNode target)
    {
        return null;
    }
    @Override
    public String getSourceName()
    {
        return "MuJava";
    }

    @Override
    public MutatorType getMutatorType()
    {
        return MutatorType.CONDITIONAL;
    }
}
