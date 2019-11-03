package de.ugoe.cs.smartshark.mutaSHARK.util;

import java.util.ArrayList;
import java.util.List;

public class Pathfinder<T> implements IValueProvider<PathNode<T>>
{
    private final IAdjacent<T> adjacent;
    private final IHeuristic<T> heuristic;
    private final INodeDistance<T> nodeDistance;

    public Pathfinder(IAdjacent<T> adjacent, IHeuristic<T> heuristic, INodeDistance<T> nodeDistance)
    {
        this.adjacent = adjacent;
        this.heuristic = heuristic;
        this.nodeDistance = nodeDistance;
    }

    @Override
    public double getValue(PathNode<T> node)
    {
        return node.getTotalCost();
    }

    public List<T> aStar(T start, T end, int maxNodes, int maxNodeDepth, int minTargetDistance)
    {
        PriorityQueue<PathNode<T>> open = new PriorityQueue<>(this);
        PriorityQueue<PathNode<T>> closed = new PriorityQueue<>(this);

        if(start.equals(end))
        {
            ArrayList<T> result = new ArrayList<>();
            result.add(start);
            result.add(end);
            return result;
        }
        open.enqueue(new PathNode<>(start,null,0,heuristic.getHeuristic(start,end)));

        
    }

}
