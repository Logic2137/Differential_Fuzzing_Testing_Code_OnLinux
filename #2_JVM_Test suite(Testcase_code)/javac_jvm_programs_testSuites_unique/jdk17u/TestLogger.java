import java.util.logging.Logger;
import java.util.logging.Level;

public class TestLogger {

    final Logger logger;

    final String className;

    static String getClassName(Class clazz) {
        if (clazz == null)
            return null;
        if (clazz.isArray())
            return getClassName(clazz.getComponentType()) + "[]";
        final String fullname = clazz.getName();
        final int lastpoint = fullname.lastIndexOf('.');
        final int len = fullname.length();
        if ((lastpoint < 0) || (lastpoint >= len))
            return fullname;
        else
            return fullname.substring(lastpoint + 1, len);
    }

    static String getLoggerName(Class clazz) {
        if (clazz == null)
            return "sun.management.test";
        Package p = clazz.getPackage();
        if (p == null)
            return "sun.management.test";
        final String pname = p.getName();
        if (pname == null)
            return "sun.management.test";
        else
            return pname;
    }

    public TestLogger(Class clazz) {
        this(getLoggerName(clazz), getClassName(clazz));
    }

    public TestLogger(Class clazz, String postfix) {
        this(getLoggerName(clazz) + ((postfix == null) ? "" : "." + postfix), getClassName(clazz));
    }

    public TestLogger(String className) {
        this("sun.management.test", className);
    }

    public TestLogger(String loggerName, String className) {
        Logger l = null;
        try {
            l = Logger.getLogger(loggerName);
        } catch (Exception x) {
        }
        logger = l;
        this.className = className;
    }

    protected Logger getLogger() {
        return logger;
    }

    public boolean isTraceOn() {
        final Logger l = getLogger();
        if (l == null)
            return false;
        return l.isLoggable(Level.FINE);
    }

    public boolean isDebugOn() {
        final Logger l = getLogger();
        if (l == null)
            return false;
        return l.isLoggable(Level.FINEST);
    }

    public void error(String func, String msg) {
        final Logger l = getLogger();
        if (l != null)
            l.logp(Level.SEVERE, className, func, msg);
    }

    public void trace(String func, String msg) {
        final Logger l = getLogger();
        if (l != null)
            l.logp(Level.FINE, className, func, msg);
    }

    public void trace(String func, Throwable t) {
        final Logger l = getLogger();
        if (l != null)
            l.logp(Level.FINE, className, func, t.toString(), t);
    }

    public void trace(String func, String msg, Throwable t) {
        final Logger l = getLogger();
        if (l != null)
            l.logp(Level.FINE, className, func, msg, t);
    }

    public void debug(String func, String msg) {
        final Logger l = getLogger();
        if (l != null)
            l.logp(Level.FINEST, className, func, msg);
    }

    public void debug(String func, Throwable t) {
        final Logger l = getLogger();
        if (l != null)
            l.logp(Level.FINEST, className, func, t.toString(), t);
    }

    public void debug(String func, String msg, Throwable t) {
        final Logger l = getLogger();
        if (l != null)
            l.logp(Level.FINEST, className, func, msg, t);
    }
}
