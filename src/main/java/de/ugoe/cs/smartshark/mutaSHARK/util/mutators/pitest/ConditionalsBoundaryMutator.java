package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.ActionExecutor;
import de.ugoe.cs.smartshark.mutaSHARK.util.Replace;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeHelper;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class ConditionalsBoundaryMutator extends PitestMutator
{
    @Override
    public List<TreeNode> getPossibleMutations(TreeNode treeNode, TreeNode target, List<Action> actions)
    {
        ArrayList<TreeNode> results = new ArrayList<>();
        for (Action action : actions)
        {
            if (action instanceof Replace)
            {
                Replace replace = (Replace) action;
                if (replace.getOriginalNode().getType().name.equalsIgnoreCase("INFIX_EXPRESSION_OPERATOR"))
                {
                    if (replace.getNewNode().getType().name.equalsIgnoreCase("INFIX_EXPRESSION_OPERATOR"))
                    {
                        String originalLabel = replace.getOriginalNode().getLabel();
                        String newLabel = replace.getNewNode().getLabel();
                        if (!originalLabel.equalsIgnoreCase(newLabel) && supportsLabelTransition(originalLabel, newLabel))
                        {
                            ActionExecutor actionExecutor = new ActionExecutor();
                            ITree newTree = actionExecutor.executeAction(treeNode.getTree(), replace);
                            results.add(new TreeNode(newTree));
                        }
                    }
                }
                System.out.println(replace);
            }
        }
        return results;
    }

    private boolean supportsLabelTransition(String originalLabel, String newLabel)
    {
        switch (originalLabel)
        {
            case "<":
                return newLabel.equalsIgnoreCase("<=");
            case "<=":
                return newLabel.equalsIgnoreCase("<");
            case ">":
                return newLabel.equalsIgnoreCase(">=");
            case ">=":
                return newLabel.equalsIgnoreCase(">");
        }
        return false;
    }
}
