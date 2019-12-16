package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.pitest;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.ActionExecutor;
import de.ugoe.cs.smartshark.mutaSHARK.util.DiffTree;
import de.ugoe.cs.smartshark.mutaSHARK.util.Replace;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InvertNegativesMutator extends PitestMutator
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
            if (action instanceof Insert)
            {
                Insert insert = (Insert) action;
                ITree insertNode = insert.getNode();
                ITree parent = insert.getParent();
                if (parent.getType().name.equals("Assignment") && insertNode.getType().name.equals("PrefixExpression"))
                {
                    List<ITree> insertNodeChildren = insertNode.getChildren();
                    if (insertNodeChildren.size() == 2)
                    {
                        ITree prefixOperator = insertNodeChildren.get(0);
                        ITree targetMovedNode = insertNodeChildren.get(1);
                        if (prefixOperator.getType().name.equals("PREFIX_EXPRESSION_OPERATOR") && prefixOperator.getLabel().equals("-"))
                        {
                            List<Move> moves = actions.stream().filter(a ->
                                                                       {
                                                                           if (!(a instanceof Move))
                                                                           {
                                                                               return false;
                                                                           }
                                                                           Move move = (Move) a;
                                                                           boolean equalToTarget = move.getNode().isIsomorphicTo(targetMovedNode);
                                                                           boolean equalToParent = move.getNode().getParent() == parent;
                                                                           return equalToParent && equalToTarget;
                                                                       }).map(a -> (Move) a).collect(Collectors.toList());
                            if (moves.size() == 1)
                            {
                                Move move = moves.get(0);
                                Delete delete = new Delete(move.getNode());
                                ActionExecutor actionExecutor = new ActionExecutor();
                                ITree clonedTree = treeNode.getTree().deepCopy();
                                Insert localInsert = actionExecutor.reassignTree(insert, clonedTree);
                                delete = actionExecutor.reassignTree(delete, clonedTree);
                                actionExecutor.executeAction(localInsert);
                                actionExecutor.executeAction(delete);
                                results.add(new MutatedNode(new TreeNode(clonedTree), this, 1, "Invert-Negative-inserted: " + localInsert));
                                treeNode = new TreeNode(treeNode.getTree().deepCopy());
                                target = new TreeNode(target.getTree().deepCopy());
                                actions = new DiffTree(treeNode.getTree(), target.getTree()).getActions();
                            }
                        }
                    }
                }
            }
            if (action instanceof Move)
            {
                Move move = (Move) action;
                ITree parent = move.getParent();
                if (parent.getType().name.equals("Assignment"))
                {
                    List<Delete> prefixExpressions = actions.stream().filter(a ->
                                                                             {
                                                                                 if (!(a instanceof Delete))
                                                                                 {
                                                                                     return false;
                                                                                 }
                                                                                 if (!a.getNode().getType().name.equals("PrefixExpression"))
                                                                                 {
                                                                                     return false;
                                                                                 }
                                                                                 ITree deleteParent = a.getNode().getParent();
                                                                                 return deleteParent == parent;
                                                                             }).map(a -> (Delete) a).collect(Collectors.toList());
                    if (prefixExpressions.size() == 1)
                    {
                        Delete prefixExpression = prefixExpressions.get(0);
                        ITree prefixExpressionNode = prefixExpression.getNode();
                        List<Delete> prefixExpressionOperators = actions.stream().filter(a ->
                                                                                         {
                                                                                             if (!(a instanceof Delete))
                                                                                             {
                                                                                                 return false;
                                                                                             }
                                                                                             if (!a.getNode().getType().name.equals("PREFIX_EXPRESSION_OPERATOR"))
                                                                                             {
                                                                                                 return false;
                                                                                             }
                                                                                             if (!a.getNode().getLabel().equals("-"))
                                                                                             {
                                                                                                 return false;
                                                                                             }
                                                                                             ITree deleteParent = a.getNode().getParent();
                                                                                             return deleteParent == prefixExpressionNode;
                                                                                         }).map(a -> (Delete) a).collect(Collectors.toList());
                        if (prefixExpressionOperators.size() == 1)
                        {
                            Delete prefixExpressionOperator = prefixExpressionOperators.get(0);
                            ActionExecutor actionExecutor = new ActionExecutor();
                            actionExecutor.executeAction(move);
                            actionExecutor.executeAction(prefixExpression);
                            actionExecutor.executeAction(prefixExpressionOperator);
                            results.add(new MutatedNode(treeNode, this, 1, "Invert-Negative-deleted: " + move + " -> " + prefixExpression + " -> " + prefixExpressionOperator));
                            treeNode = new TreeNode(treeNode.getTree().deepCopy());
                            target = new TreeNode(target.getTree().deepCopy());
                            actions = new DiffTree(treeNode.getTree(), target.getTree()).getActions();
                        }
                    }
                }
            }
        }
        return results;
    }
}
