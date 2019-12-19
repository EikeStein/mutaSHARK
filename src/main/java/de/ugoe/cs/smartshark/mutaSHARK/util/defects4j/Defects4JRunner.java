package de.ugoe.cs.smartshark.mutaSHARK.util.defects4j;

import de.ugoe.cs.smartshark.mutaSHARK.MutaShark;
import de.ugoe.cs.smartshark.mutaSHARK.util.TooManyActionsException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Defects4JRunner
{
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
            MutaShark.main(new String[]{bugFix.buggyClassFile, bugFix.fixedClassFile, "-m", "Rename", "Invert", "-p", "8", "-d", "105"});
            if (MutaShark.getSearchResult().foundPaths.size() > 0)
            {
                fixed++;
            }
        }
        catch (TooManyActionsException e)
        {
            skipped++;
        }
        System.out.println("Fixed: " + fixed + "/" + total + " skipped: " + skipped);
    }
}
