package de.ugoe.cs.smartshark.mutaSHARK.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Pathfinder<T> implements IComparison<PathNode<T>>
{
    private final IAdjacent<T> adjacent;
    private final IDistance<T> distance;
    private final INodeDistance<T> nodeDistance;

    public Pathfinder(IAdjacent<T> adjacent, IDistance<T> distance, INodeDistance<T> nodeDistance)
    {
        this.adjacent = adjacent;
        this.distance = distance;
        this.nodeDistance = nodeDistance;
    }

    @Override
    public int compare(PathNode<T> a, PathNode<T> b)
    {
        return (int) (a.getF() - b.getF());
    }

    public List<T> aStar(T start, T end, int maxNodes, int maxNodeDepth, int minTargetDistance)
    {
        IComparison<PathNode<T>> pwc = this;
        PriorityQueue<PathNode<T>> open = new PriorityQueue<>(pwc);
        HashMap<T, Double> bestF = new HashMap<>();
        List<T> path = null;

        open.enqueue(new PathNode<>(start, null, 0, distance.getDistance(start, end), 0, bestF));
        while (!open.isEmpty())
        {
            PathNode<T> cur = open.dequeue();
            boolean closeenough = false;
            if (minTargetDistance > 0)
                closeenough = distance.getDistance(cur.getSource(), end) <= minTargetDistance;
            if (cur.getSource().equals(end) || closeenough)
            {
                Stack<T> s = new Stack<>();
                path = new ArrayList<>();
                s.push(cur.getSource());
                while (cur.getPrevNode() != null)
                {
                    cur = cur.getPrevNode();
                    s.push(cur.getSource());
                }
                while (s.size() > 0)
                    path.add(s.pop());
                break;
            }
            List<T> list = adjacent.getAdjacent(cur.getSource());
            if (nodeDistance != null && maxNodeDepth != 0)
            {
                if (nodeDistance.getNodeDistance(cur.getSource(), end) + cur.getNt() >= maxNodeDepth)
                    continue;
            } else if (maxNodeDepth != 0)
                if (cur.getNt() >= maxNodeDepth)
                    continue;
            for (T d : list)
            {
                double ng = cur.getG() + distance.getDistance(cur.getSource(), d);
                if (bestF.containsKey(d))
                {
                    if (ng + distance.getDistance(d, end) < bestF.get(d))
                    {
                        for (int i = 0; i < open.getCount(); i++)
                            if (open.get(i).getSource().equals(d))
                            {
                                open.remove(i);
                                break;
                            }
                    } else
                        continue;
                }
                open.enqueue(new PathNode<>(d, cur, ng, distance.getDistance(d, end), cur.getNt() + 1, bestF));
            }
            if (maxNodes != 0 && open.getCount() > maxNodes)
                open.cut(maxNodes);
        }
        return path;
    }

}
