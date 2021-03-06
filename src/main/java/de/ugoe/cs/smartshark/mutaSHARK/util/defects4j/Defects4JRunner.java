package de.ugoe.cs.smartshark.mutaSHARK.util.defects4j;

import de.ugoe.cs.smartshark.mutaSHARK.MutaShark;
import de.ugoe.cs.smartshark.mutaSHARK.util.SearchPath;
import de.ugoe.cs.smartshark.mutaSHARK.util.SearchResult;
import de.ugoe.cs.smartshark.mutaSHARK.util.TooManyActionsException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Defects4JRunner
{
    private final static String outputPath = "C:\\Users\\Eike\\OneDrive\\Documents\\Ausbildung\\Uni\\2020\\Master\\results\\withcheat\\results.csv";
    private final static List<String> results = new ArrayList<>();

    public static int total = 0;
    public static int skipped = 0;
    public static int fixed = 0;

    static FileWriter fileWriter;

    static
    {
        try
        {
            fileWriter = new FileWriter(outputPath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Defects4JRunner()
    {
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, InterruptedException
    {
        List<Defects4JBugFix> defects4JBugFixes = new Defects4JLoader(Defects4JRunner::handleBugFix).LoadAll();
        fileWriter.close();
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
            fileWriter.write(bugFix.name + "°" + bugFix.buggyClassFile + "°" + bugFix.fixedClassFile);
            fileWriter.flush();
            MutaShark.main(new ResearchConfig(bugFix.buggyClassFile, bugFix.fixedClassFile).getStartupParameters());
            final SearchResult searchResult = MutaShark.getSearchResult();
            addResultString(bugFix, searchResult);
            fileWriter.write("\n");
            if (searchResult.foundPaths.size() > 0)
            {
                fixed++;
            }
        }
        catch (IndexOutOfBoundsException | TooManyActionsException | MalformedInputException e)
        {
            fileWriter.write("°s");
            fileWriter.write("\n");
            skipped++;
        }
        // Progress info
        System.out.println("Fixed: " + fixed + "/" + total + " skipped: " + skipped + " results: " + results.size());
        fileWriter.flush();
    }

    // Generates the output format
    private static void addResultString(Defects4JBugFix bugFix, SearchResult searchResult) throws IOException
    {
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
            fileWriter.write("°" + result);
            fileWriter.flush();
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
            paras.add(foundPath.totalActionCount + "");
            paras.add(foundPath.remainingActionCount + "");
            paras.addAll(foundPath.edges.stream().map(e -> e.getToSearchNode().getCurrentTreeNode().toString()).collect(Collectors.toList()));
            String result = String.join("°", paras);
            fileWriter.write("°" + result);
            fileWriter.flush();
            results.add(result);
        }
    }
}
