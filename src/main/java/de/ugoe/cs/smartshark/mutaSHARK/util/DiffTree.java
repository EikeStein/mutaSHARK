package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.actions.*;
import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.matchers.Mapping;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.heuristic.LcsMatcher;
import com.github.gumtreediff.tree.FakeTree;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeUtils;
import com.github.gumtreediff.utils.SequenceAlgorithms;

import java.util.*;

public class DiffTree
{
    private List<Action> actions;

    public List<Action> getActions()
    {
        return actions;
    }

    public DiffTree(ITree treeFrom, ITree treeTo, boolean recursive)
    {
        classify(treeFrom, treeTo, recursive);
    }

    private void classify(ITree treeFrom, ITree treeTo, boolean recursive)
    {
        EditScriptGenerator chawatheScriptGenerator = new InsertDeleteChawatheScriptGenerator();
        ITree toClone = treeTo.deepCopy();
        ITree fromClone = treeFrom.deepCopy();
        MappingStore mappings = match(fromClone, toClone, new MappingStore(fromClone, toClone));
        actions = chawatheScriptGenerator.computeActions(mappings).asList();
        if (recursive)
        {
            cleanUp(actions);
        }

    }

    private void cleanUp(List<Action> actions)
    {
        List<Action> additionalActions = new ArrayList<>();
        for (Action action : actions)
        {
            for (ITree descendant : action.getNode().getDescendants())
            {
                if (action instanceof Insert)
                {
                    additionalActions.add(new Insert(descendant, descendant.getParent(), descendant.positionInParent()));
                }
                if (action instanceof Delete)
                {
                    additionalActions.add(new Delete(descendant));
                }
            }
        }
        actions.addAll(additionalActions);
    }

    private MappingStore match(ITree src, ITree dst, MappingStore mappings)
    {
        Implementation impl = new Implementation(src, dst, mappings);
        impl.match();
        return impl.mappings;
    }

    private static class Implementation
    {
        private final ITree src;
        private final ITree dst;
        private final MappingStore mappings;

        public Implementation(ITree src, ITree dst, MappingStore mappings)
        {
            this.src = src;
            this.dst = dst;
            this.mappings = mappings;
        }

        public void match()
        {
            List<ITree> srcSeq = TreeUtils.preOrder(src);
            List<ITree> dstSeq = TreeUtils.preOrder(dst);
            List<int[]> lcs = longestCommonSubsequenceWithTypeAndLabel(srcSeq, dstSeq);
            for (int[] x : lcs)
            {
                ITree t1 = srcSeq.get(x[0]);
                ITree t2 = dstSeq.get(x[1]);
                mappings.addMapping(t1, t2);
            }
        }

        private static List<int[]> longestCommonSubsequenceWithTypeAndLabel(List<ITree> s0, List<ITree> s1)
        {
            short[][] lengths = new short[s0.size() + 1][s1.size() + 1];
            for (int i = 0; i < s0.size(); i++)
            {
                for (int j = 0; j < s1.size(); j++)
                {
                    if (s0.get(i).hasSameTypeAndLabel(s1.get(j)))
                    {
                        lengths[i + 1][j + 1] = (short) (lengths[i][j] + 1);
                    }
                    else
                    {
                        lengths[i + 1][j + 1] = (short) Math.max(lengths[i + 1][j], lengths[i][j + 1]);
                    }
                }
            }

            return extractIndexes(lengths, s0.size(), s1.size());
        }

        private static List<int[]> extractIndexes(short[][] lengths, int length1, int length2)
        {
            List<int[]> indexes = new ArrayList<>();

            for (int x = length1, y = length2; x != 0 && y != 0; )
            {
                if (lengths[x][y] == lengths[x - 1][y])
                {
                    x--;
                }
                else if (lengths[x][y] == lengths[x][y - 1])
                {
                    y--;
                }
                else
                {
                    indexes.add(new int[]{x - 1, y - 1});
                    x--;
                    y--;
                }
            }
            Collections.reverse(indexes);
            return indexes;
        }
    }
}

