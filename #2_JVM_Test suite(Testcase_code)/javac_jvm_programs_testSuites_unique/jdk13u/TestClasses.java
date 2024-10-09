
package jdk.jfr.event.runtime;

public class TestClasses {

    protected TestClassPrivate testClassPrivate;

    protected TestClassPrivateStatic testClassPrivateStatic;

    public TestClasses() {
        testClassPrivate = new TestClassPrivate();
        testClassPrivateStatic = new TestClassPrivateStatic();
    }

    private class TestClassPrivate {
    }

    private static class TestClassPrivateStatic {
    }

    protected class TestClassProtected {
    }

    protected static class TestClassProtectedStatic {
    }

    public void loadClasses() throws ClassNotFoundException {
        final ClassLoader cl = getClass().getClassLoader();
        cl.loadClass("jdk.jfr.event.runtime.TestClasses$TestClassProtected1");
        cl.loadClass("jdk.jfr.event.runtime.TestClasses$TestClassProtectedStatic1");
    }

    protected class TestClassProtected1 {
    }

    protected static class TestClassProtectedStatic1 {

        protected TestClassProtectedStaticInner testClassProtectedStaticInner = new TestClassProtectedStaticInner();

        protected static class TestClassProtectedStaticInner {
        }
    }

    public static class TestClassPublicStatic {

        public static class TestClassPublicStaticInner {
        }
    }
}

class TestClass {

    static {
        Runnable r = () -> System.out.println("Hello");
        r.run();
    }
}
