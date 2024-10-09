import java.lang.reflect.Method;

public class JNIBooleanTest {

    static native boolean foo(byte b);

    static boolean bar(byte b) {
        return foo(b);
    }

    public static void main(String[] args) throws Exception {
        int count = args.length > 0 ? Integer.parseInt(args[0]) : 50_000;
        byte b = 0;
        for (int i = 0; i < count; i++) {
            boolean bool = foo(b);
            if ((b == 0 && bool) || (b != 0 && !bool)) {
                throw new RuntimeException("Error: foo(" + b + ") = " + bool + " in iteration " + i);
            }
            b++;
        }
        b = 0;
        for (int i = 0; i < count; i++) {
            boolean bool = bar(b);
            if ((b == 0 && bool) || (b != 0 && !bool)) {
                throw new RuntimeException("Error: bar(" + b + ") = " + bool + " in iteration " + i);
            }
            b++;
        }
        Method foo = JNIBooleanTest.class.getDeclaredMethod("foo", byte.class);
        b = 0;
        for (int i = 0; i < count; i++) {
            boolean bool = ((Boolean) foo.invoke(null, b)).booleanValue();
            if ((b == 0 && bool) || (b != 0 && !bool)) {
                throw new RuntimeException("Error: foo(" + b + ") = " + bool + " in iteration " + i + " (reflective)");
            }
            b++;
        }
        Method bar = JNIBooleanTest.class.getDeclaredMethod("bar", byte.class);
        b = 0;
        for (int i = 0; i < count; i++) {
            boolean bool = ((Boolean) bar.invoke(null, b)).booleanValue();
            if ((b == 0 && bool) || (b != 0 && !bool)) {
                throw new RuntimeException("Error: bar(" + b + ") = " + bool + " in iteration " + i + " (reflective)");
            }
            b++;
        }
    }

    static {
        System.loadLibrary("JNIBooleanTest");
    }
}
