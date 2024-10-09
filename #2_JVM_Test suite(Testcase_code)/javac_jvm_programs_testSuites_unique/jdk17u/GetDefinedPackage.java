public class GetDefinedPackage {

    public static void main(String... args) {
        TestClassLoader loader = new TestClassLoader();
        Package pkg = loader.getDefinedPackage(TestClassLoader.PKG_NAME);
        if (pkg == null) {
            throw new RuntimeException("package foo not found");
        }
        try {
            loader.getDefinedPackage(null);
            throw new RuntimeException("NullPointerException not thrown");
        } catch (NullPointerException e) {
        }
    }

    static class TestClassLoader extends ClassLoader {

        public static final String PKG_NAME = "foo";

        public TestClassLoader() {
            super();
            definePackage(PKG_NAME);
        }

        public Package definePackage(String name) {
            return definePackage(name, null, null, null, null, null, null, null);
        }
    }
}
