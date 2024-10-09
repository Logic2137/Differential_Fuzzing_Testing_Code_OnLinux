import java.util.MissingResourceException;
import java.util.logging.Logger;

public class LoadItUp2 {

    private static final boolean DEBUG = false;

    public Boolean test(String rbName) throws Exception {
        return lookupBundle(rbName);
    }

    private boolean lookupBundle(String rbName) {
        try {
            Logger aLogger = Logger.getLogger("NestedLogger2", rbName);
        } catch (MissingResourceException re) {
            if (DEBUG) {
                System.out.println("As expected, LoadItUp2.lookupBundle() did not find the bundle " + rbName);
            }
            return false;
        }
        System.out.println("FAILED: LoadItUp2.lookupBundle() found the bundle " + rbName + " using a stack search.");
        return true;
    }
}
