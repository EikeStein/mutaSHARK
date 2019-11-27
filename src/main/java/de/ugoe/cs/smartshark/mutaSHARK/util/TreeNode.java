package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.tree.Tree;

import java.util.Objects;

public class TreeNode
{
    private final Tree tree;

    public TreeNode(Tree tree)
    {
        this.tree = tree;
    }

    public Tree getClonedTree()
    {
        return tree.deepCopy();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeNode treeNode = (TreeNode) o;
        return Objects.equals(tree, treeNode.tree);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(tree);
    }
}

