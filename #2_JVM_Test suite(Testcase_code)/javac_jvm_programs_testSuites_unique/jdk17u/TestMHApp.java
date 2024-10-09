import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class TestMHApp {

    public static void main(String[] args) throws Exception {
        try {
            Class<?> testClass = Class.forName(args[0]);
            System.out.println(testClass);
            Object obj = testClass.newInstance();
            final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(testClass.getDeclaredMethods()));
            for (final Method method : allMethods) {
                method.setAccessible(true);
                Annotation[] annotations = null;
                try {
                    annotations = method.getDeclaredAnnotations();
                } catch (Throwable th) {
                    System.out.println("skipping method");
                    continue;
                }
                boolean isTest = false;
                for (Annotation annotation : annotations) {
                    String annotationString = annotation.toString();
                    System.out.println("     annotation: " + annotationString);
                    if (annotationString.startsWith("@org.junit.Test")) {
                        isTest = true;
                    }
                }
                if (isTest) {
                    System.out.println("    invoking method: " + method.getName());
                    try {
                        method.invoke(obj);
                    } catch (IllegalAccessException iae) {
                        System.out.println("Got IllegalAccessException!!!");
                        System.out.println(iae.getCause());
                    } catch (InvocationTargetException ite) {
                        System.out.println("Got InvocationTargetException!!!");
                        throw ite;
                    }
                }
            }
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Class not found: " + args[0]);
        } catch (java.lang.IllegalAccessError iae) {
            System.out.println("Skipping test: " + args[0]);
        }
    }
}
