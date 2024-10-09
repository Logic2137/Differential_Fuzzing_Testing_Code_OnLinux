



import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ClassForNameTest {
    static final String NAME = ClassForNameTest.class.getName();

    public static void main(String[] args) throws Throwable {
        {
            final MethodType mt = MethodType.methodType(Class.class, String.class);
            final MethodHandle mh = MethodHandles.lookup()
                    .findStatic(Class.class, "forName", mt);

            Class.forName(NAME);

            mh.invoke(NAME);
            mh.bindTo(NAME).invoke();
            mh.invokeWithArguments(Arrays.asList(NAME));
            mh.invokeWithArguments(NAME);
            Class cls = (Class) mh.invokeExact(NAME);
        }

        {
            final Method fnMethod = Class.class.getMethod("forName", String.class);
            final MethodType mt = MethodType.methodType(Object.class, Object.class, Object[].class);
            final MethodHandle mh = MethodHandles.lookup()
                    .findVirtual(Method.class, "invoke", mt)
                    .bindTo(fnMethod);

            fnMethod.invoke(null, NAME);

            mh.bindTo(null).bindTo(new Object[]{NAME}).invoke();
            mh.invoke(null, new Object[]{NAME});
            mh.invokeWithArguments(null, new Object[]{NAME});
            mh.invokeWithArguments(Arrays.asList(null, new Object[]{NAME}));
            Object obj = mh.invokeExact((Object) null, new Object[]{NAME});
        }

        {
            final Method fnMethod = Class.class.getMethod("forName", String.class);
            final MethodType mt = MethodType.methodType(Object.class, Object.class, Object[].class);

            final MethodHandle mh = MethodHandles.lookup()
                    .bind(fnMethod, "invoke", mt);

            mh.bindTo(null).bindTo(new Object[]{NAME}).invoke();
            mh.invoke(null, new Object[]{NAME});
            mh.invokeWithArguments(null, NAME);
            mh.invokeWithArguments(Arrays.asList(null, NAME));
            Object obj = mh.invokeExact((Object) null, new Object[]{NAME});
        }

        {
            final Method fnMethod = Class.class.getMethod("forName", String.class);
            final MethodHandle mh = MethodHandles.lookup().unreflect(fnMethod);

            mh.bindTo(NAME).invoke();
            mh.invoke(NAME);
            mh.invokeWithArguments(NAME);
            mh.invokeWithArguments(Arrays.asList(NAME));
            Class cls = (Class) mh.invokeExact(NAME);
        }

        System.out.println("TEST PASSED");
    }
}
