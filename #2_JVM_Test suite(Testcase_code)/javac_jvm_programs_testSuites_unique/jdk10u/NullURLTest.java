



import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

public class NullURLTest {
    JarFile jarFile;

    public static void main(String[] args) throws Throwable {
        new NullURLTest();
    }

    NullURLTest() throws Throwable {
        File local = new File(System.getProperty("test.src", "."), "jars");
        String path = "jar:file:"
                + local.getPath()
                + "/class_path_test.jar!/Foo.class";

        URL validURL = new URL(path);
        URL[] validURLArray = new URL[] { validURL, validURL };
        URL[] invalidURLArray = new URL[] { validURL, null };

        int failures = 0;
        URLClassLoader loader;

        try {
            loader = new URLClassLoader(validURLArray);
        } catch (Throwable t) {
            System.err.println("URLClassLoader(validURLArray) threw " + t);
            failures++;
        }
        try {
            loader = new URLClassLoader(null);
            System.err.println("URLClassLoader(null) did not throw NPE");
            failures++;
        } catch (NullPointerException e) {
            
        }
        








        try {
            loader = new URLClassLoader(validURLArray, null);
        } catch (Throwable t) {
            System.err.println("URLClassLoader(validURLArray, null) threw " + t);
            failures++;
        }
        try {
            loader = new URLClassLoader(null, null);
            System.err.println("URLClassLoader(null, null) did not throw NPE");
            failures++;
        } catch (NullPointerException e) {
            
        }
        








        try {
            loader = new URLClassLoader(validURLArray, null, null);
        } catch (Throwable t) {
            System.err.println("URLClassLoader(validURLArray, null, null) threw " + t);
            failures++;
        }
        try {
            loader = new URLClassLoader((URL[])null, null, null);
            System.err.println("URLClassLoader(null, null, null) did not throw NPE");
            failures++;
        } catch (NullPointerException e) {
            
        }
        








        try {
            loader = URLClassLoader.newInstance(validURLArray);
        } catch (Throwable t) {
            System.err.println("URLClassLoader.newInstance(validURLArray) threw " + t);
            failures++;
        }
        try {
            loader = URLClassLoader.newInstance(null);
            System.err.println("URLClassLoader.newInstance(null) did not throw NPE");
            failures++;
        } catch (NullPointerException e) {
            
        }
        








        try {
            loader = URLClassLoader.newInstance(validURLArray, null);
        } catch (Throwable t) {
            System.err.println("URLClassLoader.newInstance(validURLArray, null) threw " + t);
            failures++;
        }
        try {
            loader = URLClassLoader.newInstance(null, null);
            System.err.println("URLClassLoader.newInstance(null, null) did not throw NPE");
            failures++;
        } catch (NullPointerException e) {
            
        }
        








        if (failures != 0) {
            throw new Exception("URLClassLoader NullURLTest had "+failures+" failures!");
        }
    }
}
