
package testgetglobal;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogManagerImpl3 extends LogManager {

    static final Logger global;

    static {
        Logger g = null;
        try {
            g = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
            throw new Error("Should not have reached here");
        } catch (Exception x) {
            System.err.println("Got expected exception - you cannot call" + " Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)" + " in LogManager subclass static initializer: " + x);
            x.printStackTrace();
        }
        if (g == null) {
            g = Logger.getGlobal();
        }
        global = g;
        System.err.println("Global is: " + global);
    }
}
