package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.tree.ITree;

import java.util.List;

public interface IMutationFactory
{
    List<IMutationOperator> getAllAvailableMutationOperators(ITree tree);
}
