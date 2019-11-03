package de.ugoe.cs.smartshark.mutaSHARK.util;

import java.util.List;

public class SearchResult
{
    private final List<SearchPath> foundPaths;

    public SearchResult(List<SearchPath> foundPaths)
    {
        this.foundPaths = foundPaths;
    }
}
