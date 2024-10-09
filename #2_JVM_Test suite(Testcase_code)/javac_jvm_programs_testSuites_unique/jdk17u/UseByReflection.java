import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UseByReflection {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        Class mainClass = Class.forName("testpackage.Main");
        Method getMainVersion = mainClass.getMethod("getMainVersion");
        int mainVersionActual = (int) getMainVersion.invoke(null);
        Method getHelperVersion = mainClass.getMethod("getHelperVersion");
        int helperVersionActual = (int) getHelperVersion.invoke(null);
        ClassLoader cl = UseByReflection.class.getClassLoader();
        int resourceVersionActual;
        try (InputStream ris = cl.getResourceAsStream("versionResource");
            BufferedReader br = new BufferedReader(new InputStreamReader(ris))) {
            resourceVersionActual = Integer.parseInt(br.readLine());
        }
        System.out.println("Main version: " + mainVersionActual);
        System.out.println("Helpers version: " + helperVersionActual);
        System.out.println("Resource version: " + resourceVersionActual);
    }
}
