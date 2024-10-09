
package test.loggerfinder;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.System.LoggerFinder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.lang.StackWalker.StackFrame;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestLoggerFinder extends LoggerFinder {

    static class TestLogger implements Logger {

        final String name;

        public TestLogger(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isLoggable(Level level) {
            return name.equals("javax.management") || name.startsWith("javax.management.") || level.getSeverity() >= Level.INFO.getSeverity();
        }

        @Override
        public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
            if (!isLoggable(level))
                return;
            publish(level, bundle, msg, thrown);
        }

        @Override
        public void log(Level level, ResourceBundle bundle, String format, Object... params) {
            if (!isLoggable(level))
                return;
            publish(level, bundle, format, params);
        }

        static void publish(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
            StackFrame sf = new CallerFinder().get().get();
            if (bundle != null && msg != null) {
                msg = bundle.getString(msg);
            }
            if (msg == null)
                msg = "";
            LocalDateTime ldt = LocalDateTime.now();
            String date = DateTimeFormatter.ISO_DATE_TIME.format(ldt);
            System.err.println(date + " " + sf.getClassName() + " " + sf.getMethodName() + "\n" + String.valueOf(level) + ": " + msg);
            thrown.printStackTrace(System.err);
        }

        static void publish(Level level, ResourceBundle bundle, String format, Object... params) {
            StackFrame sf = new CallerFinder().get().get();
            if (bundle != null && format != null) {
                format = bundle.getString(format);
            }
            String msg = format(format, params);
            LocalDateTime ldt = LocalDateTime.now();
            String date = DateTimeFormatter.ISO_DATE_TIME.format(ldt);
            System.err.println(date + " " + sf.getClassName() + " " + sf.getMethodName() + "\n" + String.valueOf(level) + ": " + msg);
        }

        static String format(String format, Object... args) {
            if (format == null)
                return "";
            int index = 0, len = format.length();
            while ((index = format.indexOf(index, '{')) >= 0) {
                if (index >= len - 2)
                    break;
                char c = format.charAt(index + 1);
                if (c >= '0' && c <= '9') {
                    return MessageFormat.format(format, args);
                }
                index++;
            }
            return format;
        }
    }

    static final class CallerFinder implements Predicate<StackWalker.StackFrame> {

        private static final StackWalker WALKER;

        static {
            PrivilegedAction<StackWalker> pa = () -> StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
            WALKER = AccessController.doPrivileged(pa);
        }

        Optional<StackWalker.StackFrame> get() {
            return WALKER.walk((s) -> s.filter(this).findFirst());
        }

        private boolean lookingForLogger = true;

        @Override
        public boolean test(StackWalker.StackFrame s) {
            Class<?> c = s.getDeclaringClass();
            boolean isLogger = System.Logger.class.isAssignableFrom(c);
            if (lookingForLogger) {
                lookingForLogger = c != TestLogger.class;
                return false;
            }
            return !isLogger;
        }
    }

    @Override
    public Logger getLogger(String name, Module module) {
        return new TestLogger(name);
    }
}
