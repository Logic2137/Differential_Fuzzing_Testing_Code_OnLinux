
package systempkg.log;

public class SystemLoggerAccessor {

    public static System.Logger getSystemLogger(String name) {
        return System.getLogger(name);
    }
}
