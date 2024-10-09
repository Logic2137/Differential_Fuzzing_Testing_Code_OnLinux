
package test.java.lang.invoke;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class CountedLoopIterationCountsTest {

    public static void main(String[] args) throws Throwable {
        run(1, -10, 0);
        run(1, 0, 0);
        run(Integer.MAX_VALUE - 1, Integer.MIN_VALUE + 10, 0);
        run(Integer.MIN_VALUE, Integer.MIN_VALUE + 4, 4);
        run(Integer.MAX_VALUE - 2, Integer.MAX_VALUE - 1, 1);
        run(Integer.MAX_VALUE - 1, 0, 0);
        run(Integer.MAX_VALUE - 1, 10, 0);
        run(Integer.MAX_VALUE - 1, -10, 0);
        run(Integer.MAX_VALUE, Integer.MIN_VALUE + 10, 0);
        run(Integer.MAX_VALUE - 1, Integer.MAX_VALUE, 1);
        run(Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
        if (failed) {
            throw new AssertionError("one or more tests failed");
        }
    }

    static boolean failed = false;

    private static void run(int start, int end, int expectedIterations) throws Throwable {
        System.out.println("run from " + start + " to " + end);
        MethodHandle loop = MethodHandles.countedLoop(MethodHandles.constant(int.class, start), MethodHandles.constant(int.class, end), MH_m1, MH_step);
        int r = (int) loop.invoke();
        if (r + 1 != expectedIterations) {
            System.out.println("expected " + expectedIterations + " iterations, but got " + r);
            failed = true;
        }
    }

    static int step(int stepCount, int counter) {
        return stepCount + 1;
    }

    static final MethodHandle MH_m1;

    static final MethodHandle MH_step;

    static {
        try {
            MH_m1 = MethodHandles.constant(int.class, -1);
            MH_step = MethodHandles.lookup().findStatic(CountedLoopIterationCountsTest.class, "step", MethodType.methodType(int.class, int.class, int.class));
        } catch (Throwable t) {
            throw new ExceptionInInitializerError(t);
        }
    }
}
