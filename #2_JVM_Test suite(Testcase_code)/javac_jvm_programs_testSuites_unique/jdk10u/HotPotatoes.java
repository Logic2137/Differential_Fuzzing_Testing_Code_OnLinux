



import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

@SuppressWarnings("unchecked")
public class HotPotatoes {
    private static void realMain(String[] args) throws Throwable {
        testImplementation(Vector.class);
        testImplementation(ArrayList.class);
        testImplementation(PriorityQueue.class);
        testImplementation(PriorityBlockingQueue.class);
    }

    private static void testImplementation(Class<? extends Collection> implClazz)
        throws Throwable
    {
        testPotato(implClazz, Vector.class);
        testPotato(implClazz, CopyOnWriteArrayList.class);

        final Constructor<? extends Collection> constr
            = implClazz.getConstructor(Collection.class);
        final Collection<Object> coll
            = constr.newInstance(Arrays.asList(new String[] {}));
        coll.add(1);
        equal(coll.toString(), "[1]");
    }

    private static void testPotato(Class<? extends Collection> implClazz,
                                   Class<? extends List> argClazz)
        throws Throwable
    {
        try {
            System.out.printf("implClazz=%s, argClazz=%s\n",
                              implClazz.getName(), argClazz.getName());
            final int iterations = 100000;
            final List<Integer> list = (List<Integer>) argClazz.newInstance();
            final Integer one = Integer.valueOf(1);
            final List<Integer> oneElementList = Collections.singletonList(one);
            final Constructor<? extends Collection> constr
                = implClazz.getConstructor(Collection.class);
            final Thread t = new CheckedThread() { public void realRun() {
                for (int i = 0; i < iterations; i++) {
                    list.add(one);
                    list.remove(one);
                }}};
            t.setDaemon(true);
            t.start();

            for (int i = 0; i < iterations; i++) {
                Collection<?> coll = constr.newInstance(list);
                Object[] elts = coll.toArray();
                check(elts.length == 0 ||
                      (elts.length == 1 && elts[0] == one));
            }
        } catch (Throwable t) { unexpected(t); }
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
    private abstract static class CheckedThread extends Thread {
        public abstract void realRun() throws Throwable;
        public void run() {
            try { realRun(); } catch (Throwable t) { unexpected(t); }}}
}
