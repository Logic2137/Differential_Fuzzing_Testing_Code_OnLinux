
package test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

public class FindSpecial {

    private static final Lookup LOOKUP = MethodHandles.lookup();

    public static void main(String... args) throws Throwable {
        findSpecialTest();
        unreflectSpecialTest();
        reflectMethodInvoke();
    }

    static void findSpecialTest() throws Throwable {
        Method m = Comparator.class.getMethod("reversed");
        MethodType mt = MethodType.methodType(m.getReturnType(), m.getParameterTypes());
        MethodHandle mh = LOOKUP.findSpecial(Comparator.class, m.getName(), mt, Comparator.class);
        MethodHandle mh1 = LOOKUP.findSpecial(m.getDeclaringClass(), m.getName(), mt, MyComparator.class);
        Comparator<Object> cmp = new MyComparator();
        Object o = mh.invoke(cmp);
        Object o1 = mh1.invoke(cmp);
    }

    static void unreflectSpecialTest() throws Throwable {
        Method m = Comparator.class.getMethod("reversed");
        MethodHandle mh = LOOKUP.unreflectSpecial(m, Comparator.class);
        MethodHandle mh1 = LOOKUP.unreflectSpecial(m, MyComparator.class);
        Comparator<Object> cmp = new MyComparator();
        Object o = mh.invoke(cmp);
        Object o1 = mh1.invoke(cmp);
    }

    static void reflectMethodInvoke() throws Throwable {
        Method m = Comparator.class.getMethod("reversed");
        try {
            Object o = m.invoke(new MyComparator());
            throw new RuntimeException("should throw an exception");
        } catch (InvocationTargetException e) {
            if (!(e.getCause() instanceof Error && e.getCause().getMessage().equals("should not reach here"))) {
                throw e.getCause();
            }
        }
    }

    static class MyComparator implements Comparator<Object> {

        public int compare(Object o1, Object o2) {
            return 0;
        }

        @Override
        public Comparator<Object> reversed() {
            throw new Error("should not reach here");
        }
    }
}
