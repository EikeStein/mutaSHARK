package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.actions.model.Action;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.MutatedNode;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AStarSearch
{
    private final TreeNode fromNode;
    private final TreeNode toNode;

    private PriorityQueue openList = new PriorityQueue();
    private List<SearchNode> closedList = new ArrayList<>();

    public AStarSearch(TreeNode fromNode, TreeNode toNode)
    {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    public SearchResult findPaths(SearchSettings searchSettings)
    {
        List<SearchPath> foundPaths = new ArrayList<>();
        openList.clear();
        closedList.clear();

        SearchNode searchNode = new SearchNode(fromNode, searchSettings.heuristic);
        openList.enqueue(searchNode);

        while (openList.getSize() > 0 && foundPaths.size() < searchSettings.maxFoundPaths)
        {
            SearchNode currentNode = openList.dequeue();
            if (currentNode.getCurrentTreeNode().getTree().isIsomorphicTo(toNode.getTree()))
            {
                foundPaths.add(new SearchPath(currentNode));
            }
            else
            {
                closedList.add(currentNode);
                expandNode(currentNode, searchSettings);
            }
        }
        return new SearchResult(foundPaths, getClostestPaths(searchSettings));
    }

    private List<SearchPath> getClostestPaths(SearchSettings searchSettings)
    {
        ArrayList<SearchPath> result = new ArrayList<>();
        closedList.sort(Comparator.comparingDouble(SearchNode::getHeuristicCost));
        for (int i = 0; i < closedList.size() && i < searchSettings.maxFoundPaths; i++)
        {
            SearchNode searchNode = closedList.get(i);
            result.add(new SearchPath(searchNode));
        }
        return result;
    }

    private void expandNode(SearchNode searchNode, SearchSettings searchSettings)
    {
        TreeNode treeNode = searchNode.getCurrentTreeNode();
        if (searchNode.getTotalHopCount() >= searchSettings.maxHops)
        {
            return;
        }
        for (TreeMutationOperator mutationOperator : searchSettings.mutationOperators)
        {
            List<MutatedNode> possibleMutations = mutationOperator.getPossibleMutations(treeNode, toNode);
            for (MutatedNode newNode : possibleMutations)
            {
                if (closedList.stream().anyMatch(n -> n.getCurrentTreeNode().equals(newNode)))
                {
                    continue;
                }

                SearchNode newSearchNode = new SearchNode(newNode, searchNode, searchSettings.heuristic);
                if (openList.find(newNode) != null)
                {
                    if (newSearchNode.getTotalCost() >= openList.find(newNode).getTotalCost())
                    {
                        continue;
                    }
                    openList.replace(openList.find(newNode), newSearchNode);
                }
                else
                {
                    openList.enqueue(newSearchNode);
                }

            }
        }

    }

}
