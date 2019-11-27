package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.shift;

import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatorType;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.arithmetic.ArithmeticOperatorMutator;

import java.util.List;

public class ShiftOperatorReplacementMutator extends TreeMutationOperator
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
        return MutatorType.SHIFT;
    }
}
