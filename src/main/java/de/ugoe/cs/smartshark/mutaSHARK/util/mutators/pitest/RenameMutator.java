package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.ActionExecutor;
import de.ugoe.cs.smartshark.mutaSHARK.util.Replace;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class RenameMutator extends PitestMutator
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
                String name = replace.getOriginalNode().getType().name;
                if (name.equalsIgnoreCase("SimpleName") || name.equalsIgnoreCase("QualifiedName"))
                {
                    if (replace.getNewNode().getType().name.equalsIgnoreCase(name))
                    {
                        String originalLabel = replace.getOriginalNode().getLabel();
                        String label = replace.getNewNode().getLabel();
                        if (!originalLabel.equals(label))
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
}
