package de.ugoe.cs.smartshark.mutaSHARK.testclasses.from;

public class ClassA
{
    public void methodA()
    {
        int i = 1;
        i = 2;
        {
            i = GetNum();
            int j = GetNum(7) + 1;
        }
    }

    private int GetNum()
    {
        return 0;
    }

    private int GetNum(int i)
    {
        return 0;
    }
}
