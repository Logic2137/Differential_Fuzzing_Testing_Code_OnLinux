



import java.lang.reflect.*;

public class StackTraceClassCache  {
    public static void main(String... args) throws Exception {
        Outer.Inner o = new Outer().new Inner();
        Class cl = o.getClass();

        
        try {
            o.work();
        } catch (Exception e) {
            checkException(e, 42);
        }

        
        clearNameCache(cl);
        cl.getName();
        try {
            o.work();
        } catch (Exception e) {
            checkException(e, 51);
        }

        
        clearNameCache(cl);
        try {
            o.work();
        } catch (Exception e) {
            checkException(e, 59);
        }
    }

    static void checkException(Exception e, int line) throws Exception {
        StackTraceElement[] fs = e.getStackTrace();

        if (fs.length < 2) {
            throw new IllegalStateException("Exception should have at least two frames", e);
        }

        assertCorrect("StackTraceClassCache$Outer$Inner.work(StackTraceClassCache.java:95)", fs[0].toString(), e);
        assertCorrect("StackTraceClassCache.main(StackTraceClassCache.java:" + line + ")",   fs[1].toString(), e);
    }

    static void assertCorrect(String expected, String actual, Exception e) throws Exception {
        if (!expected.equals(actual)) {
            throw new IllegalStateException("Expected: " + expected + "; Actual: " + actual, e);
        }
    }

    static void clearNameCache(Class cl) {
        try {
            Field f = Class.class.getDeclaredField("name");
            f.setAccessible(true);
            f.set(cl, null);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    static class Outer {
       class Inner {
           void work() throws Exception {
               throw new Exception("Sample exception");
           }
       }
    }

}
