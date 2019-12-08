package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.actions.EditScript;
import com.github.gumtreediff.actions.model.Action;
import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;

import java.util.ArrayList;
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

        SearchNode searchNode = new SearchNode(fromNode, null, 0, null, searchSettings.heuristic);
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
        return new SearchResult(foundPaths);
    }

    private void expandNode(SearchNode searchNode, SearchSettings searchSettings)
    {
        TreeNode treeNode = searchNode.getCurrentTreeNode();
        List<Action> actions = new DiffTree(treeNode.getTree(), toNode.getTree()).getActions();
        if (searchNode.getTotalHopCount() >= searchSettings.maxHops)
        {
            return;
        }
        for (TreeMutationOperator mutationOperator : searchSettings.mutationOperators)
        {
            List<TreeNode> possibleMutations = mutationOperator.getPossibleMutations(treeNode, toNode, actions);
            for (TreeNode newNode : possibleMutations)
            {
                if (closedList.stream().anyMatch(n -> n.getCurrentTreeNode().equals(newNode)))
                {
                    continue;
                }

                SearchNode newSearchNode = new SearchNode(newNode, searchNode, mutationOperator.getCost(), mutationOperator, searchSettings.heuristic);
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
