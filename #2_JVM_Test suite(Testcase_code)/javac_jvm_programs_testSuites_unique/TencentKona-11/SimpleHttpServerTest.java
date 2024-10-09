



import java.net.InetSocketAddress;
import java.net.ServerSocket;

import com.sun.net.httpserver.HttpServer;


public class SimpleHttpServerTest {

    public static void main(String[] args) throws Exception {

        System.out.println(System.getProperty("java.version"));
        InetSocketAddress serverAddr = new InetSocketAddress(0);
        HttpServer server = HttpServer.create(serverAddr, 0);
        final int serverPort = server.getAddress().getPort();
        server.start();
        server.stop(0);
        serverAddr = new InetSocketAddress(serverPort);
        int exceptionCount = 0;
        System.out.println("Using serverPort == " + serverPort);
        for (int i = 0; i < 100; i++) {
            try {
                server = HttpServer.create(serverAddr, 0);
                server.start();
                server.stop(0);
            } catch (Exception ex) {
                ex.printStackTrace();
                exceptionCount++;
            }
        }
        if (exceptionCount > 0) {
           throw new RuntimeException("Test Failed");
        }
    }
}
