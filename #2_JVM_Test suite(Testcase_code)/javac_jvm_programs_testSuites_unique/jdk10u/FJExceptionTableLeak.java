





import static java.util.concurrent.TimeUnit.SECONDS;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.function.BooleanSupplier;

public class FJExceptionTableLeak {
    static class FailingTaskException extends RuntimeException {}
    static class FailingTask extends RecursiveAction {
        public void compute() { throw new FailingTaskException(); }
    }

    static int bucketsInuse(Object[] exceptionTable) {
        int count = 0;
        for (Object x : exceptionTable)
            if (x != null) count++;
        return count;
    }

    public static void main(String[] args) throws Exception {
        final ForkJoinPool pool = new ForkJoinPool(4);
        final Field exceptionTableField =
            ForkJoinTask.class.getDeclaredField("exceptionTable");
        exceptionTableField.setAccessible(true);
        final Object[] exceptionTable = (Object[]) exceptionTableField.get(null);

        if (bucketsInuse(exceptionTable) != 0) throw new AssertionError();

        final ArrayList<FailingTask> tasks = new ArrayList<>();

        
        
        do {
            for (int i = 0; i < exceptionTable.length; i++) {
                FailingTask task = new FailingTask();
                pool.execute(task);
                tasks.add(task); 
            }
            for (FailingTask task : tasks) {
                try {
                    task.join();
                    throw new AssertionError("should throw");
                } catch (FailingTaskException success) {}
            }
        } while (bucketsInuse(exceptionTable) < exceptionTable.length * 3 / 4);

        
        
        FailingTask lastTask = tasks.get(0);

        
        tasks.clear();

        BooleanSupplier exceptionTableIsClean = () -> {
            try {
                lastTask.join();
                throw new AssertionError("should throw");
            } catch (FailingTaskException expected) {}
            int count = bucketsInuse(exceptionTable);
            if (count == 0)
                throw new AssertionError("expected to find last task");
            return count == 1;
        };
        gcAwait(exceptionTableIsClean);
    }

    

    
    static void forceFullGc() {
        CountDownLatch finalizeDone = new CountDownLatch(1);
        WeakReference<?> ref = new WeakReference<Object>(new Object() {
            protected void finalize() { finalizeDone.countDown(); }});
        try {
            for (int i = 0; i < 10; i++) {
                System.gc();
                if (finalizeDone.await(1L, SECONDS) && ref.get() == null) {
                    System.runFinalization(); 
                    return;
                }
            }
        } catch (InterruptedException unexpected) {
            throw new AssertionError("unexpected InterruptedException");
        }
        throw new AssertionError("failed to do a \"full\" gc");
    }

    static void gcAwait(BooleanSupplier s) {
        for (int i = 0; i < 10; i++) {
            if (s.getAsBoolean())
                return;
            forceFullGc();
        }
        throw new AssertionError("failed to satisfy condition");
    }
}
