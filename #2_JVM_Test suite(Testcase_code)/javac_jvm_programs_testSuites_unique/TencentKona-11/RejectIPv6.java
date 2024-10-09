



import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class RejectIPv6 {

    public static void main(String [] argv) throws Throwable {
        ServerSocket serverSocket = new ServerSocket(0);
        serverSocket.setSoTimeout(1000);
        int serverPort = serverSocket.getLocalPort();
        Socket clientSocket = new Socket();

        test("bind", () -> clientSocket.bind(
                new InetSocketAddress("::1", 0)));

        test("connect", () -> clientSocket.connect(
                new InetSocketAddress("::1", serverPort), 1000));
    }

    static void test(String msg, CodeToTest codeToTest) throws Throwable {
        Thread client = new Thread(() ->
            {
                try {
                    codeToTest.run();
                    throw new RuntimeException(msg +
                            " failed to reject IPv6 address");
                } catch (SocketException ok) {
                } catch (Exception exc) {
                    throw new RuntimeException("unexpected", exc);
                }
            });
        client.start();
        client.join();
    }

    interface CodeToTest {
        void run() throws Exception;
    }
}
