
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;



public class LinkageErrorTest {

    public static class TestHandler extends Handler {

        private volatile boolean closed;
        public TestHandler() {
            INSTANCES.add(this);
        }

        @Override
        public void publish(LogRecord record) {
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
            closed = true;
            try {
                System.out.println(INSTANCES);
            } catch (Throwable t) {
                
            }
            throw new LinkageError();
        }

        @Override
        public String toString() {
            return super.toString() + "{closed=" + closed + '}';
        }

        private static final CopyOnWriteArrayList<Handler> INSTANCES
                = new CopyOnWriteArrayList<>();
    }

    private static final Logger LOGGER = Logger.getLogger("test");
    private static final Logger GLOBAL = Logger.getGlobal();

    public static void main(String[] args) {
        LOGGER.addHandler(new TestHandler());
        LOGGER.addHandler(new TestHandler());
        GLOBAL.addHandler(new TestHandler());
    }
}
