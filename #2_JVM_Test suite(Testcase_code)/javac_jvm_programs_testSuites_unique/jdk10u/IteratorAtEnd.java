



import java.util.*;
import java.util.concurrent.*;

@SuppressWarnings("unchecked")
public class IteratorAtEnd {
    private static final int SIZE = 6;

    static void realMain(String[] args) throws Throwable {
        testCollection(new ArrayList());
        testCollection(new Vector());
        testCollection(new LinkedList());
        testCollection(new ArrayDeque());
        testCollection(new TreeSet());
        testCollection(new CopyOnWriteArrayList());
        testCollection(new CopyOnWriteArraySet());
        testCollection(new ConcurrentSkipListSet());

        testCollection(new PriorityQueue());
        testCollection(new LinkedBlockingQueue());
        testCollection(new ArrayBlockingQueue(100));
        testCollection(new ConcurrentLinkedDeque());
        testCollection(new ConcurrentLinkedQueue());
        testCollection(new LinkedTransferQueue());

        testMap(new HashMap());
        testMap(new Hashtable());
        testMap(new LinkedHashMap());
        testMap(new WeakHashMap());
        testMap(new IdentityHashMap());
        testMap(new ConcurrentHashMap());
        testMap(new ConcurrentSkipListMap());
        testMap(new TreeMap());
    }

    static void testCollection(Collection c) {
        try {
            for (int i = 0; i < SIZE; i++)
                c.add(i);
            test(c);
        } catch (Throwable t) { unexpected(t); }
    }

    static void testMap(Map m) {
        try {
            for (int i = 0; i < 3*SIZE; i++)
                m.put(i, i);
            test(m.values());
            test(m.keySet());
            test(m.entrySet());
        } catch (Throwable t) { unexpected(t); }
    }

    static void test(Collection c) {
        try {
            final Iterator it = c.iterator();
            THROWS(NoSuchElementException.class,
                   () -> { while (true) it.next(); });
            try { it.remove(); }
            catch (UnsupportedOperationException exc) { return; }
            pass();
        } catch (Throwable t) { unexpected(t); }

        if (c instanceof List) {
            final List list = (List) c;
            try {
                final ListIterator it = list.listIterator(0);
                it.next();
                final Object x = it.previous();
                THROWS(NoSuchElementException.class, () -> it.previous());
                try { it.remove(); }
                catch (UnsupportedOperationException exc) { return; }
                pass();
                check(! list.get(0).equals(x));
            } catch (Throwable t) { unexpected(t); }

            try {
                final ListIterator it = list.listIterator(list.size());
                it.previous();
                final Object x = it.next();
                THROWS(NoSuchElementException.class, () -> it.next());
                try { it.remove(); }
                catch (UnsupportedOperationException exc) { return; }
                pass();
                check(! list.get(list.size()-1).equals(x));
            } catch (Throwable t) { unexpected(t); }
        }
    }

    
    static volatile int passed = 0, failed = 0;
    static void pass() {passed++;}
    static void fail() {failed++; Thread.dumpStack();}
    static void fail(String msg) {System.out.println(msg); fail();}
    static void unexpected(Throwable t) {failed++; t.printStackTrace();}
    static void check(boolean cond) {if (cond) pass(); else fail();}
    static void equal(Object x, Object y) {
        if (x == null ? y == null : x.equals(y)) pass();
        else fail(x + " not equal to " + y);}
    public static void main(String[] args) throws Throwable {
        try {realMain(args);} catch (Throwable t) {unexpected(t);}
        System.out.printf("%nPassed = %d, failed = %d%n%n", passed, failed);
        if (failed > 0) throw new AssertionError("Some tests failed");}
    interface Fun {void f() throws Throwable;}
    static void THROWS(Class<? extends Throwable> k, Fun... fs) {
        for (Fun f : fs)
            try { f.f(); fail("Expected " + k.getName() + " not thrown"); }
            catch (Throwable t) {
                if (k.isAssignableFrom(t.getClass())) pass();
                else unexpected(t);}}
}
