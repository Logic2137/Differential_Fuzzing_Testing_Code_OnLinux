


public abstract class AsyncCloseTest {

    public abstract String description();

    public abstract AsyncCloseTest go();

    public synchronized boolean hasPassed() {
        return passed;
    }

    protected synchronized AsyncCloseTest passed() {
        if (failureReason() == null) {
            passed = true;
        }
        return this;
    }

    protected synchronized AsyncCloseTest failed(String r) {
        passed = false;
        reason.append(String.format("%n - %s", r));
        return this;
    }

    public synchronized String failureReason() {
        return reason.length() > 0 ? reason.toString() : null;
    }

    protected synchronized void closed() {
        closed = true;
    }

    protected synchronized boolean isClosed() {
        return closed;
    }

    private boolean passed;
    private final StringBuilder reason = new StringBuilder();
    private boolean closed;
}
