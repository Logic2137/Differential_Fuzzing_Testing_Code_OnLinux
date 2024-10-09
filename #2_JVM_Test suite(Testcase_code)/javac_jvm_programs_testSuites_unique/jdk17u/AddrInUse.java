import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;

public class AddrInUse implements Runnable {

    private static int port = -1;

    private static final long TIMEOUT = 10000;

    private boolean exportSucceeded = false;

    private Throwable exportException = null;

    public void run() {
        try {
            LocateRegistry.createRegistry(port);
            synchronized (this) {
                exportSucceeded = true;
                notifyAll();
            }
        } catch (Throwable t) {
            synchronized (this) {
                exportException = t;
                notifyAll();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.err.println("\nRegression test for bug 4111507\n");
        ServerSocket server = new ServerSocket(0);
        port = server.getLocalPort();
        System.err.println("Created a ServerSocket on port " + port + "...");
        System.err.println("create a registry on the same port...");
        System.err.println("(should cause an ExportException)");
        AddrInUse obj = new AddrInUse();
        synchronized (obj) {
            (new Thread(obj, "AddrInUse")).start();
            obj.wait(TIMEOUT);
            if (obj.exportSucceeded) {
                throw new RuntimeException("TEST FAILED: export on already-bound port succeeded");
            } else if (obj.exportException != null) {
                obj.exportException.printStackTrace();
                if (obj.exportException instanceof ExportException) {
                    System.err.println("TEST PASSED");
                } else {
                    throw new RuntimeException("TEST FAILED: unexpected exception occurred", obj.exportException);
                }
            } else {
                throw new RuntimeException("TEST FAILED: export timed out");
            }
        }
    }
}
