
package jdk.test.failurehandler;

public final class Stopwatch {

    protected boolean isResultAvailable;

    protected boolean isRunning;

    private long startTimeNs;

    private long stopTimeNs;

    public Stopwatch() {
        isResultAvailable = false;
    }

    public void start() {
        startTimeNs = System.nanoTime();
        isRunning = true;
    }

    public void stop() {
        if (!isRunning) {
            throw new IllegalStateException(" hasn't been started");
        }
        stopTimeNs = System.nanoTime();
        isRunning = false;
        isResultAvailable = true;
    }

    public long getElapsedTimeNs() {
        if (isRunning) {
            throw new IllegalStateException("hasn't been stopped");
        }
        if (!isResultAvailable) {
            throw new IllegalStateException("was not run");
        }
        return stopTimeNs - startTimeNs;
    }
}
