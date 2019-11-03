package de.ugoe.cs.smartshark.mutaSHARK.util;

import javax.management.OperationsException;
import java.util.ArrayList;
import java.util.List;

public class PriorityQueue<T>
{
    private final List<T> list = new ArrayList<>();
    private IValueProvider<T> valueProvider;

    public PriorityQueue(IValueProvider<T> valueProvider)
    {
        this.valueProvider = valueProvider;
    }

    public boolean isEmpty()
    {
        return list.isEmpty();
    }

    public int getCount()
    {
        return list.size();
    }

    public void enqueue(T d)
    {
        list.add(d);
    }

    public T dequeue()
    {
        if(list.isEmpty())
            throw new IndexOutOfBoundsException();
        T result = null;
        double lowestValue = Double.MAX_VALUE;

        for (T candidate : list)
        {
            double value = valueProvider.getValue(candidate);
            if (value < lowestValue)
            {
                result = candidate;
                lowestValue = value;
            }
        }
        if (result != null)
            list.remove(result);
        return result;
    }

}
