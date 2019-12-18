package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.mujava.conditional;

import com.github.gumtreediff.actions.model.Action;
import de.ugoe.cs.smartshark.mutaSHARK.util.SearchNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatorType;

import java.util.List;

public class ConditionalOperatorInsertionMutator extends ConditionalOperatorMutator
{


    @Override
    public double getExpectedCost()
    {
        return 1;
    }

    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        return null;
    }

}
