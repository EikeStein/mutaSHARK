package de.ugoe.cs.smartshark.mutaSHARK.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PriorityQueue
{
    private List<SearchNode> queue = new ArrayList<>();

    public void enqueue(SearchNode searchNode)
    {
        int index = 0;
        while (index < queue.size() && queue.get(index).getTotalCost() <= searchNode.getTotalCost())
        {
            index++;
        }
        queue.add(index, searchNode);
    }

    public SearchNode dequeue()
    {
        return queue.remove(0);
    }

    public SearchNode dequeueLast()
    {
        return queue.remove(queue.size() - 1);
    }

    public int getSize()
    {
        return queue.size();
    }

    public void clear()
    {
        queue.clear();
    }

    public Stream<SearchNode> stream()
    {
        return queue.stream();
    }

    public SearchNode find(TreeNode treeNode)
    {
        for (SearchNode searchNode : queue)
        {
            if (searchNode.getCurrentTreeNode().equals(treeNode))
            {
                return searchNode;
            }
        }
        return null;
    }

    public void replace(SearchNode originalNode, SearchNode newNode)
    {
        queue.remove(originalNode);
        enqueue(newNode);
    }
}

