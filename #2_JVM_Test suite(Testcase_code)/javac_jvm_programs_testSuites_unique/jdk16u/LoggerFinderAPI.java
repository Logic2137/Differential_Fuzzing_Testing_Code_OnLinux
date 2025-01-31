

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.System.LoggerFinder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;




public class LoggerFinderAPI {

    
    static final String TEST_FORMAT = "LOG-%4$s:-[%2$s]-%5$s%6$s%n";
    static final String JDK_FORMAT_PROP_KEY = "jdk.system.logger.format";
    static final String JUL_FORMAT_PROP_KEY =
        "java.util.logging.SimpleFormatter.format";
    static final String MESSAGE = "{0} with {1}: PASSED";
    static final String LOCALIZED = "[localized] ";

    static class RecordStream extends OutputStream {
        static final Object LOCK = new Object[0];
        final PrintStream out;
        final PrintStream err;
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        boolean record;
        RecordStream(PrintStream out, PrintStream err) {
            this.out = out;
            this.err = err;
        }

        @Override
        public void write(int i) throws IOException {
            if (record) {
                bos.write(i);
                out.write(i);
            } else {
                err.write(i);
            }
        }

        void startRecording() {
            out.flush();
            err.flush();
            bos.reset();
            record = true;
        }
        byte[] stopRecording() {
            out.flush();
            err.flush();
            record = false;
            return bos.toByteArray();
        }
    }

    static final PrintStream ERR = System.err;
    static final PrintStream OUT = System.out;
    static final RecordStream LOG_STREAM = new RecordStream(OUT, ERR);
    static {
        Locale.setDefault(Locale.US);
        PrintStream perr = new PrintStream(LOG_STREAM);
        System.setErr(perr);
    }

    public static class MyResourceBundle extends ResourceBundle {
        final Map<String, String> map = Map.of(MESSAGE, LOCALIZED + MESSAGE);
        @Override
        protected Object handleGetObject(String string) {
            return map.get(string);
        }

        @Override
        public Enumeration<String> getKeys() {
            return Collections.enumeration(map.keySet());
        }

    }

    public static class EmptyResourceBundle extends ResourceBundle {
        @Override
        protected Object handleGetObject(String string) {
            return null;
        }

        @Override
        public Enumeration<String> getKeys() {
            return Collections.emptyEnumeration();
        }

    }

    public static void main(String[] args) {
        
        
        
        
        LoggerFinder finder = System.LoggerFinder.getLoggerFinder();
        System.out.println("LoggerFinder is " + finder.getClass().getName());

        LoggerFinderAPI apiTest = new LoggerFinderAPI();
        for (Object[] params : getLoggerDataProvider()) {
            @SuppressWarnings("unchecked")
            Class<? extends Throwable> throwableClass  =
                    Throwable.class.getClass().cast(params[3]);
            apiTest.testGetLogger((String)params[0],
                                  (String)params[1],
                                  (Module)params[2],
                                  throwableClass);
        }
        for (Object[] params : getLocalizedLoggerDataProvider()) {
            @SuppressWarnings("unchecked")
            Class<? extends Throwable> throwableClass =
                    Throwable.class.getClass().cast(params[4]);
            apiTest.testGetLocalizedLogger((String)params[0],
                                  (String)params[1],
                                  (ResourceBundle)params[2],
                                  (Module)params[3],
                                  throwableClass);
        }
    }

    
    
    void testGetLogger(String desc, String name, Module mod, Class<? extends Throwable> thrown) {
        try {
            LoggerFinder finder = System.LoggerFinder.getLoggerFinder();
            Logger logger = finder.getLogger(name, mod);
            if (thrown != null) {
                throw new AssertionError("Exception " + thrown.getName()
                        + " not thrown for "
                        + "LoggerFinder.getLogger"
                        + " with " + desc);
            }
            
            synchronized(RecordStream.LOCK) {
                LOG_STREAM.startRecording();
                byte[] logged = null;
                try {
                    logger.log(Level.INFO, "{0} with {1}: PASSED",
                               "LoggerFinder.getLogger",
                               desc);
                } finally {
                    logged = LOG_STREAM.stopRecording();
                }
                check(logged, "testGetLogger", desc, null,
                      "LoggerFinder.getLogger");
            }
        } catch (Throwable x) {
            if (thrown != null && thrown.isInstance(x)) {
                System.out.printf("Got expected exception for %s with %s: %s\n",
                        "LoggerFinder.getLogger", desc, String.valueOf(x));
            } else throw x;
        }
    }

    
    
    void testGetLocalizedLogger(String desc, String name, ResourceBundle bundle,
                                Module mod, Class<? extends Throwable> thrown) {
        try {
            LoggerFinder finder = System.LoggerFinder.getLoggerFinder();
            Logger logger = finder.getLocalizedLogger(name, bundle, mod);
            if (thrown != null) {
                throw new AssertionError("Exception " + thrown.getName()
                        + " not thrown for "
                        + "LoggerFinder.getLocalizedLogger"
                        + " with " + desc);
            }
            
            synchronized(RecordStream.LOCK) {
                LOG_STREAM.startRecording();
                byte[] logged = null;
                try {
                    logger.log(Level.INFO, MESSAGE,
                              "LoggerFinder.getLocalizedLogger",
                              desc);
                } finally {
                   logged = LOG_STREAM.stopRecording();
                }
                check(logged, "testGetLocalizedLogger", desc, bundle,
                      "LoggerFinder.getLocalizedLogger");
            }
        } catch (Throwable x) {
            if (thrown != null && thrown.isInstance(x)) {
                System.out.printf("Got expected exception for %s with %s: %s\n",
                        "LoggerFinder.getLocalizedLogger", desc, String.valueOf(x));
            } else throw x;
        }
    }

    private void check(byte[] logged, String test, String desc,
                       ResourceBundle bundle, String meth) {
        String msg = new String(logged);
        String localizedPrefix =
                ((bundle==null || bundle==EMPTY_BUNDLE)?"":LOCALIZED);
        String expected = String.format(TEST_FORMAT, null,
                "LoggerFinderAPI " + test, null, Level.INFO.name(),
                localizedPrefix + meth + " with " + desc + ": PASSED",
                "");
        if (!Objects.equals(msg, expected)) {
            throw new AssertionError("Expected log message not found: "
                                     + "\n\texpected:  " + expected
                                     + "\n\tretrieved: " + msg);
        }
    }


    static final Module MODULE = LoggerFinderAPI.class.getModule();
    static final ResourceBundle BUNDLE = new MyResourceBundle();
    static final ResourceBundle EMPTY_BUNDLE = new EmptyResourceBundle();
    static final Object[][] GET_LOGGER = {
        {"null name", null, MODULE , NullPointerException.class},
        {"null module", "foo", null, NullPointerException.class},
        {"null name and module", null, null, NullPointerException.class},
        {"non null name and module", "foo", MODULE, null},
    };
    static final Object[][] GET_LOCALIZED_LOGGER = {
        {"null name", null, BUNDLE, MODULE , NullPointerException.class},
        {"null module", "foo", BUNDLE, null, NullPointerException.class},
        {"null name and module, non null bundle", null, BUNDLE, null, NullPointerException.class},
        {"non null name, module, and bundle", "foo", BUNDLE, MODULE, null},
        {"null name and bundle", null, null, MODULE , NullPointerException.class},
        {"null module and bundle", "foo", null, null, NullPointerException.class},
        {"null name and module and bundle", null, null, null, NullPointerException.class},
        {"non null name and module, null bundle", "foo", null, MODULE, null},
        
        
        {"non null name, module, and empty bundle", "foo", EMPTY_BUNDLE, MODULE, null},
    };
    public static Object[][] getLoggerDataProvider() {
        return GET_LOGGER;
    }
    public static Object[][] getLocalizedLoggerDataProvider() {
        return GET_LOCALIZED_LOGGER;
    }
}
