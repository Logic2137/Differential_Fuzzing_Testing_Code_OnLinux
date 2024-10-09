



import java.awt.*;

import java.lang.reflect.*;

import sun.awt.*;

public class TestWrapped
{
    public static void main(String[] args)
    {
        try
        {
        if (args.length != 1) {
            System.err.println("No correct toolkit class name is specified, test is not run");
            System.exit(0);
        }

        String correctToolkitClassName = args[0];
        Toolkit tk = Toolkit.getDefaultToolkit();
        Class tkClass = tk.getClass();
        if (!tkClass.getName().equals("sun.awt.HeadlessToolkit"))
        {
            System.err.println(tkClass.getName());
            System.err.println("Error: default toolkit is not an instance of HeadlessToolkit");
            System.exit(-1);
        }

        Field f = tkClass.getDeclaredField("tk");
        f.setAccessible(true);
        Class wrappedClass = f.get(tk).getClass();
        if (!wrappedClass.getName().equals(correctToolkitClassName)) {
            System.err.println(wrappedClass.getName());
            System.err.println("Error: wrapped toolkit is not an instance of " + correctToolkitClassName);
            System.exit(-1);
        }
        }
        catch (Exception z)
        {
            z.printStackTrace(System.err);
            System.exit(-1);
        }

        System.exit(0);
    }
}
