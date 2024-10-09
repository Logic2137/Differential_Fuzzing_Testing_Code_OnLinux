import java.util.*;
import java.util.function.Consumer;
import javax.imageio.spi.*;

public class ServiceRegistryRestriction {

    static class DummyTestSpi {
    }

    ClassLoader cl = ServiceRegistryRestriction.class.getClassLoader();

    <T> void construct(Class<T> clazz) {
        List<Class<?>> list = Arrays.<Class<?>>asList(clazz);
        ServiceRegistry sr = new ServiceRegistry(list.iterator());
    }

    <T> void lookup(Class<T> clazz) {
        Iterator<T> i = ServiceRegistry.lookupProviders(clazz);
    }

    <T> void lookupCL(Class<T> clazz) {
        Iterator<T> i = ServiceRegistry.lookupProviders(clazz, cl);
    }

    <T> void doOneTest(String label, Class<T> clazz, boolean expectFail, Consumer<Class<T>> op) {
        System.out.printf("testing %s with %s...", label, clazz.getName());
        try {
            op.accept(clazz);
            if (expectFail) {
                throw new AssertionError("fail, operation succeeded unexpectedly");
            } else {
                System.out.println("success");
            }
        } catch (IllegalArgumentException iae) {
            if (expectFail) {
                System.out.println("success, got expected IAE");
            } else {
                throw new AssertionError("fail, unexpected exception", iae);
            }
        }
    }

    void doTests(Class<?> clazz, boolean expectFail) {
        doOneTest("constructor", clazz, expectFail, this::construct);
        doOneTest("lookup", clazz, expectFail, this::lookup);
        doOneTest("lookupCL", clazz, expectFail, this::lookupCL);
    }

    void run() {
        doTests(ImageInputStreamSpi.class, false);
        doTests(ImageOutputStreamSpi.class, false);
        doTests(ImageReaderSpi.class, false);
        doTests(ImageTranscoderSpi.class, false);
        doTests(ImageWriterSpi.class, false);
        doTests(DummyTestSpi.class, true);
    }

    public static void main(String[] args) {
        new ServiceRegistryRestriction().run();
    }
}
