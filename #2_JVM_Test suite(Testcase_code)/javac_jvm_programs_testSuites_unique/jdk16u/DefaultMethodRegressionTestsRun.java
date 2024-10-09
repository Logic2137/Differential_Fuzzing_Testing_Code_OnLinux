


import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class DefaultMethodRegressionTestsRun {
    public static void main(String... args) throws Exception {
        File scratchDir = new File(".");
        File testDir = new File(scratchDir, "testdir");
        testDir.mkdirs();
        File srcFile = new File(new File(System.getProperty("test.src")),
                "DefaultMethodRegressionTests.java");
        String[] javacargs = {
            srcFile.getAbsolutePath(),
            "-d",
            testDir.getAbsolutePath()
        };
        com.sun.tools.javac.Main.compile(javacargs);
        runClass(testDir, "DefaultMethodRegressionTests");
    }
    static void runClass(
            File classPath,
            String classname) throws Exception {
        URL[] urls = {classPath.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(urls);
        Class<?> c = loader.loadClass(classname);

        Class<?>[] argTypes = new Class<?>[]{String[].class};
        Object[] methodArgs = new Object[]{null};

        Method method = c.getMethod("main", argTypes);
        method.invoke(c, methodArgs);
    }
}
