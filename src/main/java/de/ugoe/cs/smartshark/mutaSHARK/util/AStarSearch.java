package de.ugoe.cs.smartshark.mutaSHARK.util;

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

        SearchNode searchNode = new SearchNode(fromNode, new SearchEdge(null, 0), searchSettings.heuristic);
        openList.enqueue(searchNode);

        while (openList.getSize() > 0 && foundPaths.size() < searchSettings.maxFoundPaths)
        {
            SearchNode currentNode = openList.dequeue();
            if (currentNode.getCurrentTreeNode().equals(toNode))
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
        for (TreeMutationOperator mutationOperator :
                searchSettings.mutationOperators)
        {
            List<TreeNode> possibleMutations = mutationOperator.getPossibleMutations(treeNode);
            for (TreeNode newNode :
                    possibleMutations)
            {
                if (closedList.stream().anyMatch(n -> n.getCurrentTreeNode().equals(newNode)))
                    continue;

                SearchEdge searchEdge = new SearchEdge(searchNode, 1);
                SearchNode successor = new SearchNode(newNode, searchEdge, searchSettings.heuristic);
                double tentativeTotalCost = searchNode.getTotalCost() + searchEdge.getCost();
                if (openList.find(newNode) != null)
                {
                    if (tentativeTotalCost >= openList.find(newNode).getTotalCost())
                        continue;
                    openList.replace(openList.find(newNode), successor);
                }
                else
                {
                    openList.enqueue(successor);
                }

            }
        }

    }

}
