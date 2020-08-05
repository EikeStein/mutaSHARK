package de.ugoe.cs.smartshark.mutaSHARK.util.defects4j;

import de.ugoe.cs.smartshark.mutaSHARK.MutaShark;

import java.util.ArrayList;

public class ResearchConfig
{
    private final String bugPath;
    private final String fixPath;
    private final String pathCount = "1";
    private final String maxPathLength = "105";

    public ResearchConfig(String bugPath, String fixPath){
        this.bugPath = bugPath;
        this.fixPath = fixPath;
    }

    public String[] getStartupParameters(){
        return new String[]{bugPath, fixPath, "-m", "active", "cheated", "-p", pathCount, "-d", maxPathLength};
    }
}
