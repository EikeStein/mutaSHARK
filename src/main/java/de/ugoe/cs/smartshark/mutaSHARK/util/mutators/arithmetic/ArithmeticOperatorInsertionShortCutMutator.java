package de.ugoe.cs.smartshark.mutaSHARK.util.mutators.arithmetic;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TypeSet;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeBuilder;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeHelper;
import de.ugoe.cs.smartshark.mutaSHARK.util.TreeNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.expressions.*;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatorType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ArithmeticOperatorInsertionShortCutMutator extends ArithmeticOperatorInsertionMutator
{

    @Override
    public List<TreeNode> getPossibleMutations(TreeNode treeNode, TreeNode target)
    {
        List<String> names = TreeHelper.getNames(target.getTree()).stream().distinct().collect(Collectors.toList());
        List<TreeNode> result = new ArrayList<>();
        for (String name : names)
        {
            result.addAll(getPossiblePrefixExpressions(treeNode, target, new ExpressionStatement(new PrefixExpression(new DecrementPrefixExpressionOperatorExpression(), new SimpleNameExpression(name)))));
            result.addAll(getPossiblePrefixExpressions(treeNode, target, new ExpressionStatement(new PrefixExpression(new IncrementPrefixExpressionOperatorExpression(), new SimpleNameExpression(name)))));
            result.addAll(getPossiblePrefixExpressions(treeNode, target, new ExpressionStatement(new PostfixExpression(new SimpleNameExpression(name), new DecrementPostfixExpressionOperatorExpression()))));
            result.addAll(getPossiblePrefixExpressions(treeNode, target, new ExpressionStatement(new PostfixExpression(new SimpleNameExpression(name), new IncrementPostfixExpressionOperatorExpression()))));
        }
        //result.addAll(getPossiblePrefixExpressions(treeNode, target));
        return result;
    }

    private List<TreeNode> getPossiblePrefixExpressions(TreeNode treeNode, TreeNode target, ExpressionStatement expressionStatement)
    {
        List<TreeNode> result = new ArrayList<>();

        ITree tree = treeNode.getTree();
        List<ITree> statements = TreeHelper.findStatements(tree);
        for (int i = 0; i < statements.size() + 1; i++)
        {
            ITree currentStatement = statements.get(Math.min(i, statements.size() - 1));
            ITree current = currentStatement.getParent();

            int depth = TreeHelper.getDepthFrom(tree, current);
            int childPosition = current.getChildPosition(currentStatement);
            for (int statementPos = childPosition; statementPos <= childPosition + 1; statementPos++)
            {
                ITree clonedTree = tree.deepCopy();
                String url = TreeHelper.getUrl(current, depth);
                ITree parent = clonedTree.getChild(url);
                expressionStatement.insertTree(parent, statementPos);
                result.add(new TreeNode(clonedTree));
                String treeString = clonedTree.toTreeString();
                System.out.println(treeString);
            }
        }

        return result;
    }

    @Override
    public String getSourceName()
    {
        return "MuJava";
    }

    @Override
    public MutatorType getMutatorType()
    {
        return MutatorType.ARITHMETIC;
    }
}