package de.ugoe.cs.smartshark.mutaSHARK;

import de.ugoe.cs.smartshark.mutaSHARK.util.mutators.TreeMutationOperator;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StartUpOptions
{
    public final String pathToBug;
    public final String pathToFix;
    public final TreeMutationOperator[] mutations;

    public StartUpOptions(String pathToBug, String pathToFix, TreeMutationOperator... mutations)
    {
        this.pathToBug = pathToBug;
        this.pathToFix = pathToFix;
        this.mutations = mutations;
    }

    public static StartUpOptions parseArgs(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String pathToBug = "";
        String pathToFix = "";
        String mutatorPackage = "";

        String currentParameter = "b";
        for (String arg : args)
        {
            if (arg.startsWith("-"))
            {
                currentParameter = arg.substring(1);
            }
            else
            {
                switch (currentParameter.trim())
                {
                    case "b":
                    case "bug":
                    {
                        pathToBug = arg.trim();
                        currentParameter = "f";
                        break;
                    }
                    case "f":
                    case "fix":
                    {
                        pathToFix = arg.trim();
                        currentParameter = "m";
                        break;
                    }
                    case "m":
                    case "mutators":
                    {
                        mutatorPackage = arg.trim();
                        break;
                    }
                }
            }
        }

        TreeMutationOperator[] treeMutationOperators = initiateTreeMutators(mutatorPackage);

        return new StartUpOptions(pathToBug, pathToFix, treeMutationOperators);
    }

    private static TreeMutationOperator[] initiateTreeMutators(String mutatorPackage) throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        List<TreeMutationOperator> result = new ArrayList<>();

        List<String> mutators = new Reflections("de.ugoe.cs.smartshark.mutaSHARK.util.mutators").getSubTypesOf(TreeMutationOperator.class).stream().filter(c -> !Modifier.isAbstract(c.getModifiers()) && c.getName().contains(mutatorPackage)).map(Class::getName).collect(Collectors.toList());

        for (String mutator : mutators)
        {
            TreeMutationOperator treeMutationOperator = (TreeMutationOperator) Class.forName(mutator).newInstance();
            result.add(treeMutationOperator);
        }
        TreeMutationOperator[] resultArray = new TreeMutationOperator[mutators.size()];
        result.toArray(resultArray);
        return resultArray;
    }

}
