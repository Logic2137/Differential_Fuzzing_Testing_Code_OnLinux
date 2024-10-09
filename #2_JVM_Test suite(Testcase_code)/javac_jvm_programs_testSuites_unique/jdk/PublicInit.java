
package java.lang;

public class PublicInit {
    private static boolean initialized;
    static {
        initialized = true;
    }
}
