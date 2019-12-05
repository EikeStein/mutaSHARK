package de.ugoe.cs.smartshark.mutaSHARK.util.defects4j;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Defects4JLoader
{
    public static final String defects4jPath = "D:\\defects4j";
    public static final String defects4jBinPath = defects4jPath + "\\framework\\bin";

    public static final String checkouttargetRootPath = "/Defects4JCheckouts";

    public Defects4JLoader()
    {

    }

    public Defects4JFix Load(Defects4JProjectName projectName, int index) throws IOException, InterruptedException
    {
        String rootPath = checkouttargetRootPath + "/" + projectName.name + "/" + index;
        String buggyPath = rootPath + "/" + "buggy";
        String fixedPath = rootPath + "/" + "fixed";
        String defects4jPath = Paths.get(defects4jBinPath, "defects4j").toString();

        String buggyCommand = "cmd /C start /wait perl \"" + defects4jPath + "\" checkout -p " + projectName.name + " -v " + index + "b" + " -w \"" + buggyPath+"\"";
        int result = Runtime.getRuntime().exec(buggyCommand).waitFor();
        Runtime.getRuntime().exec("cmd /C start /wait perl " + defects4jPath + " checkout -p " + projectName.name + " -v " + index + "f" + " -w " + fixedPath).waitFor();

        return new Defects4JFix();
    }

    public List<Defects4JFix> LoadAll() throws IOException, InterruptedException
    {
        ArrayList<Defects4JFix> results = new ArrayList<>();

        String projectsFolder = Paths.get(defects4jPath, "framework", "projects").toString();
        File[] directories = new File(projectsFolder).listFiles(File::isDirectory);

        for (File directory : Objects.requireNonNull(directories))
        {
            File modified_classes = new File(directory.getAbsolutePath(), "modified_classes");
            if(!modified_classes.exists())
                continue;
            for (File commit : Objects.requireNonNull(modified_classes.listFiles()))
            {
                String commitName = commit.getName();
                commitName = commitName.substring(0,commitName.indexOf('.'));
                Defects4JFix defects4JFix = Load(new Defects4JProjectName(directory.getName()), Integer.parseInt(commitName));
                results.add(defects4JFix);
            }
        }
        return results;

    }

}
