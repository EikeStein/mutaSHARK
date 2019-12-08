package de.ugoe.cs.smartshark.mutaSHARK;

import com.github.gumtreediff.gen.Generators;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import de.ugoe.cs.smartshark.mutaSHARK.util.*;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.mujava.arithmetic.ArithmeticOperatorInsertionShortCutMutator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MutaShark
{
    private static SearchResult searchResult;

    // -b|-bug path/to/bug.java -f|-fix path/to/fix.java -m|-mutator full.name.of.mutator1 full.name.of.mutator2 ...
    // path/to/bug.java path/to/fix.java  full.name.of.mutator1 full.name.of.mutator2 ...
    // path/to/bug.java path/to/fix.java (in this case all mutatations in package util.mutators are used)
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException
    {
        searchResult = null;
        StartUpOptions startUpOptions = StartUpOptions.parseArgs(args);
        System.out.println("Test");


        com.github.gumtreediff.client.Run.initGenerators();
        TreeContext treeFrom = Generators.getInstance().getTree(startUpOptions.pathToBug);
        TreeContext treeTo = Generators.getInstance().getTree(startUpOptions.pathToFix);

        TreeNode toNode = new TreeNode(TreeHelper.updateTree(treeTo.getRoot()));
        TreeNode fromNode = new TreeNode(TreeHelper.updateTree(treeFrom.getRoot()));
        AStarSearch aStarSearch = new AStarSearch(fromNode, toNode);

        searchResult = aStarSearch.findPaths(new SearchSettings(5, 5, toNode, Arrays.asList(startUpOptions.mutations)));

        for (SearchPath foundPath : searchResult.foundPaths)
        {
            List<TreeMutationOperator> mutators = foundPath.edges.stream().map(e -> e.getToSearchNode().getMutationOperator()).collect(Collectors.toList());
            System.out.println(mutators);
        }
    }


    public static SearchResult getSearchResult()
    {
        return searchResult;
    }
}
