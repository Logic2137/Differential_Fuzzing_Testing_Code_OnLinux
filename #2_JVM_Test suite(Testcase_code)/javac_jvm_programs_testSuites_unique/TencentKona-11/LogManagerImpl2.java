
package testgetglobal;

import java.util.logging.LogManager;
import java.util.logging.Logger;


public class LogManagerImpl2 extends LogManager {

    final Logger globalLogger;
    public LogManagerImpl2() {
        globalLogger = Logger.getGlobal();
        System.err.println("Global is: " + globalLogger);
    }

}
