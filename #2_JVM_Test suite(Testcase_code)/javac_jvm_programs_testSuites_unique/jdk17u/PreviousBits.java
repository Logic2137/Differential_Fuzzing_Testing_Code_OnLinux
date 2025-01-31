import java.util.*;

public class PreviousBits {

    void testHashCode(final BitSet s) {
        long h = 1234;
        long[] words = s.toLongArray();
        for (int i = words.length; --i >= 0; ) h ^= words[i] * (i + 1);
        equal((int) ((h >> 32) ^ h), s.hashCode());
    }

    void testOutOfBounds(final BitSet s) {
        THROWS(IndexOutOfBoundsException.class, new F() {

            void f() {
                s.previousSetBit(-2);
            }
        }, new F() {

            void f() {
                s.previousClearBit(-2);
            }
        }, new F() {

            void f() {
                s.previousSetBit(Integer.MIN_VALUE);
            }
        }, new F() {

            void f() {
                s.previousClearBit(Integer.MIN_VALUE);
            }
        }, new F() {

            void f() {
                s.nextSetBit(-1);
            }
        }, new F() {

            void f() {
                s.nextClearBit(-1);
            }
        }, new F() {

            void f() {
                s.nextSetBit(Integer.MIN_VALUE);
            }
        }, new F() {

            void f() {
                s.nextClearBit(Integer.MIN_VALUE);
            }
        });
    }

    void test(String[] args) throws Throwable {
        final BitSet s = new BitSet();
        testOutOfBounds(s);
        testHashCode(s);
        for (int i = -1; i < 93; ) {
            equal(-1, s.previousSetBit(i));
            equal(i, s.previousClearBit(i));
            i++;
            equal(-1, s.nextSetBit(i));
            equal(i, s.nextClearBit(i));
        }
        for (int j = 0; j < 161; j++) {
            s.clear();
            s.set(j);
            testOutOfBounds(s);
            testHashCode(s);
            for (int i = -1; i < j; i++) {
                equal(-1, s.previousSetBit(i));
                equal(i, s.previousClearBit(i));
                if (i >= 0) {
                    equal(j, s.nextSetBit(i));
                    equal(i, s.nextClearBit(i));
                }
            }
            equal(j, s.previousSetBit(j));
            equal(j - 1, s.previousClearBit(j));
            equal(j, s.nextSetBit(j));
            equal(j + 1, s.nextClearBit(j));
            for (int i = j + 1; i < j + 100; i++) {
                equal(j, s.previousSetBit(i));
                equal(i, s.previousClearBit(i));
                equal(-1, s.nextSetBit(i));
                equal(i, s.nextClearBit(i));
            }
        }
        s.clear();
        for (int i = 0; i <= 128; i += 2) s.set(i);
        testHashCode(s);
        for (int i = 1; i <= 128; i++) {
            equal(s.previousSetBit(i), ((i & 1) == 0) ? i : i - 1);
            equal(s.previousClearBit(i), ((i & 1) == 0) ? i - 1 : i);
        }
        for (int i = 1; i <= 128; i += 2) s.set(i);
        testHashCode(s);
        for (int i = 1; i <= 128; i++) {
            equal(s.previousSetBit(i), i);
            equal(s.previousClearBit(i), -1);
        }
        Random rnd = new Random();
        s.clear();
        for (int i = 0; i < 10; i++) s.set(rnd.nextInt(1066));
        List<Integer> down = new ArrayList<Integer>();
        for (int i = s.length(); (i = s.previousSetBit(i - 1)) >= 0; ) down.add(i);
        List<Integer> up = new ArrayList<Integer>();
        for (int i = s.nextSetBit(0); i >= 0; i = s.nextSetBit(i + 1)) up.add(i);
        Collections.reverse(up);
        equal(up, down);
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
        new PreviousBits().instanceMain(args);
    }

    void instanceMain(String[] args) throws Throwable {
        try {
            test(args);
        } catch (Throwable t) {
            unexpected(t);
        }
        System.out.printf("%nPassed = %d, failed = %d%n%n", passed, failed);
        if (failed > 0)
            throw new AssertionError("Some tests failed");
    }

    abstract class F {

        abstract void f() throws Throwable;
    }

    void THROWS(Class<? extends Throwable> k, F... fs) {
        for (F f : fs) try {
            f.f();
            fail("Expected " + k.getName() + " not thrown");
        } catch (Throwable t) {
            if (k.isAssignableFrom(t.getClass()))
                pass();
            else
                unexpected(t);
        }
    }
}
