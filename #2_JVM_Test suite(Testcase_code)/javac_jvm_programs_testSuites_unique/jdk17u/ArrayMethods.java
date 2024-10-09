import java.lang.reflect.*;
import java.util.Arrays;

public class ArrayMethods {

    public int failed = 0;

    public static void main(String[] args) throws Exception {
        ArrayMethods m = new ArrayMethods();
        m.testGetMethod();
        m.testGetMethods();
        m.testGetDeclaredMethod();
        m.testGetDeclaredMethods();
        m.testGetInterfaces();
        if (m.failed != 0)
            throw new RuntimeException("Test failed, check log for details");
    }

    public void testGetMethod() {
        try {
            Method m = new String[0].getClass().getMethod("clone", (Class<?>[]) null);
            failed++;
            System.out.println("getMethod(\"clone\", null) Should not find clone()");
        } catch (NoSuchMethodException e) {
            ;
        }
    }

    public void testGetMethods() {
        Method[] m = new Integer[0][0][0].getClass().getMethods();
        for (Method mm : m) if (mm.getName().contentEquals("clone")) {
            failed++;
            System.out.println("getMethods() Should not find clone()");
        }
    }

    public void testGetDeclaredMethod() {
        try {
            Method m = new Object[0][0].getClass().getDeclaredMethod("clone", (Class<?>[]) null);
            failed++;
            System.out.println("getDeclaredMethod(\"clone\", null) Should not find clone()");
        } catch (NoSuchMethodException e) {
            ;
        }
    }

    public void testGetDeclaredMethods() {
        Method[] m = new Throwable[0][0][0][0].getClass().getDeclaredMethods();
        if (m.length != 0) {
            failed++;
            System.out.println("getDeclaredMethods().length should be 0");
        }
    }

    public void testGetInterfaces() {
        Class<?>[] is = new Integer[0].getClass().getInterfaces();
        boolean thisFailed = false;
        if (is.length != 2)
            thisFailed = true;
        if (!is[0].getCanonicalName().equals("java.lang.Cloneable"))
            thisFailed = true;
        if (!is[1].getCanonicalName().equals("java.io.Serializable"))
            thisFailed = true;
        if (thisFailed) {
            failed++;
            System.out.println(Arrays.asList(is));
            System.out.println("Should contain exactly Cloneable, Serializable in that order.");
        }
    }
}
