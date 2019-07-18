package de.ugoe.cs.smartshark.mutaSHARK.util;

import java.util.ArrayList;
import java.util.List;

public class PriorityQueue<T>
{
    private final List<T> list = new ArrayList<>();
    private IComparison<T> comparable;

    public PriorityQueue(IComparison<T> comparable)
    {
        this.comparable = comparable;
    }

    private void fix(int i)
    {
        int child = i + 1;
        T item = list.get(i);
        while (child < list.size())
        {
            if (child + 1 < list.size() && comparable.compare(list.get(child), list.get(child + 1)) > 0)
                child++;
            if (comparable.compare(item, list.get(child)) > 0)
            {
                list.set(i, list.get(child));
                i = child;
            } else
                break;
            child = i + 1;
        }
        list.set(i, item);
    }

    public void remove(int i)
    {
        list.set(i, list.get(list.size() - 1));
        list.remove(list.size() - 1);
        if (i < list.size() - 1)
            fix(i);
    }

    public boolean isEmpty()
    {
        return list.isEmpty();
    }

    public T get(int index)
    {
        return list.get(index);
    }

    public int getCount()
    {
        return list.size();
    }

    public void enqueue(T d)
    {
        int i, j;
        list.add(d);
        i = list.size() - 1;
        j = i >> 1;
        while (i > 0 && comparable.compare(list.get(j), list.get(i)) > 0)
        {
            T tmp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, tmp);
            i = j;
            j >>= 1;
        }
    }

    public T dequeue()
    {
        T ret = list.get(0);
        remove(0);
        return ret;
    }

    public void cut(int newlen)
    {
        while (list.size() > newlen)
        {
            list.remove(newlen);
        }
    }
}
