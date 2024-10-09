public abstract class AsyncCloseTest {

    public abstract String description();

    public abstract AsyncCloseTest go();

    public synchronized boolean hasPassed() {
        return passed;
    }

    protected synchronized AsyncCloseTest passed() {
        if (reason == null)
            passed = true;
        return this;
    }

    protected synchronized AsyncCloseTest failed(String r) {
        passed = false;
        reason = r;
        return this;
    }

    public synchronized String failureReason() {
        return reason;
    }

    protected synchronized void closed() {
        closed = true;
    }

    protected synchronized boolean isClosed() {
        return closed;
    }

    private boolean passed;

    private String reason;

    private boolean closed;
}
