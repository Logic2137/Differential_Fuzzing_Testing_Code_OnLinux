import java.util.ArrayList;
import java.util.Collections;
import java.util.AbstractList;
import java.util.List;
import java.util.Objects;

public class NCopies {

    static volatile int passed = 0, failed = 0;

    static void fail(String msg) {
        failed++;
        new AssertionError(msg).printStackTrace();
    }

    static void pass() {
        passed++;
    }

    static void unexpected(Throwable t) {
        failed++;
        t.printStackTrace();
    }

    static void check(boolean condition, String msg) {
        if (condition)
            passed++;
        else
            fail(msg);
    }

    static void check(boolean condition) {
        check(condition, "Assertion failure");
    }

    private static void checkEmpty(List<String> x) {
        check(x.isEmpty());
        check(x.size() == 0);
        check(x.indexOf("foo") == -1);
        check(x.lastIndexOf("foo") == -1);
        check(x.toArray().length == 0);
        check(x.toArray().getClass() == Object[].class);
    }

    private static void checkFoos(List<String> x) {
        check(!x.isEmpty());
        check(x.indexOf(new String("foo")) == 0);
        check(x.lastIndexOf(new String("foo")) == x.size() - 1);
        check(x.toArray().length == x.size());
        check(x.toArray().getClass() == Object[].class);
        String[] sa = x.toArray(new String[x.size()]);
        check(sa.getClass() == String[].class);
        check(sa[0].equals("foo"));
        check(sa[sa.length - 1].equals("foo"));
        check(x.get(x.size() / 2).equals("foo"));
        checkEmpty(x.subList(x.size() / 2, x.size() / 2));
    }

    private static <T> List<T> referenceNCopies(int n, T o) {
        return new AbstractList<>() {

            public int size() {
                return n;
            }

            public T get(int index) {
                Objects.checkIndex(index, n);
                return o;
            }
        };
    }

    private static void checkHashCode() {
        int[] sizes = { 0, 1, 2, 3, 5, 10, 31, 32, 100, 1000 };
        String[] elements = { null, "non-null" };
        for (int size : sizes) {
            for (String element : elements) {
                int expectedHashCode = referenceNCopies(size, element).hashCode();
                int actualHashCode = Collections.nCopies(size, element).hashCode();
                check(expectedHashCode == actualHashCode, "Collections.nCopies(" + size + ", " + element + ").hashCode()");
            }
        }
    }

    private static void checkEquals() {
        int[][] sizePairs = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 2, 1 } };
        String[] elements = { null, "non-null" };
        for (int[] pair : sizePairs) {
            for (String element : elements) {
                boolean equal = pair[0] == pair[1];
                String msg = "[" + pair[0] + ", " + element + "] <=> [" + pair[1] + ", " + element + "]";
                check(equal == Collections.nCopies(pair[0], element).equals(Collections.nCopies(pair[1], element)), msg);
                check(equal == Collections.nCopies(pair[0], element).equals(referenceNCopies(pair[1], element)), msg);
                check(equal == referenceNCopies(pair[0], element).equals(Collections.nCopies(pair[1], element)), msg);
            }
        }
        List<String> nulls = Collections.nCopies(10, null);
        List<String> nonNulls = Collections.nCopies(10, "non-null");
        List<String> nullsButOne = new ArrayList<>(nulls);
        nullsButOne.set(9, "non-null");
        List<String> nonNullsButOne = new ArrayList<>(nonNulls);
        nonNullsButOne.set(9, null);
        check(!nulls.equals(nonNulls));
        check(!nulls.equals(nullsButOne));
        check(!nulls.equals(nonNullsButOne));
        check(!nonNulls.equals(nonNullsButOne));
        check(Collections.nCopies(0, null).equals(Collections.nCopies(0, "non-null")));
    }

    public static void main(String[] args) {
        try {
            List<String> empty = Collections.nCopies(0, "foo");
            checkEmpty(empty);
            checkEmpty(empty.subList(0, 0));
            List<String> foos = Collections.nCopies(42, "foo");
            check(foos.size() == 42);
            checkFoos(foos.subList(foos.size() / 2, foos.size() - 1));
            checkHashCode();
            checkEquals();
        } catch (Throwable t) {
            unexpected(t);
        }
        System.out.printf("%nPassed = %d, failed = %d%n%n", passed, failed);
        if (failed > 0)
            throw new Error("Some tests failed");
    }
}
