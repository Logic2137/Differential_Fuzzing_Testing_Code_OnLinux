import java.nio.file.Paths;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogManagerInModuleTest {

    public static void main(String[] args) throws Exception {
        if (System.getProperty("java.util.logging.config.class", null) == null) {
            System.setProperty("java.util.logging.config.file", Paths.get(System.getProperty("test.src", "src"), "logging.properties").toString());
        }
        if (LogManagerInModuleTest.class.getModule().isNamed()) {
            throw new RuntimeException("Unexpected named module for " + LogManagerInModuleTest.class + ": " + LogManagerInModuleTest.class.getModule().getName());
        }
        LogManager manager = LogManager.getLogManager();
        System.out.println("LogManager: " + manager);
        Class<?> logManagerClass = manager.getClass();
        if (!"test.logmanager".equals(logManagerClass.getModule().getName())) {
            throw new RuntimeException("Bad module for log manager: " + logManagerClass.getModule() + "; class is: " + logManagerClass.getName());
        }
        Logger logger = Logger.getLogger("com.xyz.foo");
        Handler[] handlers = logger.getHandlers();
        if (handlers.length != 1) {
            throw new RuntimeException("Expected 1 handler, found " + handlers.length);
        }
        Class<?> handlerClass = handlers[0].getClass();
        if (!"test.handlers".equals(handlerClass.getModule().getName())) {
            throw new RuntimeException("Bad module for handler: " + handlerClass.getModule() + "; class is: " + handlerClass.getName());
        }
    }
}
