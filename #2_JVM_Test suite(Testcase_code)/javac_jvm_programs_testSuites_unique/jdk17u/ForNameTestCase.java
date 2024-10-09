
package optim;

public class ForNameTestCase {

    private static final String EXPECTED = "expected";

    public static Class<?> forName() {
        try {
            Class<?> cl = Class.forName("java.lang.String");
            return cl;
        } catch (ClassNotFoundException | IllegalArgumentException | ClassCastException x) {
            throw new InternalError(x);
        }
    }

    public static Class<?> forName0() throws ClassNotFoundException {
        return Class.forName("java.lang.String");
    }

    public static Class<?> forName1() throws Exception {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("java.lang.String");
        } catch (ClassNotFoundException e) {
            return null;
        }
        return clazz;
    }

    public static void forNameException() throws Exception {
        try {
            Class.forName("java.lang.String");
            throw new Exception(EXPECTED);
        } catch (ClassNotFoundException e) {
            return;
        } catch (RuntimeException e) {
            return;
        }
    }

    public static Class<?> forName2() throws Exception {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("java.lang.String");
            try {
                throw new Exception("das");
            } catch (Exception ex) {
            }
        } catch (ClassNotFoundException e) {
            return null;
        }
        return clazz;
    }

    public static Class<?> forName3() throws Exception {
        Class<?> clazz = null;
        try {
            return clazz = Class.forName("java.lang.String");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Class<?> forName4() throws Exception {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("java.lang.String");
        } catch (ClassNotFoundException e) {
            return null;
        } catch (RuntimeException e) {
            return null;
        }
        return clazz;
    }

    public static Class<?> forName5() {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("java.lang.String");
        } catch (ClassNotFoundException e) {
        }
        int i;
        try {
            i = 0;
        } catch (Exception e) {
        }
        return clazz;
    }

    public static Class<?> forName6() {
        Class<?> clazz = null;
        try {
            return Class.forName("java.lang.String");
        } catch (ClassNotFoundException e) {
        }
        try {
            int k = 0;
        } catch (RuntimeException e) {
        }
        int i;
        try {
            return Class.forName("TOTO");
        } catch (ClassNotFoundException e) {
        }
        try {
            return Class.forName("TOTO");
        } catch (ClassNotFoundException e) {
        }
        try {
            return Class.forName("TOTO");
        } catch (ClassNotFoundException e) {
        }
        try {
            return Class.forName("TOTO");
        } catch (ClassNotFoundException e) {
        }
        return clazz;
    }

    public static Class<?> forName7() {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("optim.AType");
        } catch (ClassNotFoundException e) {
        }
        return clazz;
    }

    public static Class<?> negativeforName() {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("jdk.internal.jimage.BasicImageReader");
        } catch (ClassNotFoundException e) {
        }
        return clazz;
    }
}
