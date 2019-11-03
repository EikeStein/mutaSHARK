package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;

import java.util.ArrayList;
import java.util.List;

public class MutationPathFinder implements IAdjacent<TreeNode>, IHeuristic<TreeNode>, INodeDistance<TreeNode>
{
    private final Pathfinder<TreeNode> pathfinder;
    private final IMutationFactory mutationFactory;

    public MutationPathFinder(IMutationFactory mutationFactory)
    {
        this.mutationFactory = mutationFactory;
        pathfinder = new Pathfinder<>(this, this, this);
    }

    @Override
    public List<TreeNode> getAdjacent(TreeNode source)
    {
        ITree tree = source.getTree();
        List<IMutationOperator> mutationOperators = mutationFactory.getAllAvailableMutationOperators(tree);

        List<TreeNode> adjacentTreeNodes = new ArrayList<>();

        for (IMutationOperator mutationOperator : mutationOperators)
        {
            ITree mutatedTree = mutationOperator.applyTo(tree.deepCopy());
            TreeNode adjacentTreeNode = new TreeNode(mutatedTree, mutationOperator);
            adjacentTreeNodes.add(adjacentTreeNode);
        }
        return adjacentTreeNodes;
    }

    @Override
    public double getHeuristic(TreeNode start, TreeNode end)
    {
        ITree startTree = start.getTree();
        ITree endTree = end.getTree();

        Matcher matcher = Matchers.getInstance().getMatcher(startTree, endTree);
        matcher.match();
        ActionGenerator actionGenerator = new ActionGenerator(startTree, endTree, matcher.getMappings());
        actionGenerator.generate();
        List<Action> actions = actionGenerator.getActions();

        // Maybe heuristic?

        return actions.size();

    }

    @Override
    public int getNodeDistance(TreeNode source, TreeNode end)
    {
        return 0;
    }
}
