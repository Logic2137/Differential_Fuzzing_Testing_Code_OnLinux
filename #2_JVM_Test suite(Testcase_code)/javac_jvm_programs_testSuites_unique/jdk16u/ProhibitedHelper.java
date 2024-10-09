

public class ProhibitedHelper {
    public static void main(String[] args) throws Throwable {

        String className = "java.lang.Prohibited";
        ClassLoader sysLoader = ClassLoader.getSystemClassLoader();
        try {
            Module unnamedModule = sysLoader.getUnnamedModule();
            Class cls = Class.forName(unnamedModule, className);
            System.out.println("cls " + cls);
            throw new java.lang.RuntimeException(className +
                "in a prohibited package shouldn't be loaded");
        } catch (Exception e) {
            e.printStackTrace();
            if (!(e instanceof java.lang.SecurityException)) {
                throw new java.lang.RuntimeException(
                    "SecurityException is expected to be thrown while loading " + className);
            }
        }

        try {
            Class cls = Class.forName(className, false, sysLoader);
            System.out.println("cls " + cls);
            throw new java.lang.RuntimeException(className +
                "in a prohibited package shouldn't be loaded");
        } catch (Exception e) {
            e.printStackTrace();
            if (!(e instanceof java.lang.ClassNotFoundException)) {
                throw new java.lang.RuntimeException(
                    "ClassNotFoundException is expected to be thrown while loading " + className);
            }
        }
    }
}
