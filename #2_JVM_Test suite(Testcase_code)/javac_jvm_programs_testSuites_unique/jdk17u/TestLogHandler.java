import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class TestLogHandler extends Handler {

    private final String illegal;

    private boolean testFailed;

    public TestLogHandler(String illegal) {
        this.illegal = illegal;
        this.testFailed = false;
    }

    @Override
    public void publish(LogRecord record) {
        String msg = record.getMessage();
        String method = record.getSourceMethodName();
        String className = record.getSourceClassName();
        if (msg.contains(illegal)) {
            testFailed = true;
        }
        if (msg.contains("attribute names=")) {
            System.err.println("LOG: " + className + "." + method + ": " + msg);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }

    public boolean testFailed() {
        return testFailed;
    }
}
