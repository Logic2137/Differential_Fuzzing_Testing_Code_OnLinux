



public class HugeCapacity {
    private static int failures = 0;

    public static void main(String[] args) {
        testLatin1();
        testUtf16();
        testHugeInitialString();
        testHugeInitialCharSequence();
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

    private static void testHugeInitialString() {
        try {
            String str = "Z".repeat(Integer.MAX_VALUE - 8);
            StringBuilder sb = new StringBuilder(str);
        } catch (OutOfMemoryError ignore) {
        } catch (Throwable unexpected) {
            unexpected.printStackTrace();
            failures++;
        }
    }

    private static void testHugeInitialCharSequence() {
        try {
            CharSequence seq = new MyHugeCharSeq();
            StringBuilder sb = new StringBuilder(seq);
        } catch (OutOfMemoryError ignore) {
        } catch (Throwable unexpected) {
            unexpected.printStackTrace();
            failures++;
        }
    }

    private static class MyHugeCharSeq implements CharSequence {
        public char charAt(int i) {
            throw new UnsupportedOperationException();
        }
        public int length() { return Integer.MAX_VALUE; }
        public CharSequence subSequence(int st, int e) {
            throw new UnsupportedOperationException();
        }
        public String toString() { return ""; }
    }
}
