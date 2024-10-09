
package testgetglobal;

import java.util.logging.LogManager;
import java.util.logging.Logger;


public class LogManagerImpl1 extends LogManager {

    static final Logger global;
    static {
        global = Logger.getGlobal();
        System.err.println("Global is: " + global);
    }

}
