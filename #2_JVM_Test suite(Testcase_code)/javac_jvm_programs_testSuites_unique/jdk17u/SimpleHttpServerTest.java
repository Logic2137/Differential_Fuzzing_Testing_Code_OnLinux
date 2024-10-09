import java.net.InetSocketAddress;
import java.net.ServerSocket;
import com.sun.net.httpserver.HttpServer;

public class SimpleHttpServerTest {

    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("java.version"));
        InetSocketAddress serverAddr = new InetSocketAddress(0);
        HttpServer server = HttpServer.create(serverAddr, 0);
        int serverPort = server.getAddress().getPort();
        server.start();
        server.stop(0);
        serverAddr = new InetSocketAddress(serverPort);
        int exceptionCount = 0;
        boolean failedOnce = false;
        System.out.println("Using serverPort == " + serverPort);
        RETRY: while (exceptionCount == 0) {
            for (int i = 0; i < 100; i++) {
                try {
                    server = HttpServer.create(serverAddr, 0);
                    server.start();
                    server.stop(0);
                } catch (Exception ex) {
                    if (!failedOnce) {
                        failedOnce = true;
                        server = HttpServer.create(new InetSocketAddress(0), 0);
                        serverPort = server.getAddress().getPort();
                        server.start();
                        server.stop(0);
                        serverAddr = new InetSocketAddress(serverPort);
                        System.out.println("Retrying with serverPort == " + serverPort);
                        continue RETRY;
                    }
                    System.err.println("Got exception at iteration: " + i);
                    ex.printStackTrace();
                    exceptionCount++;
                }
            }
            break;
        }
        if (exceptionCount > 0) {
            throw new RuntimeException("Test Failed: got " + exceptionCount + " exceptions.");
        }
    }
}
