package de.ugoe.cs.smartshark.mutaSHARK.util;

import java.util.List;

public interface IAdjacent<T>
{
    List<T> getAdjacent(T source);
}

