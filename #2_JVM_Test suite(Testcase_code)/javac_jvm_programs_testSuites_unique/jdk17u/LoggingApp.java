import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class LoggingApp {

    private static final Lock lock = new ReentrantLock();

    private static volatile Logger logger;

    public static Logger logger() {
        Logger logger = LoggingApp.logger;
        if (logger != null)
            return logger;
        lock.lock();
        try {
            if ((logger = LoggingApp.logger) != null)
                return logger;
            LoggingApp.logger = logger = Logger.getLogger("com.example", "MyBundle");
        } finally {
            lock.unlock();
        }
        return logger;
    }
}
