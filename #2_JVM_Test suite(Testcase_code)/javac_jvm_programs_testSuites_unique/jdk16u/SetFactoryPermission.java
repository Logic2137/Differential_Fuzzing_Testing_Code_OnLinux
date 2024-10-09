



import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.server.RMISocketFactory;
import java.security.AccessControlException;

public class SetFactoryPermission {
    static boolean success = false;

    interface Runner {
        public void run() throws Exception;
    }

    public static void main (String[] args) throws Exception {
        if (args.length > 0) {
            success = System.getSecurityManager() == null || args[0].equals("success");
        }

        doTest(()->{
            System.out.println("Verify URLConnection.setContentHandlerFactor()");
            URLConnection.setContentHandlerFactory(null);
        });
        doTest(()->{
            System.out.println("Verify URL.setURLStreamHandlerFactory()");
            URL.setURLStreamHandlerFactory(null);
        });
        doTest(()->{
            System.out.println("Verify ServerSocket.setSocketFactory()");
            ServerSocket.setSocketFactory(null);
        });
        doTest(()->{
            System.out.println("Verify Socket.setSocketImplFactory()");
            Socket.setSocketImplFactory(null);
        });
        doTest(()->{
            System.out.println("Verify RMISocketFactory.setSocketFactory()");
            RMISocketFactory.setSocketFactory(null);
        });
    }

    static void doTest(Runner func) throws Exception {
        try {
            func.run();
            if (!success) {
                throw new RuntimeException("AccessControlException is not thrown. Test failed");
            }
        } catch (SecurityException e) {
            if (success) {
                e.printStackTrace();
                throw new RuntimeException("AccessControlException is thrown unexpectedly. Test failed");
            }
        }
    }
}
