package de.ugoe.cs.smartshark.mutaSHARK.testclasses.to;

public class ClassA
{
    public void methodA()
    {
        int i = 1;
        i = 2;
        i++;
        {
            i = GetNum();
            int j = GetNum(7) + 1;
            --j;
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
