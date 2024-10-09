



import static java.lang.reflect.Modifier.*;

public class T8034044 {
    enum En {
        V() {}
    }

    static class InnStat {
        static Object o = new Object() {};
    }

    public static void main(String[] args)
            throws Throwable {
        Object o = new Object() {};
        test(o.getClass());
        test(En.V.getClass());
        test(InnStat.o.getClass());
        new T8034044().f();
    }

    public void f() {
        Object o = new Object() {};
        test(o.getClass());
    }

    static void test(Class clazz) {
        if ((clazz.getModifiers() & STATIC) != 0)
            throw new AssertionError(clazz.toString() +
                    " should not be static");
    }
}
