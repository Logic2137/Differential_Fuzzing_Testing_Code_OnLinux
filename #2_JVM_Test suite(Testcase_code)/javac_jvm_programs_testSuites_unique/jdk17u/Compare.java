import java.util.Random;

public class Compare {

    final Random rnd = new Random();

    boolean toBoolean(long x) {
        return x > 0;
    }

    void compareAll(long x, long y) {
        check(Double.compare(x, y) == Double.valueOf(x).compareTo(Double.valueOf(y)));
        check(Float.compare(x, y) == Float.valueOf(x).compareTo(Float.valueOf(y)));
        check(Long.compare(x, y) == Long.valueOf(x).compareTo(Long.valueOf(y)));
        check(Integer.compare((int) x, (int) y) == Integer.valueOf((int) x).compareTo(Integer.valueOf((int) y)));
        check(Short.compare((short) x, (short) y) == Short.valueOf((short) x).compareTo(Short.valueOf((short) y)));
        check(Character.compare((char) x, (char) y) == Character.valueOf((char) x).compareTo(Character.valueOf((char) y)));
        check(Byte.compare((byte) x, (byte) y) == Byte.valueOf((byte) x).compareTo(Byte.valueOf((byte) y)));
        check(Boolean.compare(toBoolean(x), toBoolean(y)) == Boolean.valueOf(toBoolean(x)).compareTo(Boolean.valueOf(toBoolean(y))));
        check(Double.compare(x, y) == -Double.compare(y, x));
        check(Float.compare(x, y) == -Float.compare(y, x));
        check(Long.compare(x, y) == -Long.compare(y, x));
        check(Integer.compare((int) x, (int) y) == -Integer.compare((int) y, (int) x));
        check(Short.compare((short) x, (short) y) == -Short.compare((short) y, (short) x));
        check(Character.compare((char) x, (char) y) == -Character.compare((char) y, (char) x));
        check(Byte.compare((byte) x, (byte) y) == -Byte.compare((byte) y, (byte) x));
        equal(Long.compare(x, y), x < y ? -1 : x > y ? 1 : 0);
        {
            int a = (int) x, b = (int) y;
            equal(Integer.compare(a, b), a < b ? -1 : a > b ? 1 : 0);
        }
        {
            short a = (short) x, b = (short) y;
            equal(Short.compare(a, b), a - b);
        }
        {
            char a = (char) x, b = (char) y;
            equal(Character.compare(a, b), a - b);
        }
        {
            byte a = (byte) x, b = (byte) y;
            equal(Byte.compare(a, b), a - b);
        }
        {
            boolean a = toBoolean(x), b = toBoolean(y);
            equal(Boolean.compare(a, b), a == b ? 0 : a ? 1 : -1);
        }
    }

    void test(String[] args) throws Exception {
        long[] longs = { Long.MIN_VALUE, Integer.MIN_VALUE, Short.MIN_VALUE, Character.MIN_VALUE, Byte.MIN_VALUE, -1, 0, 1, Byte.MAX_VALUE, Character.MAX_VALUE, Short.MAX_VALUE, Integer.MAX_VALUE, Long.MAX_VALUE, rnd.nextLong(), rnd.nextInt() };
        for (long x : longs) {
            for (long y : longs) {
                compareAll(x, y);
            }
        }
    }

    volatile int passed = 0, failed = 0;

    void pass() {
        passed++;
    }

    void fail() {
        failed++;
        Thread.dumpStack();
    }

    void fail(String msg) {
        System.err.println(msg);
        fail();
    }

    void unexpected(Throwable t) {
        failed++;
        t.printStackTrace();
    }

    void check(boolean cond) {
        if (cond)
            pass();
        else
            fail();
    }

    void equal(Object x, Object y) {
        if (x == null ? y == null : x.equals(y))
            pass();
        else
            fail(x + " not equal to " + y);
    }

    public static void main(String[] args) throws Throwable {
        new Compare().instanceMain(args);
    }

    public void instanceMain(String[] args) throws Throwable {
        try {
            test(args);
        } catch (Throwable t) {
            unexpected(t);
        }
        System.out.printf("%nPassed = %d, failed = %d%n%n", passed, failed);
        if (failed > 0)
            throw new AssertionError("Some tests failed");
    }
}
