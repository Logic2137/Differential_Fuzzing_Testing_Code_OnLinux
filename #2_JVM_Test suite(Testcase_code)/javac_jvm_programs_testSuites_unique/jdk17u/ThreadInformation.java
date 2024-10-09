
package MyPackage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class ThreadInformation {

    private Thread thread;

    private Allocator allocator;

    public ThreadInformation(Thread thread, Allocator allocator) {
        this.thread = thread;
        this.allocator = allocator;
    }

    public void waitForJobDone() {
        allocator.waitForJobDone();
    }

    public void stop() {
        try {
            allocator.stopRun();
            thread.join();
            if (!allocator.endedNormally()) {
                throw new RuntimeException("Thread did not end normally...");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread got interrupted...");
        }
    }

    private void start() {
        allocator.start();
    }

    public static void startThreads(List<ThreadInformation> threadList) {
        for (ThreadInformation info : threadList) {
            info.start();
        }
    }

    public static void stopThreads(List<ThreadInformation> threadList) {
        for (ThreadInformation info : threadList) {
            info.stop();
        }
    }

    public Thread getThread() {
        return thread;
    }

    public static void waitForThreads(List<ThreadInformation> threadList) {
        System.err.println("Waiting for threads to be done");
        for (ThreadInformation info : threadList) {
            info.waitForJobDone();
        }
    }

    public static List<ThreadInformation> createThreadList(int numThreads) {
        List<ThreadInformation> threadList = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            Allocator allocator = new Allocator(i);
            Thread thread = new Thread(allocator, "Allocator" + i);
            ThreadInformation info = new ThreadInformation(thread, allocator);
            threadList.add(info);
            thread.start();
        }
        return threadList;
    }
}

class Allocator implements Runnable {

    private int depth;

    private List<int[]> currentList;

    private BlockingQueue<Object> jobCanStart;

    private BlockingQueue<Object> jobDone;

    private BlockingQueue<Object> jobCanStop;

    private boolean failed;

    public Allocator(int depth) {
        this.jobCanStart = new LinkedBlockingQueue<>();
        this.jobDone = new LinkedBlockingQueue<>();
        this.jobCanStop = new LinkedBlockingQueue<>();
        this.depth = depth;
    }

    public boolean endedNormally() {
        return !failed;
    }

    private void helper() {
        List<int[]> newList = new ArrayList<>();
        int iterations = (1 << 20) / 24;
        for (int i = 0; i < iterations; i++) {
            int[] newTmp = new int[5];
            newList.add(newTmp);
        }
        currentList = newList;
    }

    private void recursiveWrapper(int depth) {
        if (depth > 0) {
            recursiveWrapper(depth - 1);
            return;
        }
        helper();
    }

    public void stopRun() throws InterruptedException {
        jobCanStop.put(new Object());
    }

    public void run() {
        String name = Thread.currentThread().getName();
        System.err.println("Going to run: " + name);
        waitForStart();
        System.err.println("Running: " + name);
        for (int j = 0; j < 100; j++) {
            recursiveWrapper(depth);
        }
        try {
            jobDone.put(new Object());
            System.err.println("Waiting for main: " + name);
            jobCanStop.take();
            System.err.println("Waited for main: " + name);
        } catch (InterruptedException e) {
            failed = true;
        }
    }

    public void waitForJobDone() {
        try {
            jobDone.take();
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread got interrupted...");
        }
    }

    public void waitForStart() {
        try {
            jobCanStart.take();
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread got interrupted...");
        }
    }

    public void start() {
        try {
            jobCanStart.put(new Object());
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread got interrupted...");
        }
    }
}
