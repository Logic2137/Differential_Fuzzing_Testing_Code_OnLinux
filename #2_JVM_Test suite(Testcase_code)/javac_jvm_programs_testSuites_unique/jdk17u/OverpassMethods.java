import java.lang.reflect.Method;
import java.util.Arrays;

public class OverpassMethods {

    static {
        try {
            System.loadLibrary("OverpassMethods");
        } catch (UnsatisfiedLinkError ex) {
            System.err.println("Could not load OverpassMethods library");
            System.err.println("java.library.path:" + System.getProperty("java.library.path"));
            throw ex;
        }
    }

    static private void log(Object msg) {
        System.out.println(String.valueOf(msg));
    }

    static private native Method[] getJVMTIDeclaredMethods(Class<?> klass);

    public interface Parent {

        default String def() {
            return "Parent.def";
        }

        String method0();

        String method1();
    }

    public interface Child extends Parent {

        String method2();
    }

    public static class Impl implements Child {

        public String method0() {
            return "Impl.method0";
        }

        public String method1() {
            return "Impl.method1";
        }

        public String method2() {
            return "Impl.method2";
        }
    }

    public static void main(String[] args) {
        new Impl();
        Method[] reflectMethods = Child.class.getDeclaredMethods();
        Method[] jvmtiMethods = getJVMTIDeclaredMethods(Child.class);
        if (jvmtiMethods == null) {
            throw new RuntimeException("getJVMTIDeclaredMethods failed");
        }
        log("Reflection getDeclaredMethods returned: " + Arrays.toString(reflectMethods));
        log("JVMTI GetClassMethods returned: " + Arrays.toString(jvmtiMethods));
        if (reflectMethods.length != jvmtiMethods.length) {
            throw new RuntimeException("OverpassMethods failed: Unexpected method count from JVMTI GetClassMethods!");
        }
        if (!reflectMethods[0].equals(jvmtiMethods[0])) {
            throw new RuntimeException("OverpassMethods failed: Unexpected method from JVMTI GetClassMethods!");
        }
        log("Test passed: Got expected output from JVMTI GetClassMethods!");
    }
}
