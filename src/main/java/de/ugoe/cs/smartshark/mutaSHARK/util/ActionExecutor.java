package de.ugoe.cs.smartshark.mutaSHARK.util;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.ITree;
import de.ugoe.cs.smartshark.mutaSHARK.util.defects4j.Defects4JBugFix;

import java.util.ArrayList;

public class ActionExecutor
{
    public ActionExecutor()
    {
    }


    public ITree executeAction(ITree originalTree, Action action)
    {
        ITree copy = originalTree.deepCopy();
        if (action instanceof Insert)
        {
            return executeInsert(copy, (Insert) action);
        }
        if (action instanceof Addition)
        {
            return executeAddition(copy, (Addition) action);
        }
        if (action instanceof Move)
        {
            return executeMove(copy, (Move) action);
        }
        if (action instanceof Update)
        {
            return executeUpdate(copy, (Update) action);
        }
        if (action instanceof Delete)
        {
            return executeDelete(copy, (Delete) action);
        }
        if (action instanceof Replace)
        {
            return executeReplace(copy, (Replace) action);
        }
        throw new UnsupportedOperationException("The action " + action.getName() + " is unsupported");
    }

    private ITree executeReplace(ITree copy, Replace action)
    {
        String url = TreeHelper.getUrl(action.getOriginalNode(), Integer.MAX_VALUE);
        ITree child = copy.getChild(url);
        ITree parent = child.getParent();
        ArrayList<ITree> newChildren = new ArrayList<>(parent.getChildren());
        for (int i = 0; i < newChildren.size(); i++)
        {
            ITree oldChild = newChildren.get(i);
            if (oldChild.equals(child))
            {
                newChildren.remove(oldChild);
                newChildren.add(i, action.getNewNode().deepCopy());
            }
        }
        parent.setChildren(newChildren);
        return copy;
    }

    private ITree executeDelete(ITree copy, Delete action)
    {
        return null;
    }

    private ITree executeUpdate(ITree copy, Update action)
    {
        return null;
    }

    private ITree executeMove(ITree copy, Move action)
    {
        return null;
    }

    private ITree executeAddition(ITree copy, Addition action)
    {
        return null;
    }

    private ITree executeInsert(ITree copy, Insert action)
    {
        return null;
    }
}
