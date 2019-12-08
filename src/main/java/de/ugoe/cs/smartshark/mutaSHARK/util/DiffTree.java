package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.actions.AllNodesClassifier;
import com.github.gumtreediff.actions.ChawatheScriptGenerator;
import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.actions.InsertDeleteChawatheScriptGenerator;
import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.heuristic.LcsMatcher;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeUtils;
import com.github.gumtreediff.utils.SequenceAlgorithms;

import java.util.*;

public class DiffTree
{
    protected final Set<ITree> srcUpdTrees = new HashSet<>();

    protected final Set<ITree> dstUpdTrees = new HashSet<>();

    protected final Set<ITree> srcMvTrees = new HashSet<>();

    protected final Set<ITree> dstMvTrees = new HashSet<>();

    protected final Set<ITree> srcDelTrees = new HashSet<>();

    protected final Set<ITree> dstAddTrees = new HashSet<>();

    private List<Action> actions;

    public Set<ITree> getSrcUpdTrees()
    {
        return srcUpdTrees;
    }

    public Set<ITree> getDstUpdTrees()
    {
        return dstUpdTrees;
    }

    public Set<ITree> getSrcMvTrees()
    {
        return srcMvTrees;
    }

    public Set<ITree> getDstMvTrees()
    {
        return dstMvTrees;
    }

    public Set<ITree> getSrcDelTrees()
    {
        return srcDelTrees;
    }

    public Set<ITree> getDstAddTrees()
    {
        return dstAddTrees;
    }

    public List<Action> getActions()
    {
        return actions;
    }

    public DiffTree(ITree treeFrom, ITree treeTo)
    {
        classify(treeFrom, treeTo);
    }

    private void classify(ITree treeFrom, ITree treeTo)
    {
        ChawatheScriptGenerator chawatheScriptGenerator = new ChawatheScriptGenerator();
        MappingStore mappings = match(treeFrom, treeTo, new MappingStore(treeFrom, treeTo));
        actions = chawatheScriptGenerator.computeActions(mappings).asList();
        cleanUp(actions);

        for (Action a : actions)
        {
            if (a instanceof Delete)
            {
                srcDelTrees.add(a.getNode());
            }
            else if (a instanceof TreeDelete)
            {
                srcDelTrees.add(a.getNode());
                srcDelTrees.addAll(a.getNode().getDescendants());
            }
            else if (a instanceof Insert)
            {
                dstAddTrees.add(a.getNode());
            }
            else if (a instanceof TreeInsert)
            {
                dstAddTrees.add(a.getNode());
                dstAddTrees.addAll(a.getNode().getDescendants());
            }
            else if (a instanceof Update)
            {
                srcUpdTrees.add(a.getNode());
                dstUpdTrees.add(mappings.getDstForSrc(a.getNode()));
            }
            else if (a instanceof Move)
            {
                srcMvTrees.add(a.getNode());
                srcMvTrees.addAll(a.getNode().getDescendants());
                dstMvTrees.add(mappings.getDstForSrc(a.getNode()));
                dstMvTrees.addAll(mappings.getDstForSrc(a.getNode()).getDescendants());
            }
        }
    }

    private void cleanUp(List<Action> actions)
    {
        for (int i = 0; i < actions.size(); i++)
        {
            Action action = actions.get(i);
            if (action instanceof Insert)
            {
                Insert insert = (Insert) action;
                Optional<Action> deleteAction = actions.stream().filter(a -> a instanceof Delete && TreeHelper.urlEqual(a.getNode(), insert.getNode(), true, 1)).findAny();
                if (deleteAction.isPresent())
                {
                    actions.remove(insert);
                    actions.remove(deleteAction.get());
                    actions.add(i, new Replace(deleteAction.get().getNode(), insert.getNode()));
                    i = 0;
                }
            }
        }
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

