package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.io.TreeIoUtils;
import com.github.gumtreediff.tree.*;

import java.util.*;

public class TreeNode
{
    private final ITree tree;

    public TreeNode(ITree tree)
    {

        this.tree = tree;
    }

    public ITree getTree()
    {
        return tree;
    }
}

