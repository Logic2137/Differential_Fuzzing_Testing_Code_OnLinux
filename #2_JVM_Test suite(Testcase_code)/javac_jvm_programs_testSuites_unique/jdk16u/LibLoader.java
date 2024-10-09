
package gc.g1.unloading.loading;

public class LibLoader {

    static {
        try {
            System.loadLibrary("define");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Could not load \"define\" library");
            System.err.println("java.library.path:" + System.getProperty("java.library.path"));
            throw e;
        }
    }

}
