package de.ugoe.cs.smartshark.mutaSHARK.util.defects4j;

import de.ugoe.cs.smartshark.mutaSHARK.MutaShark;
import de.ugoe.cs.smartshark.mutaSHARK.util.SearchPath;
import de.ugoe.cs.smartshark.mutaSHARK.util.SearchResult;
import de.ugoe.cs.smartshark.mutaSHARK.util.TooManyActionsException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Defects4JRunner
{
    final private static List<String> results = new ArrayList<>();
    public static int total = 0;
    public static int skipped = 0;
    public static int fixed = 0;

    public static void main(String[] args) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, InterruptedException
    {
        List<Defects4JBugFix> defects4JBugFixes = new Defects4JLoader(Defects4JRunner::handleBugFix).LoadAll();
    }

    private static void handleBugFix(Defects4JBugFix bugFix) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException
    {
        total++;
        if (total < 0)
        {
            return;
        }
        try
        {
            MutaShark.main(new String[]{bugFix.buggyClassFile, bugFix.fixedClassFile, "-m", "Rename", "Invert", "-p", "1", "-d", "105"});
            final SearchResult searchResult = MutaShark.getSearchResult();
            addResultString(bugFix, searchResult);
            if (searchResult.foundPaths.size() > 0)
            {
                fixed++;
            }
        }
        catch (TooManyActionsException e)
        {
            skipped++;
        }
        System.out.println("Fixed: " + fixed + "/" + total + " skipped: " + skipped + " results: " + results.size());
    }

    private static void addResultString(Defects4JBugFix bugFix, SearchResult searchResult)
    {
        results.add(String.join("°", new String[]{bugFix.name, bugFix.buggyClassFile, bugFix.fixedClassFile, searchResult.foundPaths.size() + "", searchResult.closestPaths.size() + ""}));
        for (SearchPath foundPath : searchResult.foundPaths)
        {
            if (foundPath.edges.size() == 0)
            {
                continue;
            }
            final ArrayList<String> paras = new ArrayList<>();
            paras.add("f");
            paras.add(foundPath.totalCost + "");
            paras.add(foundPath.edges.size() + "");
            paras.add(foundPath.totalActionCount + "");
            paras.add(foundPath.remainingActionCount + "");
            paras.addAll(foundPath.edges.stream().map(e -> e.getToSearchNode().getCurrentTreeNode().toString()).collect(Collectors.toList()));
            String result = String.join("°", paras);
            results.add(result);
        }
        for (SearchPath foundPath : searchResult.closestPaths)
        {
            if (foundPath.edges.size() == 0)
            {
                continue;
            }
            final ArrayList<String> paras = new ArrayList<>();
            paras.add("c");
            paras.add(foundPath.totalCost + "");
            paras.add(foundPath.edges.size() + "");
            paras.addAll(foundPath.edges.stream().map(e -> e.getToSearchNode().getCurrentTreeNode().toString()).collect(Collectors.toList()));
            String result = String.join("°", paras);
            results.add(result);
        }
    }
}
