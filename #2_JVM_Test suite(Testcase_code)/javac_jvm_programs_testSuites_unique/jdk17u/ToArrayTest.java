import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Iterator;

public class ToArrayTest {

    static class TestCollection<E> extends AbstractCollection<E> {

        private final E[] elements;

        private int[] sizes;

        private int nextSize;

        public TestCollection(E[] elements) {
            this.elements = elements;
            setSizeSequence(new int[] { elements.length });
        }

        void setSizeSequence(int... sizes) {
            this.sizes = sizes;
            nextSize = 0;
        }

        @Override
        public int size() {
            return sizes[nextSize == sizes.length - 1 ? nextSize : nextSize++];
        }

        @Override
        public Iterator<E> iterator() {
            return new Iterator<E>() {

                int pos = 0;

                public boolean hasNext() {
                    return pos < sizes[nextSize];
                }

                public E next() {
                    return elements[pos++];
                }

                public void remove() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            };
        }
    }

    static final Object[] OBJECTS = { new Object(), new Object(), new Object() };

    static final TestCollection<?> CANDIDATE = new TestCollection<Object>(OBJECTS);

    static final int CAP = OBJECTS.length;

    static final int LAST = CAP - 1;

    Object[] a;

    Object[] res;

    int last() {
        return a.length - 1;
    }

    protected void test() throws Throwable {
        res = new TestCollection<>(new Object[] { "1", "2" }).toArray(new String[0]);
        check(res instanceof String[]);
        check(res.length == 2);
        check(res[1] == "2");
        try {
            res = CANDIDATE.toArray(new String[CAP]);
            check(false);
        } catch (Throwable t) {
            check(t instanceof ArrayStoreException);
        }
        a = new Object[CAP - 1];
        res = CANDIDATE.toArray(a);
        check(res != a);
        check(res[LAST] != null);
        a = new Object[CAP];
        res = CANDIDATE.toArray(a);
        check(res == a);
        check(res[last()] != null);
        a = new Object[CAP + 1];
        res = CANDIDATE.toArray(a);
        check(res == a);
        check(res[last()] == null);
        a = new Object[CAP - 2];
        CANDIDATE.setSizeSequence(CAP, CAP - 1);
        res = CANDIDATE.toArray(a);
        check(res != a);
        check(res.length == CAP - 1);
        check(res[LAST - 1] != null);
        a = Arrays.copyOf(OBJECTS, CAP);
        CANDIDATE.setSizeSequence(CAP, CAP - 1);
        res = CANDIDATE.toArray(a);
        check(res == a);
        check(res[last()] == null);
        a = new Object[CAP - 1];
        CANDIDATE.setSizeSequence(CAP - 1, CAP);
        res = CANDIDATE.toArray(a);
        check(res != a);
        check(res[LAST] != null);
        a = new Object[CAP - 1];
        CANDIDATE.setSizeSequence(CAP - 2, CAP - 1);
        res = CANDIDATE.toArray(a);
        check(res == a);
        check(res[last()] != null);
        a = Arrays.copyOf(OBJECTS, CAP);
        CANDIDATE.setSizeSequence(CAP - 2, CAP - 1);
        res = CANDIDATE.toArray(a);
        check(res == a);
        check(res[last()] == null);
        test_7121314();
    }

    protected void test_7121314() throws Throwable {
        a = new Object[CAP - 1];
        CANDIDATE.setSizeSequence(CAP, CAP - 1);
        res = CANDIDATE.toArray(a);
        check(res == a);
        check(res[last()] != null);
        a = Arrays.copyOf(OBJECTS, CAP - 1);
        CANDIDATE.setSizeSequence(CAP, CAP - 2);
        res = CANDIDATE.toArray(a);
        check(res == a);
        check(res[last()] == null);
    }

    public static void main(String[] args) throws Throwable {
        ToArrayTest testcase = new ToArrayTest();
        try {
            testcase.test();
        } catch (Throwable t) {
            unexpected(t);
        }
        System.out.printf("%nPassed = %d, failed = %d%n%n", passed, failed);
        if (failed > 0)
            throw new Exception("Some tests failed");
    }

    static volatile int passed = 0, failed = 0;

    static void pass() {
        passed++;
    }

    static void fail() {
        failed++;
        Thread.dumpStack();
    }

    static void fail(String msg) {
        System.out.println(msg);
        fail();
    }

    static void unexpected(Throwable t) {
        failed++;
        t.printStackTrace();
    }

    static void check(boolean cond) {
        if (cond)
            pass();
        else
            fail();
    }

    static void equal(Object x, Object y) {
        if (x == null ? y == null : x.equals(y))
            pass();
        else {
            System.out.println(x + " not equal to " + y);
            fail();
        }
    }
}
