



import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CloseSocket {

    private static ArrayList<TestCase> testCases = new ArrayList<>();

    static {
        testCases.add(socket -> socket.startHandshake());
        testCases.add(socket -> {
            InputStream in = socket.getInputStream();
            in.read();
        });
        testCases.add(socket -> {
            OutputStream out = socket.getOutputStream();
            out.write(43);
        });
    }

    public static void main(String[] args) throws Exception {
        try (Server server = new Server()) {
            new Thread(server).start();

            SocketFactory factory = SSLSocketFactory.getDefault();
            try (SSLSocket socket = (SSLSocket) factory.createSocket("localhost",
                    server.getPort())) {
                socket.setSoTimeout(2000);
                System.out.println("Client established TCP connection");
                boolean failed = false;
                for (TestCase testCase : testCases) {
                    try {
                        testCase.test(socket);
                        System.out.println("ERROR: no exception");
                        failed = true;
                    } catch (IOException e) {
                        System.out.println("Failed as expected: " + e);
                    }
                }
                if (failed) {
                    throw new Exception("One or more tests failed");
                }
            }
        }
    }

    static class Server implements AutoCloseable, Runnable {

        final ServerSocket serverSocket;

        Server() throws IOException {
            serverSocket = new ServerSocket(0);
        }

        public int getPort() {
            return serverSocket.getLocalPort();
        }

        @Override
        public void run() {
            try (Socket s = serverSocket.accept()) {
                System.out.println("Server accepted connection");
                
                
                Thread.currentThread().sleep(100);
                s.close();
                System.out.println("Server closed socket, done.");
            } catch (Exception e) {
                throw new RuntimeException("Problem in test execution", e);
            }
        }

        @Override
        public void close() throws Exception {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }
        }
    }

    interface TestCase {
        void test(SSLSocket socket) throws IOException;
    }
}
