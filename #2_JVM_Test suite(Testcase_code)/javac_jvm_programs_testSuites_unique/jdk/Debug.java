
package nsk.share;

public class Debug
{
    
    static public void Assert(boolean condition, String message)
    {
        if (!condition)
            Debug.Fail(message);
    }

    
    static public void Fail(String message)
    {
        System.out.println(message);
        System.exit(100);
    }

    static public void Fail(Throwable e)
    {
        Fail(e.toString());
    }
}
