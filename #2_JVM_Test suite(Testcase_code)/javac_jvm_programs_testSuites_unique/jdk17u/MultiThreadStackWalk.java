import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.lang.StackWalker.StackFrame;
import static java.lang.StackWalker.Option.*;

public class MultiThreadStackWalk {

    static Set<String> infrastructureClasses = new TreeSet<>(Arrays.asList("jdk.internal.reflect.NativeMethodAccessorImpl", "jdk.internal.reflect.DelegatingMethodAccessorImpl", "java.lang.reflect.Method", "com.sun.javatest.regtest.MainWrapper$MainThread", "java.lang.Thread"));

    static final List<Class<?>> streamPipelines = Arrays.asList(classForName("java.util.stream.AbstractPipeline"), classForName("java.util.stream.TerminalOp"));

    static Class<?> classForName(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isStreamPipeline(Class<?> clazz) {
        for (Class<?> c : streamPipelines) {
            if (c.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }

    public static class Env {

        final AtomicLong frameCounter;

        final long checkMarkAt;

        final long max;

        final AtomicBoolean debug;

        final AtomicLong markerCalled;

        final AtomicLong maxReached;

        final Set<String> unexpected;

        public Env(long total, long markAt, AtomicBoolean debug) {
            this.debug = debug;
            frameCounter = new AtomicLong();
            maxReached = new AtomicLong();
            unexpected = Collections.synchronizedSet(new TreeSet<>());
            this.max = total + 2;
            this.checkMarkAt = total - markAt + 1;
            this.markerCalled = new AtomicLong();
        }

        private Env(Env orig, long start) {
            debug = orig.debug;
            frameCounter = new AtomicLong(start);
            maxReached = orig.maxReached;
            unexpected = orig.unexpected;
            max = orig.max;
            checkMarkAt = orig.checkMarkAt;
            markerCalled = orig.markerCalled;
        }

        public void consume(StackFrame sfi) {
            if (frameCounter.get() == 0 && isStreamPipeline(sfi.getDeclaringClass())) {
                return;
            }
            final long count = frameCounter.getAndIncrement();
            final StringBuilder builder = new StringBuilder();
            builder.append("Declaring class[").append(count).append("]: ").append(sfi.getDeclaringClass());
            builder.append('\n');
            builder.append("\t").append(sfi.getClassName()).append(".").append(sfi.toStackTraceElement().getMethodName()).append(sfi.toStackTraceElement().isNativeMethod() ? "(native)" : "(" + sfi.toStackTraceElement().getFileName() + ":" + sfi.toStackTraceElement().getLineNumber() + ")");
            builder.append('\n');
            if (debug.get()) {
                System.out.print("[debug] " + builder.toString());
                builder.setLength(0);
            }
            if (count == max) {
                maxReached.incrementAndGet();
            }
            if (count == checkMarkAt) {
                if (sfi.getDeclaringClass() != MultiThreadStackWalk.Marker.class) {
                    throw new RuntimeException("Expected Marker at " + count + ", found " + sfi.getDeclaringClass());
                }
            } else {
                if (count <= 0 && sfi.getDeclaringClass() != MultiThreadStackWalk.Call.class) {
                    throw new RuntimeException("Expected Call at " + count + ", found " + sfi.getDeclaringClass());
                } else if (count > 0 && count < max && sfi.getDeclaringClass() != MultiThreadStackWalk.Test.class) {
                    throw new RuntimeException("Expected Test at " + count + ", found " + sfi.getDeclaringClass());
                } else if (count == max && sfi.getDeclaringClass() != MultiThreadStackWalk.class) {
                    throw new RuntimeException("Expected MultiThreadStackWalk at " + count + ", found " + sfi.getDeclaringClass());
                } else if (count == max && !sfi.toStackTraceElement().getMethodName().equals("runTest")) {
                    throw new RuntimeException("Expected runTest method at " + count + ", found " + sfi.toStackTraceElement().getMethodName());
                } else if (count == max + 1) {
                    if (sfi.getDeclaringClass() != MultiThreadStackWalk.WalkThread.class) {
                        throw new RuntimeException("Expected MultiThreadStackWalk at " + count + ", found " + sfi.getDeclaringClass());
                    }
                    if (count == max && !sfi.toStackTraceElement().getMethodName().equals("run")) {
                        throw new RuntimeException("Expected main method at " + count + ", found " + sfi.toStackTraceElement().getMethodName());
                    }
                } else if (count > max + 1) {
                    if (!infrastructureClasses.contains(sfi.getDeclaringClass().getName())) {
                        System.err.println("**** WARNING: encountered unexpected infrastructure class at " + count + ": " + sfi.getDeclaringClass().getName());
                        unexpected.add(sfi.getDeclaringClass().getName());
                    }
                }
            }
            if (count == 100) {
                StackWalker.getInstance(RETAIN_CLASS_REFERENCE).forEach(x -> {
                    StackTraceElement st = x.toStackTraceElement();
                    StringBuilder b = new StringBuilder();
                    b.append("*** inner walk: ").append(x.getClassName()).append(st == null ? "- no stack trace element -" : ("." + st.getMethodName() + (st.isNativeMethod() ? "(native)" : "(" + st.getFileName() + ":" + st.getLineNumber() + ")"))).append('\n');
                    if (debug.get()) {
                        System.out.print(b.toString());
                        b.setLength(0);
                    }
                });
            }
        }
    }

    public interface Call {

        enum WalkType {

            WALKSTACK
        }

        default WalkType getWalkType() {
            return WalkType.WALKSTACK;
        }

        default void walk(Env env) {
            WalkType walktype = getWalkType();
            System.out.println("Thread " + Thread.currentThread().getName() + " starting walk with " + walktype);
            switch(walktype) {
                case WALKSTACK:
                    StackWalker.getInstance(RETAIN_CLASS_REFERENCE).forEach(env::consume);
                    break;
                default:
                    throw new InternalError("Unknown walk type: " + walktype);
            }
        }

        default void call(Env env, Call next, int total, int current, int markAt) {
            if (current < total) {
                next.call(env, next, total, current + 1, markAt);
            }
        }
    }

    public static class Marker implements Call {

        final WalkType walkType;

        Marker(WalkType walkType) {
            this.walkType = walkType;
        }

        @Override
        public WalkType getWalkType() {
            return walkType;
        }

        @Override
        public void call(Env env, Call next, int total, int current, int markAt) {
            env.markerCalled.incrementAndGet();
            if (current < total) {
                next.call(env, next, total, current + 1, markAt);
            } else {
                next.walk(env);
            }
        }
    }

    public static class Test implements Call {

        final Marker marker;

        final WalkType walkType;

        final AtomicBoolean debug;

        Test(WalkType walkType) {
            this.walkType = walkType;
            this.marker = new Marker(walkType);
            this.debug = new AtomicBoolean();
        }

        @Override
        public WalkType getWalkType() {
            return walkType;
        }

        @Override
        public void call(Env env, Call next, int total, int current, int markAt) {
            if (current < total) {
                int nexti = current + 1;
                Call nextObj = nexti == markAt ? marker : next;
                nextObj.call(env, next, total, nexti, markAt);
            } else {
                walk(env);
            }
        }
    }

    public static Env runTest(Test test, int total, int markAt) {
        Env env = new Env(total, markAt, test.debug);
        test.call(env, test, total, 0, markAt);
        return env;
    }

    public static void checkTest(Env env, Test test) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + ": Marker called: " + env.markerCalled.get());
        System.out.println(threadName + ": Max reached: " + env.maxReached.get());
        System.out.println(threadName + ": Frames consumed: " + env.frameCounter.get());
        if (env.markerCalled.get() == 0) {
            throw new RuntimeException(Thread.currentThread().getName() + ": Marker was not called.");
        }
        if (env.markerCalled.get() > 1) {
            throw new RuntimeException(Thread.currentThread().getName() + ": Marker was called more than once: " + env.maxReached.get());
        }
        if (!env.unexpected.isEmpty()) {
            System.out.flush();
            System.err.println("Encountered some unexpected infrastructure classes below 'main': " + env.unexpected);
        }
        if (env.maxReached.get() == 0) {
            throw new RuntimeException(Thread.currentThread().getName() + ": max not reached");
        }
        if (env.maxReached.get() > 1) {
            throw new RuntimeException(Thread.currentThread().getName() + ": max was reached more than once: " + env.maxReached.get());
        }
    }

    static class WalkThread extends Thread {

        final static AtomicLong walkersCount = new AtomicLong();

        Throwable failed = null;

        final Test test;

        public WalkThread(Test test) {
            super("WalkThread[" + walkersCount.incrementAndGet() + ", type=" + test.getWalkType() + "]");
            this.test = test;
        }

        public void run() {
            try {
                Env env = runTest(test, 1000, 10);
                checkTest(env, test);
            } catch (Throwable t) {
                failed = t;
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        WalkThread[] threads = new WalkThread[Call.WalkType.values().length * 3];
        Throwable failed = null;
        for (int i = 0; i < threads.length; i++) {
            Test test = new Test(Call.WalkType.values()[i % Call.WalkType.values().length]);
            threads[i] = new WalkThread(test);
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
            if (failed == null)
                failed = threads[i].failed;
            else if (threads[i].failed == null) {
                failed.addSuppressed(threads[i].failed);
            }
        }
        if (failed != null) {
            throw failed;
        }
    }
}
