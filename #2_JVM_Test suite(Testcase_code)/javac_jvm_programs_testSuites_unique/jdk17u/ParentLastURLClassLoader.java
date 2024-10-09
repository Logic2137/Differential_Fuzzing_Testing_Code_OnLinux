
package jdk.test.lib.classloader;

import java.net.URL;
import java.net.URLClassLoader;

public class ParentLastURLClassLoader extends URLClassLoader {

    public ParentLastURLClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            Class<?> c = findClass(name);
            if (c != null) {
                return c;
            }
        } catch (ClassNotFoundException e) {
        }
        return super.loadClass(name);
    }
}
