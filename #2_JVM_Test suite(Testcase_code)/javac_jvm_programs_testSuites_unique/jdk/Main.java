



package jdk.test;

public class Main {
    static final ClassLoader BOOT_LOADER     = null;
    static final ClassLoader PLATFORM_LOADER = ClassLoader.getPlatformClassLoader();
    static final ClassLoader SYS_LOADER      = ClassLoader.getSystemClassLoader();

    public static void main(String[] args) throws Exception {
        boolean shouldLoad = false;
        ClassLoader expectedLoader = SYS_LOADER;

        

        assertTrue(args.length <= 4);
        String testName = args[0];
        String className = args[1].replace('/', '.');
        String shouldLoadName = args[2];  
        String loaderName = "SYS";
        if (args.length == 4) {
            loaderName = args[3];
        }

        if (shouldLoadName.equals("true")) {
            shouldLoad = true;
        } else if (shouldLoadName.equals("false")) {
            shouldLoad = false;
        } else {
            assertTrue(false);
        }

        if (loaderName.equals("SYS")) {
            expectedLoader = SYS_LOADER;
        } else if (loaderName.equals("EXT")) {
            expectedLoader = PLATFORM_LOADER;
        } else if (loaderName.equals("BOOT")) {
            expectedLoader = BOOT_LOADER;
        }

        System.out.println(testName + ": class=" + className + " shouldLoad=" +
                           shouldLoadName + " by loader:" + expectedLoader);

        
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }

        if (clazz != null) {
            
            if (shouldLoad) {
                
                ClassLoader actualLoader = clazz.getClassLoader();
                if (actualLoader != expectedLoader) {
                    throw new RuntimeException(testName + " FAILED: " + clazz + " loaded by " + actualLoader +
                                               ", expected " + expectedLoader);
                }
                
                
                if (actualLoader == SYS_LOADER) {
                    String s = clazz.newInstance().toString();
                    if (!s.equals("hi")) {
                        throw new RuntimeException(testName + " FAILED: toString() returned \"" + s
                                                   + "\" instead of \"hi\"" );
                    }
                }
                System.out.println(testName + " PASSED: class loaded as expected.");
            } else {
                throw new RuntimeException(testName + " FAILED: class loaded, but should have failed to load.");
            }
        } else {
            
            if (shouldLoad) {
                throw new RuntimeException(testName + " FAILED: class failed to load.");
            } else {
                System.out.println(testName + " PASSED: ClassNotFoundException thrown as expected");
            }
        }
    }

    static void assertTrue(boolean expr) {
        if (!expr)
            throw new RuntimeException("assertion failed");
    }
}
