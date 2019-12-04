package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;

import java.util.Objects;

public class TreeNode
{
    private final ITree tree;

    public TreeNode(ITree tree)
    {
        this.tree = tree;
    }

    public ITree getClonedTree()
    {
        return tree.deepCopy();
    }

    public ITree getTree()
    {
        return tree;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeNode treeNode = (TreeNode) o;
        return tree.isIsomorphicTo(treeNode.tree);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(tree);
    }
}

