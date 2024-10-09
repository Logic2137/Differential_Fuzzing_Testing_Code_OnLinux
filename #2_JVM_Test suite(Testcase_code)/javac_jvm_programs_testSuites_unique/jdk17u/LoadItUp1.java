import java.util.logging.Logger;

public class LoadItUp1 {

    public Logger getAnonymousLogger(String rbName) throws Exception {
        return Logger.getAnonymousLogger(rbName);
    }

    public Logger getLogger(String loggerName) {
        return Logger.getLogger(loggerName);
    }

    public Logger getLogger(String loggerName, String bundleName) {
        return Logger.getLogger(loggerName, bundleName);
    }
}
