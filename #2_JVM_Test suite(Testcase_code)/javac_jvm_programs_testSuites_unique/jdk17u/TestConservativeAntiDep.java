import java.lang.reflect.Array;
import java.util.Arrays;

public class TestConservativeAntiDep {

    private static long longField;

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        for (int i = 0; i < 20_000; i++) {
            test1(A.class);
            test2(B.class);
        }
    }

    private static int test1(Class klass) {
        Object[] in = (Object[]) Array.newInstance(klass, 100);
        Object[] o = in;
        int v = 1;
        for (int i = 0; i < 100; i++) {
            longField = i;
            o = (A[]) o;
            v *= 2;
        }
        return v + o.length;
    }

    private static int test2(Class klass) throws IllegalAccessException, InstantiationException {
        A in = (A) klass.newInstance();
        A o = in;
        int v = 1;
        for (int i = 0; i < 100; i++) {
            longField = i;
            o = (B) o;
            v *= 2;
        }
        return v + o.intField;
    }

    private static class A {

        int intField;

        public A() {
        }
    }

    private static class B extends A {

        public B() {
        }
    }
}
