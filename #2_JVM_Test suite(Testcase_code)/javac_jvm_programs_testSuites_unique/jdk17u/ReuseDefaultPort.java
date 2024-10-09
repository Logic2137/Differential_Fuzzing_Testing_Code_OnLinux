import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class ReuseDefaultPort implements Remote {

    private static int rmiPort = 0;

    private ReuseDefaultPort() {
    }

    public static void main(String[] args) throws Exception {
        System.err.println("\nRegression test for bug 6269166\n");
        RMISocketFactory.setSocketFactory(new SF());
        Remote impl = new ReuseDefaultPort();
        Remote stub = UnicastRemoteObject.exportObject(impl, 0);
        System.err.println("- exported object: " + stub);
        try {
            Registry registry = LocateRegistry.createRegistry(rmiPort);
            System.err.println("- exported registry: " + registry);
            System.err.println("TEST PASSED");
        } finally {
            UnicastRemoteObject.unexportObject(impl, true);
        }
    }

    private static class SF extends RMISocketFactory {

        private static RMISocketFactory defaultFactory = RMISocketFactory.getDefaultSocketFactory();

        SF() {
        }

        public Socket createSocket(String host, int port) throws IOException {
            System.err.format("in SF::createSocket: %s, %d%n", host, port);
            return defaultFactory.createSocket(host, port);
        }

        public ServerSocket createServerSocket(int port) throws IOException {
            System.err.format("in SF::createServerSocket: %d%n", port);
            ServerSocket server = defaultFactory.createServerSocket(port);
            rmiPort = server.getLocalPort();
            System.err.println("rmiPort: " + rmiPort);
            return server;
        }
    }
}
