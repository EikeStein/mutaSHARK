package de.ugoe.cs.smartshark.mutaSHARK;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.client.diff.ui.web.views.DiffView;
import com.github.gumtreediff.gen.Generators;
import com.github.gumtreediff.io.TreeIoUtils;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;
import de.ugoe.cs.smartshark.mutaSHARK.testclasses.to.ClassA;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class MutaShark
{
    public static void main(String[] args) throws Exception
    {
        String classAFrom = "C:\\Users\\Eike\\OneDrive\\Documents\\Ausbildung\\Uni\\1919\\Masterarbeit\\Projekt\\mutaSHARK\\src\\main\\java\\de\\ugoe\\cs\\smartshark\\mutaSHARK\\testclasses\\from\\ClassA.java";
        String classATo = "C:\\Users\\Eike\\OneDrive\\Documents\\Ausbildung\\Uni\\1919\\Masterarbeit\\Projekt\\mutaSHARK\\src\\main\\java\\de\\ugoe\\cs\\smartshark\\mutaSHARK\\testclasses\\to\\ClassA.java";
        com.github.gumtreediff.client.Run.initGenerators();
        TreeContext treeFrom = Generators.getInstance().getTree(classAFrom);
        TreeContext treeTo = Generators.getInstance().getTree(classATo);

        ITree fromRoot = treeFrom.getRoot();
        ITree toRoot = treeTo.getRoot();

        Matcher matcher = Matchers.getInstance().getMatcher(fromRoot, toRoot);
        matcher.match();
        MappingStore mappings = matcher.getMappings();

        ActionGenerator actionGenerator = new ActionGenerator(fromRoot, toRoot, mappings);
        List<Action> actions1 = actionGenerator.getActions();
        List<Action> actions2 = actionGenerator.generate();

        TreeIoUtils.TreeSerializer toXml = TreeIoUtils.toXml(treeFrom);
        String output = toXml.toString();

        for (ITree tree : fromRoot.getDescendants())
        {
            int depth = tree.getDepth();
            String typeLabel = treeFrom.getTypeLabel(tree.getType());

        }
    }
}
