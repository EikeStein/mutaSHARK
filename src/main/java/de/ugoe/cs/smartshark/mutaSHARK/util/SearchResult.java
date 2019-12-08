package de.ugoe.cs.smartshark.mutaSHARK.util;

import java.util.List;

public class SearchResult
{
    public final List<SearchPath> foundPaths;

    public SearchResult(List<SearchPath> foundPaths)
    {
        this.foundPaths = foundPaths;
    }
}
