

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.PlatformLoggingMXBean;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.logging.*;



public class CustomLevel extends Level {
    public CustomLevel(String name, int value, String resourceBundleName) {
        super(name, value, resourceBundleName);
    }

    private static final List<Level> levels = new ArrayList<>();
    private static final String RB_NAME = "myresource";
    private static final String OTHERRB_NAME = "myresource2";

    private static class CustomLevelReference extends WeakReference<Level> {
        final String name;
        final int value;
        final String resourceBundleName;
        public CustomLevelReference(Level level, ReferenceQueue<Level> queue) {
            super(level, queue);
            name = level.getName();
            value = level.intValue();
            resourceBundleName = level.getResourceBundleName();
        }

        @Override
        public String toString() {
            return "CustomLevelReference(\"" + name + "\", " + value + ", \""
                    + resourceBundleName + "\")";
        }

    }

    public static void main(String[] args) throws Exception {
        setupCustomLevels();
        setUpCustomLevelsOtherLoader();
        PlatformLoggingMXBean mxbean = ManagementFactory.getPlatformMXBean(PlatformLoggingMXBean.class);
        Logger logger = Logger.getLogger("foo.bar");

        
        for (Level level : levels) {
            final ResourceBundle rb = getResourceBundle(level);
            String name = level.getName();
            Level l = Level.parse(name);
            if (!name.equals("WARNING") && !name.equals("INFO")
                 && !name.equals("SEVERE")) {
                
                checkCustomLevel(l, level);
            } else if (l != Level.WARNING && l != Level.INFO && l != Level.SEVERE
                    || !name.equals(l.getName())) {
                throw new RuntimeException("Unexpected level " + formatLevel(l));
            }
            System.out.println("Level.parse found expected level: "
                            + formatLevel(l));
            String localizedName = rb.getString(level.getName());
            l = Level.parse(localizedName);
            if (l != level) {
                throw new RuntimeException("Unexpected level " + l + " "
                    + l.getClass() + " for " + localizedName
                    + " in " + rb.getBaseBundleName());
            }
            l = Level.parse(String.valueOf(level.intValue()));
            System.out.println("Level.parse(" + level.intValue() + ") returns " + l);
            if (l != level) {
                if (l == null || l.intValue() != level.intValue()) {
                    throw new RuntimeException("Unexpected level " + l
                            + (l == null ? "" : (" " + l.getClass()))
                            + " for " + level.intValue());
                }
            }
            mxbean.setLoggerLevel(logger.getName(), String.valueOf(level.intValue()));
            Level l2 = logger.getLevel();
            if (l2 != level) {
                if (l2 == null || l2.intValue() != level.intValue()) {
                    throw new RuntimeException("Unexpected level " + l2
                            + (l2 == null ? "" : (" " + l2.getClass()))
                            + " for " + level.intValue());
                }
            }
        }


        final long otherLevelCount = levels.stream()
            .filter(CustomLevel::isCustomLoader)
            .count();

        
        
        ReferenceQueue<Level> queue = new ReferenceQueue<>();
        List<CustomLevelReference> refs = new ArrayList<>();
        List<CustomLevelReference> customRefs = new ArrayList<>();
        int otherLevels = 0;
        while (!levels.isEmpty()) {
            Level l = levels.stream().findAny().get();
            boolean isCustomLoader = isCustomLoader(l);
            if (isCustomLoader) otherLevels++;

            CustomLevelReference ref = new CustomLevelReference(l, queue);
            if (isCustomLoader) {
                customRefs.add(ref);
            } else {
                refs.add(ref);
            }

            
            levels.remove(l);
            l = null;

            
            if (otherLevels == otherLevelCount) {
                if (customRefs.size() != otherLevelCount) {
                    throw new RuntimeException("Test bug: customRefs.size() != "
                             + otherLevelCount);
                }
                waitForGC(customRefs, queue);
            }
        }
        if (otherLevelCount != otherLevels || otherLevelCount == 0) {
            throw new RuntimeException("Test bug: "
                + "no or wrong count of levels loaded from custom loader");
        }
        if (!customRefs.isEmpty()) {
            throw new RuntimeException(
                "Test bug: customRefs.size() should be empty!");
        }
        while (!refs.isEmpty()) {
            final Reference<?> ref = refs.remove(0);
            if (ref.get() == null) {
                throw new RuntimeException("Unexpected garbage collection for "
                           + ref);
            }
        }
    }

    private static void waitForGC(List<CustomLevelReference> customRefs,
                                  ReferenceQueue<Level> queue)
         throws InterruptedException
    {
        while (!customRefs.isEmpty()) {
            Reference<? extends Level> ref2;
            do {
                System.gc();
                Thread.sleep(100);
            } while ((ref2 = queue.poll()) == null);

            
            if (!customRefs.contains(ref2)) {
               throw new RuntimeException("Unexpected reference: " + ref2);
            }
            CustomLevelReference ref = customRefs.remove(customRefs.indexOf(ref2));
            System.out.println(ref2 + " garbage collected");
            final String name = ref.name;
            Level l;
            try {
                l = Level.parse(name);
                if (!name.equals("SEVERE")
                    && !name.equals("INFO")
                    || !name.equals(l.getName())) {
                    throw new RuntimeException("Unexpected level "
                            + formatLevel(l));
                } else {
                    if (l == Level.WARNING || l == Level.INFO
                            || l == Level.SEVERE) {
                        System.out.println("Level.parse found expected level: "
                                + formatLevel(l));
                    } else {
                        throw new RuntimeException("Unexpected level "
                            + formatLevel(l));
                    }
                }
            } catch (IllegalArgumentException iae) {
                if (!name.equals("WARNING")
                    && !name.equals("INFO")
                    && !name.equals("SEVERE")) {
                    System.out.println("Level.parse fired expected exception: "
                        + iae);
                } else {
                    throw iae;
                }
            }
        }
    }

    private static boolean isCustomLoader(Level level) {
        final ClassLoader cl = level.getClass().getClassLoader();
        return cl != null
             && cl != ClassLoader.getPlatformClassLoader()
             && cl != ClassLoader.getSystemClassLoader();
    }

    static ResourceBundle getResourceBundle(Level level) {
        return isCustomLoader(level)
            ? ResourceBundle.getBundle(OTHERRB_NAME, Locale.getDefault(),
                                       level.getClass().getClassLoader())
            : ResourceBundle.getBundle(RB_NAME);
    }

    private static void setupCustomLevels() throws IOException {
        levels.add(new CustomLevel("EMERGENCY", 1090, RB_NAME));
        levels.add(new CustomLevel("ALERT", 1060, RB_NAME));
        levels.add(new CustomLevel("CRITICAL", 1030, RB_NAME));
        levels.add(new CustomLevel("WARNING", 1010, RB_NAME));
        levels.add(new CustomLevel("INFO", 1000, RB_NAME));
    }

    static void setUpCustomLevelsOtherLoader()
         throws MalformedURLException,
               ClassNotFoundException, NoSuchMethodException,
               IllegalAccessException, InvocationTargetException
    {
        final String classes = System.getProperty("test.classes",
                                                  "build/classes");
        final String sources = System.getProperty("test.src",
                                                  "src");
        final URL curl = new File(classes).toURI().toURL();
        final URL surl = new File(sources).toURI().toURL();
        URLClassLoader loader = new URLClassLoader(new URL[] {curl, surl},
                                     ClassLoader.getPlatformClassLoader());
        Class<?> customLevelClass = Class.forName("CustomLevel", false, loader);
        Method m = customLevelClass.getMethod("setUpCustomLevelsOtherLoader",
                                              List.class);
        m.invoke(null, levels);
    }

    public static void setUpCustomLevelsOtherLoader(List<Level> levels) {
        levels.add(new CustomLevel("OTHEREMERGENCY", 1091, OTHERRB_NAME));
        levels.add(new CustomLevel("OTHERALERT", 1061, OTHERRB_NAME));
        levels.add(new CustomLevel("OTHERCRITICAL", 1031, OTHERRB_NAME));
        levels.add(new CustomLevel("SEVERE", 1011, OTHERRB_NAME));
        levels.add(new CustomLevel("INFO", 1000, OTHERRB_NAME));
    }

    static void checkCustomLevel(Level level, Level expected) {
        
        if (!level.equals(expected)) {
            throw new RuntimeException(formatLevel(level) + " != "
                 + formatLevel(expected));
        }

        if (!level.getName().equals(expected.getName())) {
            throw new RuntimeException(formatLevel(level) + " != "
                 + formatLevel(expected));
        }

        
        if (level != expected) {
            throw new RuntimeException(formatLevel(level) + " != "
                 + formatLevel(expected));
        }

        final ResourceBundle rb = getResourceBundle(level);
        String name = rb.getString(level.getName());
        if (!level.getLocalizedName().equals(name)) {
            
            throw new RuntimeException(level.getLocalizedName() + " != " + name);
        }
    }

    static String formatLevel(Level l) {
        return l + ":" + l.intValue() + ":" + l.getClass().getName();
    }
}
