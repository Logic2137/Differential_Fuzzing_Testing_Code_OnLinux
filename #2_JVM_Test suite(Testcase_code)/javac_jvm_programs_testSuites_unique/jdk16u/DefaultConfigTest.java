



import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DefaultConfigTest {
    static final java.io.PrintStream out = System.out;
    public static void main(String arg[]) {
        LogManager manager = LogManager.getLogManager();

        if (!Set.of(Collections.list(manager.getLoggerNames()).toArray())
                .equals(Set.of("", "global"))) {
            throw new RuntimeException("Unexpected logger name in: "
                    + Collections.list(manager.getLoggerNames()));
        }
        Logger global = Logger.getGlobal();
        Logger root = Logger.getLogger("");
        Logger foo  = Logger.getLogger("com.xyz.foo");
        if (!Set.of(Collections.list(manager.getLoggerNames()).toArray())
                .equals(Set.of("", "global", "com.xyz.foo"))) {
            throw new RuntimeException("Unexpected logger name in: "
                    + Collections.list(manager.getLoggerNames()));
        }
        for (Logger l : List.of(global, foo)) {
            if (l.getLevel() != null) {
                throw new RuntimeException("Unexpected level "
                        + l.getLevel() + " for " + l.getName());
            }
            if (l.getHandlers().length != 0) {
                throw new RuntimeException("Unexpected handlers "
                        + List.of(l.getHandlers()) + " for " + l.getName());
            }
            for (Level level : List.of(
                    Level.ALL, Level.FINEST, Level.FINER, Level.FINE, Level.CONFIG)) {
                if (l.isLoggable(level)) {
                    throw new RuntimeException("Level "
                            + level + " should not be loggable for " + l.getName());
                }
            }
            for (Level level : List.of(
                    Level.INFO, Level.WARNING, Level.SEVERE)) {
                if (!l.isLoggable(level)) {
                    throw new RuntimeException("Level "
                            + level + " should be loggable for " + l.getName());
                }
            }
        }
        if (root.getLevel() != Level.INFO) {
            throw new RuntimeException("Unexpected level "
                    + root.getLevel() + " for root logger");
        }
        if (root.getHandlers().length != 1) {
            throw new RuntimeException("Unexpected handlers "
                    + List.of(root.getHandlers()) + " for root logger");
        }
        if (root.getHandlers()[0].getClass() != ConsoleHandler.class) {
            throw new RuntimeException("Unexpected handlers "
                    + List.of(root.getHandlers()) + " for root logger");
        }
        if (root.getHandlers()[0].getLevel() != Level.INFO) {
            throw new RuntimeException("Unexpected level "
                    + root.getHandlers()[0].getLevel() + " for handler "
                    + root.getHandlers()[0]);
        }
        out.println(List.of(root, global, foo).stream()
                .map(Logger::getName)
                .map(s -> String.format("\"%s\"", s))
                .collect(Collectors.joining(", ",
                        "Default configuration checked for ",
                        ".")));
    }
}
