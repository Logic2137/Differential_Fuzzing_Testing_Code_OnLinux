import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

public class SampleNest {

    static List<Class<?>> _nestedTypes = new LinkedList<>();

    static void gather(Class<?> c) {
        _nestedTypes.add(c);
        for (Class<?> d : c.getDeclaredClasses()) {
            gather(d);
        }
    }

    static {
        gather(SampleNest.class);
        SampleNest s = new SampleNest();
    }

    public static Class<?>[] nestedTypes() {
        return _nestedTypes.toArray(new Class<?>[0]);
    }

    static class StaticClass {
    }

    static interface StaticIface {
    }

    class InnerClass {
    }

    interface InnerIface {
    }

    static class DeepNest1 {

        static class DeepNest2 {

            static class DeepNest3 {
            }
        }
    }

    public SampleNest() {
        class LocalClass {
        }
        _nestedTypes.add(LocalClass.class);
        Runnable r = new Runnable() {

            public void run() {
                _nestedTypes.add(getClass());
            }
        };
        r.run();
    }
}
