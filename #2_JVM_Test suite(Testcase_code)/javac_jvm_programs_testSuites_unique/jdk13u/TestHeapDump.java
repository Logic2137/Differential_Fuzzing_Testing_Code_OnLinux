public class TestHeapDump {

    private static final int NUM_ITER = 10000;

    private static final int ARRAY_SIZE = 1000;

    private static final int EXPECTED_OBJECTS = ARRAY_SIZE + 1 + 1;

    static {
        try {
            System.loadLibrary("TestHeapDump");
        } catch (UnsatisfiedLinkError ule) {
            System.err.println("Could not load TestHeapDump library");
            System.err.println("java.library.path: " + System.getProperty("java.library.path"));
            throw ule;
        }
    }

    native static int heapdump(Class<?> filterClass);

    public static void main(String[] args) {
        new TestHeapDump().run();
    }

    static Object root = new TestObject();

    TestObject[] array;

    public void run() {
        array = new TestObject[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = new TestObject();
        }
        TestObject localRoot = new TestObject();
        for (int i = 0; i < NUM_ITER; i++) {
            int numObjs = heapdump(TestObject.class);
            if (numObjs != EXPECTED_OBJECTS) {
                throw new RuntimeException("Expected " + EXPECTED_OBJECTS + " objects, but got " + numObjs);
            }
        }
    }

    public static class TestObject {
    }
}
