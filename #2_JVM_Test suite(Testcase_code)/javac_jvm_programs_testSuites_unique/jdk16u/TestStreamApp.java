

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class TestStreamApp {
    public static final String testAnnotation = "@org.testng.annotations.Test";
    public static void main(String args[]) throws Exception {
        try {
            Class<?> testClass = Class.forName(args[0]);
            System.out.println(testClass);
            Annotation[] classAnnotations = null;
            boolean classHasTestAnnotation = false;
            try {
                classAnnotations= testClass.getDeclaredAnnotations();
                for (Annotation classAnnotation : classAnnotations) {
                    String annoString = classAnnotation.toString();
                    System.out.println("     class annotation: " + annoString);
                    if (annoString.startsWith(testAnnotation)) {
                        classHasTestAnnotation = true;
                    }
                }
            } catch (Throwable th) {
                System.out.println("Skip class annotation");
            }
            Object obj = testClass.newInstance();
            final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(testClass.getDeclaredMethods()));
            for (final Method method : allMethods) {
                
                Annotation[] annotations = method.getDeclaredAnnotations();
                boolean isTest = false;
                
                if (method.getName().startsWith("test")) {
                    isTest = true;
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
            System.out.println("Class not found!");
        }
    }
}
