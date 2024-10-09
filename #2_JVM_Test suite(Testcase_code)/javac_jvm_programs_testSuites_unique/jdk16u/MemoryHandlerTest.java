


import java.io.File;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.MemoryHandler;

public class MemoryHandlerTest {

    static final String CFG_FILE_PROP = "java.util.logging.config.file";
    static final String LM_PROP_FNAME = "MemoryHandlerTest.props";
    static Logger logger;

    public static void main(String... args) throws IOException {
        
        String tstSrc = System.getProperty("test.src", ".");
        File fname = new File(tstSrc, LM_PROP_FNAME);
        String prop = fname.getCanonicalPath();
        System.setProperty(CFG_FILE_PROP, prop);
        LogManager.getLogManager();
        
        logger = Logger.getLogger(MemoryHandlerTest.class.getName());
        
        logger.setUseParentHandlers(false);
        
        
        
        
        
        CustomMemoryHandler cmh = new CustomMemoryHandler();
        try {
            logger.addHandler(cmh);
        } catch (RuntimeException rte) {
            throw new RuntimeException(
                "Test Failed: did not load java.util.logging.ConsoleHandler as expected",
                rte);
        }
        
        
        if (CustomTargetHandler.numLoaded !=1) {
            throw new RuntimeException(
                "Test failed: did not load CustomTargetHandler as expected");
        }
        
        
        
        CustomMemoryHandlerNoTarget cmhnt = null;
        try {
            cmhnt = new CustomMemoryHandlerNoTarget();
        } catch (RuntimeException re) {
            
            System.out.println("Info: " + re.getMessage() + " as expected.");
        }
        if (cmhnt != null) {
            throw new RuntimeException(
                "Test Failed: erroneously loaded CustomMemoryHandlerNoTarget");
        }

        
        logger.log(Level.WARNING, "Unused");
        if (CustomTargetHandler.numPublished != 1) {
            throw new RuntimeException("Test failed: CustomTargetHandler was not used");
        }

        
        if (SimpleTargetHandler.numPublished != 0) {
            throw new RuntimeException("Test failed: SimpleTargetHandler has been used");
        }

        
        
        MemoryHandler mh = new MemoryHandler();
        mh.publish(new LogRecord(Level.INFO, "Unused msg to MemoryHandler"));
        
        if (SimpleTargetHandler.numPublished != 1) {
            throw new RuntimeException("Test failed: SimpleTargetHandler was not used");
        }
    }

    public static class CustomMemoryHandler extends MemoryHandler {
    }

    public static class CustomMemoryHandlerNoTarget extends MemoryHandler {
    }

    public static class CustomTargetHandler extends Handler {

        public static int numPublished;
        public static int numLoaded;

        public CustomTargetHandler() {
            numLoaded++;
        }

        @Override
        public void publish(LogRecord unused) {
            numPublished++;
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }
    }

    public static class SimpleTargetHandler extends Handler {
        public static int numPublished;

        @Override
        public void publish(LogRecord unused) {
            numPublished++;
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }
    }
}
