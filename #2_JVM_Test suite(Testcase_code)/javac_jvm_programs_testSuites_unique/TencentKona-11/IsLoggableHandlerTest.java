


import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.MemoryHandler;
import java.util.logging.StreamHandler;
import java.util.stream.Stream;

public class IsLoggableHandlerTest {


    public static void main(String... args) throws IOException {
        String userDir = System.getProperty("user.dir", ".");
        File logfile = new File(userDir, "IsLoggableHandlerTest_" + UUID.randomUUID() + ".log");
        try {
            System.out.println("Dummy logfile: " + logfile.getAbsolutePath());
            Handler h = new CustomHandler();
            testIsLoggable(h);
            testIsLoggable(new MemoryHandler(h, 1, Level.ALL));
            testIsLoggable(new StreamHandler(System.out, new java.util.logging.SimpleFormatter()));
            testIsLoggable(new FileHandler(logfile.getAbsolutePath()));
            testIsLoggable(new ConsoleHandler());
        } finally {
            if (logfile.canRead()) {
                try {
                    System.out.println("Deleting dummy logfile: " + logfile.getAbsolutePath());
                    logfile.delete();
                } catch (Throwable t) {
                    System.out.println("Warning: failed to delete dummy logfile: " + t);
                    t.printStackTrace();
                }
            }
        }
    }

    public static void testIsLoggable(Handler h) {
        System.out.println("Testing " + h.getClass().getName());
        
        if (h.isLoggable(null)) {
            throw new AssertionError(h.getClass().getName()
                    + ": null record should not be loggable");
        }
        h.setLevel(Level.ALL);
        
        if (h.isLoggable(null)) {
            throw new AssertionError(h.getClass().getName()
                    + ": null record should not be loggable");
        }
        
        h.publish(null);
    }

    public static final class CustomHandler extends Handler {
        @Override
        public void publish(LogRecord record) { }
        @Override
        public void flush() { }
        @Override
        public void close() throws SecurityException { }
    }


}
