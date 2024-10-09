
package pkg.b.l;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.ResourceBundle;

public class LoggerB implements System.Logger {

    private static Map<String, LoggerB> map = new HashMap<>();

    public static LoggerB getLogger(String name) {
        return map.computeIfAbsent(name, (n) -> new LoggerB());
    }

    public static boolean checkLog(String name, Level level, ResourceBundle bundle, String format, Throwable throwable, Object... params) {
        LoggerB logger = map.get(name);
        LogEvent event = new LogEvent(level, bundle, format, null, params);
        for (LogEvent l : logger.queue) {
            if (l.equals(event)) {
                return true;
            }
        }
        return false;
    }

    private Queue<LogEvent> queue = new LinkedList<>();

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isLoggable(Level level) {
        return true;
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
        String msg = bundle != null ? bundle.getString(format) : format;
        log(new LogEvent(level, bundle, msg, null, params));
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Throwable throwable) {
        String msg = bundle != null ? bundle.getString(format) : format;
        log(new LogEvent(level, bundle, msg, throwable, (Object) null));
    }

    void log(LogEvent l) {
        print(l);
        queue.add(l);
    }

    private void print(LogEvent l) {
        System.err.println("LoggerB Message" + l);
    }

    public Queue<LogEvent> getLogEvent() {
        return queue;
    }

    public static class LogEvent {

        public LogEvent(Level level, ResourceBundle bundle, String format, Throwable throwable, Object... params) {
            this.level = level;
            this.bundle = bundle;
            this.format = format;
            this.throwable = throwable;
            this.params = params;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof LogEvent) {
                LogEvent e = (LogEvent) o;
                return level == e.level && bundle == e.bundle && format == e.format && params == e.params;
            }
            return false;
        }

        private Level level;

        private ResourceBundle bundle;

        private String format;

        private Throwable throwable;

        private Object[] params;
    }
}
