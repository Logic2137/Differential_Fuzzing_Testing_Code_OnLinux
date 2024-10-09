
package java.lang;

class DefaultInit {
    private static boolean initialized;
    static {
        initialized = true;
    }
}
