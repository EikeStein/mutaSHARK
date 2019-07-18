package de.ugoe.cs.smartshark.mutaSHARK.util;

import java.util.List;

public class MutationPathFinder implements IAdjacent<TreeNode>, IDistance<TreeNode>, INodeDistance<TreeNode>
{
    private final Pathfinder<TreeNode> pathfinder;

    public MutationPathFinder()
    {
        pathfinder = new Pathfinder<>(this, this, this);
    }

    @Override
    public List<TreeNode> getAdjacent(TreeNode source)
    {
        return null;
    }

    @Override
    public double getDistance(TreeNode start, TreeNode end)
    {
        return 0;
    }

    @Override
    public int getNodeDistance(TreeNode source, TreeNode end)
    {
        return 0;
    }
}
