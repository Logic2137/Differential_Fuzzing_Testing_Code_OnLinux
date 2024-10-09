


import java.net.URL;
import java.net.URLClassLoader;
import java.io.File;
import java.lang.reflect.Method;

public class JdiLoadedByCustomLoader {

    public static void main(String args[]) throws Exception {
        
        File f1 = new File(args[0]);

        
        URL[] urls = { f1.toURL() };
        URLClassLoader cl = new URLClassLoader(urls);

        
        
        Class c = Class.forName("ListConnectors", true, cl);
        Method m = c.getDeclaredMethod("list");
        Object o = c.newInstance();
        m.invoke(o);
    }
}
