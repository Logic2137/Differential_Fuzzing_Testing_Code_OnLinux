
package p1;

public class Initializer {

    private static boolean inited = false;

    static synchronized void init() {
        inited = true;
    }

    public static synchronized boolean isInited() {
        return inited;
    }
}
