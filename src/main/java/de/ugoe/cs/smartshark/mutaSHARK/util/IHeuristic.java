package de.ugoe.cs.smartshark.mutaSHARK.util;

public interface IHeuristic<T>
{
    double getHeuristic(T start, T end);
}
