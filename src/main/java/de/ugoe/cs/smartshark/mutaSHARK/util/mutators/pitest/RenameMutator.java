package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.*;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;

import java.util.ArrayList;
import java.util.List;

public class RenameMutator extends PitestMutator
{
    @Override
    public List<MutatedNode> getPossibleMutations(TreeNode treeNode, TreeNode target)
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
                            actionExecutor.executeAction(replace);
                            ITree newTree = replace.getOriginalNode().getParents().stream().filter(t -> t.getParent() == null).findFirst().get();
                            results.add(new MutatedNode(new TreeNode(newTree), this, 10, "Cheated-rename: " + originalLabel + " -> " + label));
                            treeNode = new TreeNode(newTree.deepCopy());
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

