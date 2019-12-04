package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.relational;

import de.ugoe.cs.smartshark.mutaSHARK.util.TreeHelper;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatorType;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.arithmetic.ArithmeticOperatorMutator;

import java.util.List;

public class RelationalOperatorReplamentMutator extends TreeMutationOperator
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
        return MutatorType.RELATIONAL;
    }
}

