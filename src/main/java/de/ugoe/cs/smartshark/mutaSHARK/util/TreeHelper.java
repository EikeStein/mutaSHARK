package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TreeHelper
{
    private static final String methodTreeTypeName = "MethodDeclaration";
    private static final String variableAssignmentTreeTypeName = "Assignment";
    private static final String variableDeclarationTreeTypeName = "VariableDeclarationFragment";
    private static final String methodInvocationTreeTypeName = "MethodInvocation";
    private static final String blockTreeTypeName = "Block";
    private static final String statementSuffixTypeName = "Statement";
    private static final String simpleNameTypeName = "SimpleName";

    public static List<ITree> findMethods(ITree tree)
    {
        return tree.getDescendants().stream().filter(t -> t.getType().name.equalsIgnoreCase(methodTreeTypeName)).collect(Collectors.toList());
    }

    public static List<ITree> findStatements(ITree tree)
    {
        return tree.getDescendants().stream().filter(t -> t.getType().name.toLowerCase().endsWith(statementSuffixTypeName.toLowerCase())).collect(Collectors.toList());
    }

    public static List<ITree> findBlocks(ITree tree)
    {
        return tree.getDescendants().stream().filter(t -> t.getType().name.equalsIgnoreCase(blockTreeTypeName)).collect(Collectors.toList());
    }

    public static ITree updateTree(ITree tree)
    {
        int length = tree.getLabel().length();
        tree.setLength(length);
        int pos = tree.getPos() + tree.getLength();
        for (ITree child : tree.getChildren())
        {
            child.setPos(pos);
            updateTree(child);
            int childLength = child.getLength();
            pos += childLength;
            tree.setLength(tree.getLength() + childLength);
        }
        return tree;
    }

    public static List<ITree> findMethodInvocations(ITree tree)
    {
        return tree.getDescendants().stream().filter(t -> t.getType().name.equalsIgnoreCase(methodInvocationTreeTypeName)).collect(Collectors.toList());
    }

    public static List<ITree> findVariableAssignments(ITree tree, boolean includeDeclarations)
    {
        List<ITree> result = tree.getDescendants().stream().filter(t -> t.getType().name.equalsIgnoreCase(variableAssignmentTreeTypeName)).collect(Collectors.toList());
        if (includeDeclarations)
            result.addAll(tree.getDescendants().stream().filter(t -> t.getType().name.equalsIgnoreCase(variableDeclarationTreeTypeName)).collect(Collectors.toList()));
        return result;
    }

    public static List<String> getNames(ITree tree)
    {
        return tree.getDescendants().stream().filter(t -> t.getType().name.equalsIgnoreCase(simpleNameTypeName)).map(ITree::getLabel).collect(Collectors.toList());
    }

    public static ITree findTreeIsomophic(ITree tree, ITree elementToFind)
    {
        List<ITree> findings = tree.getDescendants().stream().filter(t -> t.isIsomorphicTo(elementToFind)).collect(Collectors.toList());
        if (findings.size() > 1)
            throw new ArrayIndexOutOfBoundsException("Too many fitting tree nodes");
        for (ITree finding : findings)
            return finding;
        return null;
    }

    public static int getDepthFrom(ITree level1, ITree level2)
    {
        int level = 0;
        while (level2 != level1)
        {
            level++;
            level2 = level2.getParent();
        }
        return level;
    }

    public static String getUrl(ITree tree, int maxLevels)
    {
        String url = "";
        int level = 0;
        while (tree.getParent() != null && level < maxLevels)
        {
            int childPosition = tree.getParent().getChildPosition(tree);
            tree = tree.getParent();
            url = childPosition + "." + url;
            level++;
        }
        url = url.substring(0, url.length() - 1);
        return url;
    }
}
