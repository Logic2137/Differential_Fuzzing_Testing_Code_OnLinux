import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.util.Arrays;

public class GetUnsafeTest {

    static final String NAME = "sun.misc.Unsafe";

    private static boolean isTestFailed = false;

    private static void fail() {
        isTestFailed = true;
        try {
            throw new Exception();
        } catch (Throwable e) {
            StackTraceElement frame = e.getStackTrace()[1];
            System.out.printf("Failed at %s:%d\n", frame.getFileName(), frame.getLineNumber());
        }
    }

    public static void main(String[] args) throws Throwable {
        {
            final MethodType mt = MethodType.methodType(Class.class, String.class);
            final MethodHandle mh = MethodHandles.lookup().findStatic(Class.class, "forName", mt);
            try {
                Class.forName(NAME);
                fail();
            } catch (Throwable e) {
            }
            try {
                mh.invoke(NAME);
                fail();
            } catch (Throwable e) {
            }
            try {
                mh.bindTo(NAME).invoke();
                fail();
            } catch (Throwable e) {
            }
            try {
                mh.invokeWithArguments(Arrays.asList(NAME));
                fail();
            } catch (Throwable e) {
            }
            try {
                mh.invokeWithArguments(NAME);
                fail();
            } catch (Throwable e) {
            }
            try {
                Class cls = (Class) mh.invokeExact(NAME);
                fail();
            } catch (Throwable e) {
            }
        }
        {
            final Method fnMethod = Class.class.getMethod("forName", String.class);
            final MethodType mt = MethodType.methodType(Object.class, Object.class, Object[].class);
            final MethodHandle mh = MethodHandles.lookup().findVirtual(Method.class, "invoke", mt).bindTo(fnMethod);
            try {
                fnMethod.invoke(null, NAME);
                fail();
            } catch (Throwable e) {
            }
            try {
                mh.bindTo(null).bindTo(new Object[] { NAME }).invoke();
                fail();
            } catch (Throwable e) {
            }
            try {
                mh.invoke(null, new Object[] { NAME });
                fail();
            } catch (Throwable e) {
            }
            try {
                mh.invokeWithArguments(null, new Object[] { NAME });
                fail();
            } catch (Throwable e) {
            }
            try {
                mh.invokeWithArguments(Arrays.asList(null, new Object[] { NAME }));
                fail();
            } catch (Throwable e) {
            }
            try {
                Object obj = mh.invokeExact((Object) null, new Object[] { NAME });
                fail();
            } catch (Throwable e) {
            }
        }
        {
            final Method fnMethod = Class.class.getMethod("forName", String.class);
            final MethodType mt = MethodType.methodType(Object.class, Object.class, Object[].class);
            final MethodHandle mh = MethodHandles.lookup().bind(fnMethod, "invoke", mt);
            try {
                mh.bindTo(null).bindTo(new Object[] { NAME }).invoke();
                fail();
            } catch (Throwable e) {
            }
            try {
                mh.invoke(null, new Object[] { NAME });
                fail();
            } catch (Throwable e) {
            }
            try {
                mh.invokeWithArguments(null, NAME);
                fail();
            } catch (Throwable e) {
            }
            try {
                mh.invokeWithArguments(Arrays.asList(null, NAME));
                fail();
            } catch (Throwable e) {
            }
            try {
                Object obj = mh.invokeExact((Object) null, new Object[] { NAME });
                fail();
            } catch (Throwable e) {
            }
        }
        {
            final Method fnMethod = Class.class.getMethod("forName", String.class);
            final MethodHandle mh = MethodHandles.lookup().unreflect(fnMethod);
            try {
                mh.bindTo(NAME).invoke();
                fail();
            } catch (Throwable e) {
            }
            try {
                mh.invoke(NAME);
                fail();
            } catch (Throwable e) {
            }
            try {
                mh.invokeWithArguments(NAME);
                fail();
            } catch (Throwable e) {
            }
            try {
                mh.invokeWithArguments(Arrays.asList(NAME));
                fail();
            } catch (Throwable e) {
            }
            try {
                Class cls = (Class) mh.invokeExact(NAME);
                fail();
            } catch (Throwable e) {
            }
        }
        if (!isTestFailed) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
            System.exit(1);
        }
    }
}
