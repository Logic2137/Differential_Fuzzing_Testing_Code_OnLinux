
package testgetglobal;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class BadLogManagerImpl extends LogManager {

    final Logger globalLogger;

    public BadLogManagerImpl() {
        globalLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        System.err.println("Global is: " + globalLogger);
        throw new Error("Should not have reached here");
    }
}
