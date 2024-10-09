import java.util.stream.Stream;

public class IsParallelCapable {

    public abstract static class TestCL extends ClassLoader {

        static {
            ClassLoader.registerAsParallelCapable();
        }

        public abstract boolean expectCapable();

        public Class findClass(String name) throws ClassNotFoundException {
            throw new ClassNotFoundException("Why are you using this?");
        }
    }

    public static class ParaCL extends TestCL {

        static {
            ClassLoader.registerAsParallelCapable();
        }

        @Override
        public boolean expectCapable() {
            return true;
        }
    }

    public static class NonParaCL extends TestCL {

        @Override
        public boolean expectCapable() {
            return false;
        }
    }

    public static class NonParaSubCL1 extends ParaCL {

        @Override
        public boolean expectCapable() {
            return false;
        }
    }

    public static class NonParaSubCL2 extends NonParaCL {

        static {
            ClassLoader.registerAsParallelCapable();
        }

        @Override
        public boolean expectCapable() {
            return false;
        }
    }

    public static class ParaSubCL extends ParaCL {

        static {
            ClassLoader.registerAsParallelCapable();
        }

        @Override
        public boolean expectCapable() {
            return true;
        }
    }

    public static void main(String[] args) throws Exception {
        if (!ClassLoader.getSystemClassLoader().isRegisteredAsParallelCapable()) {
            throw new RuntimeException("System classloader not parallel capable!?");
        }
        Stream.of(ParaCL.class, NonParaCL.class, NonParaSubCL1.class, NonParaSubCL2.class, ParaSubCL.class).forEach(IsParallelCapable::testClassLoaderClass);
    }

    private static void testClassLoaderClass(Class<? extends TestCL> klazz) {
        try {
            TestCL cl = (TestCL) klazz.newInstance();
            if (cl.expectCapable() != cl.isRegisteredAsParallelCapable()) {
                throw new RuntimeException(klazz + " expectCapable: " + cl.expectCapable() + ", isRegisteredAsParallelCapable: " + cl.isRegisteredAsParallelCapable());
            } else {
                System.out.println(klazz + " passed");
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
