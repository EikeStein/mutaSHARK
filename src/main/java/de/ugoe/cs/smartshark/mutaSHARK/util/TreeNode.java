package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.tree.ITree;

public class TreeNode
{
    private final ITree tree;
    private final IMutationOperator mutationOperator;

    /**
     * Deep clones a tree.
     *
     * @param tree The tree to clone
     */
    public TreeNode(ITree tree)
    {
        this.tree = tree;
        mutationOperator = null;
    }

    public TreeNode(ITree mutatedTree, IMutationOperator mutationOperator)
    {
        this.tree = mutatedTree;
        this.mutationOperator = mutationOperator;
    }

    public ITree getTree()
    {
        ITree tree = this.tree;
        return this.tree;
    }
}
