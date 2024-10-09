import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;

public class DumpStackTest {

    public static void main(String[] args) {
        test();
        testThread();
        testLambda();
        testMethodInvoke();
        testMethodHandle();
    }

    static class CallFrame {

        final String classname;

        final String methodname;

        CallFrame(Class<?> c, String methodname) {
            this(c.getName(), methodname);
        }

        CallFrame(String classname, String methodname) {
            this.classname = classname;
            this.methodname = methodname;
        }

        String getClassName() {
            return classname;
        }

        String getMethodName() {
            return methodname;
        }

        String getFileName() {
            int i = classname.lastIndexOf('.');
            int j = classname.lastIndexOf('$');
            String name = classname.substring(i + 1, j >= 0 ? j : classname.length());
            return name + ".java";
        }

        @Override
        public String toString() {
            return classname + "." + methodname + "(" + getFileName() + ")";
        }
    }

    static void test() {
        CallFrame[] callStack = new CallFrame[] { new CallFrame(Thread.class, "getStackTrace"), new CallFrame(DumpStackTest.class, "test"), new CallFrame(DumpStackTest.class, "main"), new CallFrame("jdk.internal.reflect.NativeMethodAccessorImpl", "invoke0"), new CallFrame("jdk.internal.reflect.NativeMethodAccessorImpl", "invoke"), new CallFrame("jdk.internal.reflect.DelegatingMethodAccessorImpl", "invoke"), new CallFrame(Method.class, "invoke"), new CallFrame(Thread.class, "run") };
        assertStackTrace(Thread.currentThread().getStackTrace(), callStack);
        getStackTrace(callStack);
    }

    static void getStackTrace(CallFrame[] callStack) {
        callStack[0] = new CallFrame(DumpStackTest.class, "getStackTrace");
        try {
            throw new RuntimeException();
        } catch (RuntimeException ex) {
            assertStackTrace(ex.getStackTrace(), callStack);
        }
    }

    static void testThread() {
        Thread t1 = new Thread() {

            public void run() {
                c();
            }

            void c() {
                CallFrame[] callStack = new CallFrame[] { new CallFrame(Thread.class, "getStackTrace"), new CallFrame(this.getClass(), "c"), new CallFrame(this.getClass(), "run") };
                assertStackTrace(Thread.currentThread().getStackTrace(), callStack);
                DumpStackTest.getStackTrace(callStack);
            }
        };
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
        }
    }

    static void testLambda() {
        Consumer<Void> c = (x) -> consumeLambda();
        c.accept(null);
    }

    static void consumeLambda() {
        CallFrame[] callStack = new CallFrame[] { new CallFrame(Thread.class, "getStackTrace"), new CallFrame(DumpStackTest.class, "consumeLambda"), new CallFrame(DumpStackTest.class, "lambda$testLambda$0"), new CallFrame(DumpStackTest.class, "testLambda"), new CallFrame(DumpStackTest.class, "main"), new CallFrame("jdk.internal.reflect.NativeMethodAccessorImpl", "invoke0"), new CallFrame("jdk.internal.reflect.NativeMethodAccessorImpl", "invoke"), new CallFrame("jdk.internal.reflect.DelegatingMethodAccessorImpl", "invoke"), new CallFrame(Method.class, "invoke"), new CallFrame(Thread.class, "run") };
        assertStackTrace(Thread.currentThread().getStackTrace(), callStack);
        DumpStackTest.getStackTrace(callStack);
    }

    static void testMethodInvoke() {
        try {
            Method m = DumpStackTest.class.getDeclaredMethod("methodInvoke");
            m.invoke(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void methodInvoke() {
        CallFrame[] callStack = new CallFrame[] { new CallFrame(Thread.class, "getStackTrace"), new CallFrame(DumpStackTest.class, "methodInvoke"), new CallFrame("jdk.internal.reflect.NativeMethodAccessorImpl", "invoke0"), new CallFrame("jdk.internal.reflect.NativeMethodAccessorImpl", "invoke"), new CallFrame("jdk.internal.reflect.DelegatingMethodAccessorImpl", "invoke"), new CallFrame(Method.class, "invoke"), new CallFrame(DumpStackTest.class, "testMethodInvoke"), new CallFrame(DumpStackTest.class, "main"), new CallFrame("jdk.internal.reflect.NativeMethodAccessorImpl", "invoke0"), new CallFrame("jdk.internal.reflect.NativeMethodAccessorImpl", "invoke"), new CallFrame("jdk.internal.reflect.DelegatingMethodAccessorImpl", "invoke"), new CallFrame(Method.class, "invoke"), new CallFrame(Thread.class, "run") };
        assertStackTrace(Thread.currentThread().getStackTrace(), callStack);
        DumpStackTest.getStackTrace(callStack);
    }

    static void testMethodHandle() {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            MethodHandle handle = lookup.findStatic(DumpStackTest.class, "methodHandle", MethodType.methodType(void.class));
            handle.invoke();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    static void methodHandle() {
        CallFrame[] callStack = new CallFrame[] { new CallFrame(Thread.class, "getStackTrace"), new CallFrame(DumpStackTest.class, "methodHandle"), new CallFrame(DumpStackTest.class, "testMethodHandle"), new CallFrame(DumpStackTest.class, "main"), new CallFrame("jdk.internal.reflect.NativeMethodAccessorImpl", "invoke0"), new CallFrame("jdk.internal.reflect.NativeMethodAccessorImpl", "invoke"), new CallFrame("jdk.internal.reflect.DelegatingMethodAccessorImpl", "invoke"), new CallFrame(Method.class, "invoke"), new CallFrame(Thread.class, "run") };
        assertStackTrace(Thread.currentThread().getStackTrace(), callStack);
        DumpStackTest.getStackTrace(callStack);
    }

    static void assertStackTrace(StackTraceElement[] actual, CallFrame[] expected) {
        System.out.println("--- Actual ---");
        Arrays.stream(actual).forEach(e -> System.out.println(e));
        System.out.println("--- Expected ---");
        Arrays.stream(expected).forEach(e -> System.out.println(e));
        for (int i = 0, j = 0; i < actual.length; i++) {
            if (actual[i].getClassName().startsWith("com.sun.javatest.regtest"))
                continue;
            assertEquals(actual[i], expected[j++], i);
        }
    }

    static void assertEquals(StackTraceElement actual, CallFrame expected, int idx) {
        if (!actual.getClassName().equals(expected.getClassName()) || !actual.getFileName().equals(expected.getFileName()) || !actual.getMethodName().equals(expected.getMethodName())) {
            throw new RuntimeException("StackTraceElements mismatch at index " + idx + ". Expected [" + expected + "], but get [" + actual + "]");
        }
    }
}
