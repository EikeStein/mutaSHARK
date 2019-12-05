package de.ugoe.cs.smartshark.mutaSHARK;

import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.actions.SimplifiedChawatheScriptGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.gen.Generators;
import com.github.gumtreediff.matchers.Mapping;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils;
import de.ugoe.cs.smartshark.mutaSHARK.util.*;
import de.ugoe.cs.smartshark.mutaSHARK.util.defects4j.Defects4JLoader;
import de.ugoe.cs.smartshark.mutaSHARK.util.defects4j.Defects4JProjectName;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.arithmetic.ArithmeticOperatorInsertionShortCutMutator;

import java.util.ArrayList;
import java.util.List;

public class MutaShark
{
    public static void main(String[] args) throws Exception
    {
        new Defects4JLoader().LoadAll();

        String classAFrom = "C:\\Users\\Eike\\OneDrive\\Documents\\Ausbildung\\Uni\\1919\\Masterarbeit\\Projekt\\Code\\mutaSHARK\\src\\main\\java\\de\\ugoe\\cs\\smartshark\\mutaSHARK\\testclasses\\from\\ClassA.java";
        String classATo = "C:\\Users\\Eike\\OneDrive\\Documents\\Ausbildung\\Uni\\1919\\Masterarbeit\\Projekt\\Code\\mutaSHARK\\src\\main\\java\\de\\ugoe\\cs\\smartshark\\mutaSHARK\\testclasses\\to\\ClassA.java";
        com.github.gumtreediff.client.Run.initGenerators();
        TreeContext treeFrom = Generators.getInstance().getTree(classAFrom);
        TreeContext treeTo = Generators.getInstance().getTree(classATo);

        ITree fromRoot = TreeHelper.updateTree(TreeHelper.findMethods(treeFrom.getRoot()).get(0));
        ITree toRoot = TreeHelper.updateTree(TreeHelper.findMethods(treeTo.getRoot()).get(0));
        TreeHelper.updateTree(toRoot);

        ArithmeticOperatorInsertionShortCutMutator mutator = new ArithmeticOperatorInsertionShortCutMutator();

        TreeNode toNode = new TreeNode(toRoot);
        TreeNode fromNode = new TreeNode(fromRoot);
        AStarSearch aStarSearch = new AStarSearch(fromNode, toNode);
        ArrayList<TreeMutationOperator> mutationOperators = new ArrayList<>();
        mutationOperators.add(new ArithmeticOperatorInsertionShortCutMutator());
        SearchResult paths = aStarSearch.findPaths(new SearchSettings(1, 3, toNode, mutationOperators));

        List<TreeNode> possibleMutations = mutator.getPossibleMutations(fromNode, toNode);
        for (TreeNode possibleMutation : possibleMutations)
        {
            if (possibleMutation.getTree().isIsomorphicTo(toRoot))
            {
                String treeString = possibleMutation.getTree().toTreeString();
                System.out.println(treeString);
            }
        }
    }
}
