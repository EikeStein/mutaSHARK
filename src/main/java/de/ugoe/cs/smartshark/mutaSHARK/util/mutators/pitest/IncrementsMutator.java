package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.*;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;

import java.util.ArrayList;
import java.util.List;

public class IncrementsMutator extends PitestMutator
{
    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target)
    {
        ArrayList<MutatedNode> results = new ArrayList<>();
        results.addAll(getPossibleMutations(treeNode,target, "PREFIX_EXPRESSION_OPERATOR"));
        results.addAll(getPossibleMutations(treeNode,target, "POSTFIX_EXPRESSION_OPERATOR"));
        results.addAll(getPossibleMutations(treeNode,target, "INFIX_EXPRESSION_OPERATOR"));
        return results;
    }

    private boolean supportsLabelTransition(String originalLabel, String newLabel)
    {
        switch (originalLabel)
        {
            case "--":
                return newLabel.equalsIgnoreCase("++");
            case "++":
                return newLabel.equalsIgnoreCase("--");
            case "-":
                return newLabel.equalsIgnoreCase("+");
            case "+":
                return newLabel.equalsIgnoreCase("-");
        }
        return false;
    }

    private List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target, String operatorName)
    {
        treeNode = new TreeNode(treeNode.getTree().deepCopy());
        target = new TreeNode(target.getTree().deepCopy());
        List<Action> actions = new DiffTree(treeNode.getTree(), target.getTree()).getActions();
        ArrayList<MutatedNode> results = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
        {
            Action action = actions.get(i);
            if (action instanceof Replace)
            {
                Replace replace = (Replace) action;
                if (replace.getOriginalNode().getType().name.equalsIgnoreCase(operatorName))
                {
                    if (replace.getNewNode().getType().name.equalsIgnoreCase(operatorName))
                    {
                        String originalLabel = replace.getOriginalNode().getLabel();
                        String newLabel = replace.getNewNode().getLabel();
                        if (!originalLabel.equalsIgnoreCase(newLabel) && supportsLabelTransition(originalLabel, newLabel))
                        {
                            ActionExecutor actionExecutor = new ActionExecutor();
                            actionExecutor.executeAction(replace);
                            results.add(new MutatedNode(treeNode, this, 1, operatorName + ": " + originalLabel + " -> " + newLabel));
                            treeNode = new TreeNode(treeNode.getTree().deepCopy());
                            target = new TreeNode(target.getTree().deepCopy());
                            actions = new DiffTree(treeNode.getTree(), target.getTree()).getActions();
                        }
                    }
                }
                System.out.println(replace);
            }
        }
        return results;
    }
}
