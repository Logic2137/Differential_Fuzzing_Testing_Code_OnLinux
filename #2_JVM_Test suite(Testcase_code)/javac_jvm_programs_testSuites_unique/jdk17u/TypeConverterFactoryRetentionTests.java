import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.Supplier;
import jdk.dynalink.DynamicLinkerFactory;
import jdk.dynalink.linker.GuardedInvocation;
import jdk.dynalink.linker.GuardingDynamicLinker;
import jdk.dynalink.linker.GuardingTypeConverterFactory;
import jdk.dynalink.linker.LinkRequest;
import jdk.dynalink.linker.LinkerServices;

public class TypeConverterFactoryRetentionTests {

    private static final int MAX_ITERATIONS = 1000;

    private static class TestLinker implements GuardingDynamicLinker, GuardingTypeConverterFactory {

        public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) {
            throw new UnsupportedOperationException();
        }

        public GuardedInvocation convertToType(Class<?> sourceType, Class<?> targetType, Supplier<MethodHandles.Lookup> lookupSupplier) {
            MethodHandle result = MethodHandles.empty(MethodType.methodType(targetType, sourceType));
            return new GuardedInvocation(result);
        }
    }

    private static class TestClassLoader extends ClassLoader {

        private final String name;

        TestClassLoader(ClassLoader parent, String name) {
            super(parent);
            this.name = name;
            if (this.name.length() != 1) {
                throw new IllegalArgumentException();
            }
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            if (this.name.equals(name)) {
                byte[] bytes = Base64.getDecoder().decode("yv66vgAAADQACgoAAgADBwAEDAAFAAYBABBqYXZhL2xhbmcvT2Jq" + "ZWN0AQAGPGluaXQ+AQADKClWBwAIAQABWAEABENvZGUAMQAHAAIA" + "AAAAAAEAAgAFAAYAAQAJAAAAEQABAAEAAAAFKrcAAbEAAAAAAAA=");
                assert bytes[63] == 'X';
                bytes[63] = (byte) name.charAt(0);
                return defineClass(name, bytes, 0, bytes.length);
            }
            throw new ClassNotFoundException();
        }
    }

    public static void main(String[] args) throws Exception {
        testSystemLoaderToOtherLoader();
        testParentToChildLoader();
        testUnrelatedLoaders();
    }

    private static final LinkerServices createLinkerServices() {
        DynamicLinkerFactory f = new DynamicLinkerFactory();
        f.setFallbackLinkers();
        f.setPrioritizedLinker(new TestLinker());
        return f.createLinker().getLinkerServices();
    }

    private static void testSystemLoaderToOtherLoader() throws ClassNotFoundException {
        testFromOneClassToClassLoader(double.class);
    }

    private static void testParentToChildLoader() throws ClassNotFoundException {
        TestClassLoader parent = new TestClassLoader(null, "Y");
        Class<?> y = Class.forName("Y", true, parent);
        testFromOneClassToClassLoader(y);
    }

    private static void testFromOneClassToClassLoader(Class<?> y) throws ClassNotFoundException {
        ReferenceQueue<ClassLoader> refQueue = new ReferenceQueue<>();
        List<PhantomReference<ClassLoader>> refs = new ArrayList<>();
        LinkerServices linkerServices = createLinkerServices();
        for (int count = 0; count < MAX_ITERATIONS; count++) {
            TestClassLoader cl = new TestClassLoader(y.getClassLoader(), "X");
            Class<?> x = Class.forName("X", true, cl);
            assert x.getClassLoader() == cl;
            linkerServices.getTypeConverter(y, x);
            linkerServices.getTypeConverter(x, y);
            refs.add(new PhantomReference<>(cl, refQueue));
            System.gc();
            if (refQueue.poll() != null) {
                return;
            }
        }
        throw new AssertionError("Should have GCd a class loader by now");
    }

    private static void testUnrelatedLoaders() throws ClassNotFoundException {
        ReferenceQueue<ClassLoader> refQueue1 = new ReferenceQueue<>();
        ReferenceQueue<ClassLoader> refQueue2 = new ReferenceQueue<>();
        List<PhantomReference<ClassLoader>> refs = new ArrayList<>();
        boolean gc1 = false;
        boolean gc2 = false;
        LinkerServices linkerServices = createLinkerServices();
        for (int count = 0; count < MAX_ITERATIONS; count++) {
            TestClassLoader cl1 = new TestClassLoader(null, "X");
            Class<?> x = Class.forName("X", true, cl1);
            assert x.getClassLoader() == cl1;
            TestClassLoader cl2 = new TestClassLoader(null, "Y");
            Class<?> y = Class.forName("Y", true, cl2);
            assert y.getClassLoader() == cl2;
            linkerServices.getTypeConverter(y, x);
            linkerServices.getTypeConverter(x, y);
            refs.add(new PhantomReference<>(cl1, refQueue1));
            refs.add(new PhantomReference<>(cl2, refQueue2));
            System.gc();
            if (refQueue1.poll() != null) {
                gc1 = true;
            }
            if (refQueue2.poll() != null) {
                gc2 = true;
            }
            if (gc1 && gc2) {
                return;
            }
        }
        throw new AssertionError("Should have GCd a class loader from both queues by now");
    }
}
