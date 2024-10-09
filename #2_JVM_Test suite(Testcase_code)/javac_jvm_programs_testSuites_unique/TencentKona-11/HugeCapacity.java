public class HugeCapacity {

    private static int failures = 0;

    public static void main(String[] args) {
        testLatin1();
        testUtf16();
        if (failures > 0) {
            throw new RuntimeException(failures + " tests failed");
        }
    }

    private static void testLatin1() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.ensureCapacity(Integer.MAX_VALUE / 2);
            sb.ensureCapacity(Integer.MAX_VALUE / 2 + 1);
        } catch (OutOfMemoryError oom) {
            oom.printStackTrace();
            failures++;
        }
    }

    private static void testUtf16() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append('\u042b');
            sb.ensureCapacity(Integer.MAX_VALUE / 4);
            sb.ensureCapacity(Integer.MAX_VALUE / 4 + 1);
        } catch (OutOfMemoryError oom) {
            oom.printStackTrace();
            failures++;
        }
    }
}
