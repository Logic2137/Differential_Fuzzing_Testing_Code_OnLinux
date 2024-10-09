

import java.io.*;
import java.util.*;
import java.util.logging.*;


public class CustomLogManager extends LogManager {
    static LogManager INSTANCE;
    Map<String,Logger> namedLoggers = new HashMap<>();
    Properties props = initConfig();
    public CustomLogManager() {
        if (INSTANCE != null) {
            throw new RuntimeException("CustomLogManager already created");
        }
        INSTANCE = this;
    }

    private boolean useParentHandlers(String loggerName) {
        String s = props.getProperty(loggerName + ".useParentHandlers");
        if (s == null)
            return true;   

        s = s.toLowerCase();
        if (s.equals("true") || s.equals("1")) {
           return true;
        } else if (s.equals("false") || s.equals("0")) {
           return false;
        }
        return true;
    }

    public synchronized boolean addLogger(Logger logger) {
        String name = logger.getName();
        if (namedLoggers.containsKey(name)) {
            return false;
        }
        namedLoggers.put(name, logger);
        
        if (props.get(name + ".level") != null) {
            logger.setLevel(Level.parse(props.getProperty(name + ".level")));
        }
        
        if (props.get(name + ".handlers") != null && logger.getHandlers().length == 0) {
            logger.addHandler(new CustomHandler());
        }
        if (!useParentHandlers(name)) {
            logger.setUseParentHandlers(false);
        }
        
        int ix = 1;
        for (;;) {
            int ix2 = name.indexOf(".", ix);
            if (ix2 < 0) {
                break;
            }
            String pname = name.substring(0, ix2);
            if (props.get(pname + ".level") != null ||
                props.get(pname + ".handlers") != null) {
                
                
                
                
                if (!namedLoggers.containsKey(pname)) {
                    Logger parent = Logger.getLogger(pname);
                    if (!useParentHandlers(pname)) {
                        parent.setUseParentHandlers(false);
                    }
                }
            }
            ix = ix2 + 1;
        }
        return true;
    }

    public synchronized Logger getLogger(String name) {
        return namedLoggers.get(name);
    }

    public synchronized Enumeration<String> getLoggerNames() {
        return Collections.enumeration(namedLoggers.keySet());
    }

    public String getProperty(String name) {
        return props.getProperty(name);
    }

    public void readConfiguration() {
        
    }

    public void readConfiguration(InputStream ins) {
        
    }

    private Properties initConfig() {
        Properties props = new Properties();
        props.put(".level", "CONFIG");
        props.put("CustomLogManagerTest.level", "WARNING");
        props.put("CustomLogManagerTest.handlers", "CustomLogManager$CustomHandler");
        props.put("SimpleLogManager.level", "INFO");
        props.put("SimpleLogManager.handlers", "CustomLogManager$CustomHandler");
        props.put("CustomLogManager$CustomHandler.level", "WARNING");
        props.put(".handlers", "CustomLogManager$CustomHandler");
        props.put("org.foo.bar.level", "SEVERE");
        props.put("org.foo.bar.useParentHandlers", "true");
        props.put("org.foo.handlers", "CustomLogManager$CustomHandler");
        props.put("org.foo.useParentHandlers", "false");
        props.put("org.openjdk.level", "SEVERE");
        props.put("org.openjdk.handlers", "CustomLogManager$CustomHandler");
        props.put("org.openjdk.core.level", "INFO");
        props.put("org.openjdk.core.useParentHandlers", "false");

        return props;
    }
    public static void checkLogger(String name) {
        checkLogger(name, null);
    }

    public static void checkLogger(String name, String resourceBundleName) {
        Logger logger = INSTANCE.getLogger(name);
        if (logger == null) {
            throw new RuntimeException("Logger \"" + name + "\" not exist");
        }
        System.out.format("Logger \"%s\" level=%s handlers=%s resourcebundle=%s useParentHandlers=%s%n",
            name, logger.getLevel(),
            Arrays.toString(logger.getHandlers()),
            logger.getResourceBundleName(),
            logger.getUseParentHandlers());
        String rb = logger.getResourceBundleName();
        if (rb != resourceBundleName && (rb == null || rb.equals(resourceBundleName))) {
            throw new RuntimeException("Logger \"" + name +
                "\" unexpected resource bundle: " + rb);
        }

        String value = INSTANCE.getProperty(name + ".level");
        String level = logger.getLevel() != null ? logger.getLevel().getName() : null;
        if (level != value && (level == null || level.equals(value))) {
            throw new RuntimeException("Logger \"" + name + "\" unexpected level: " + level);
        }

        Handler[] handlers = logger.getHandlers();
        String hdl = INSTANCE.getProperty(name + ".handlers");
        if ((hdl == null && handlers.length != 0) ||
            (hdl != null && handlers.length != 1)) {
            throw new RuntimeException("Logger \"" + name + "\" unexpected handler: " +
                Arrays.toString(handlers));
        }

        String s = INSTANCE.getProperty(name + ".useParentHandlers");
        boolean uph = (s != null && s.equals("false")) ? false : true;
        if (logger.getUseParentHandlers() != uph) {
            throw new RuntimeException("Logger \"" + name + "\" unexpected useParentHandlers: " +
                logger.getUseParentHandlers());
        }
        checkParents(name);
    }

    private static void checkParents(String name) {
        int ix = 1;
        for (;;) {
            int ix2 = name.indexOf(".", ix);
            if (ix2 < 0) {
                break;
            }
            String pname = name.substring(0, ix2);
            if (INSTANCE.getProperty(pname + ".level") != null ||
                INSTANCE.getProperty(pname + ".handlers") != null) {
                
                
                checkLogger(pname);
            }
            ix = ix2 + 1;
        }
    }

    
    private class CustomHandler extends StreamHandler {
    }
}
