package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.tree.ITree;

public interface IMutationOperator
{
    ITree applyTo(ITree tree);
}
