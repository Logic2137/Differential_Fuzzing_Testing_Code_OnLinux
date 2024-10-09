



import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;


public class StopNoStartTest {

    public static void main(String[] args) throws Exception {

        InetSocketAddress serverAddr = new InetSocketAddress(0);
        HttpServer server = HttpServer.create(serverAddr, 0);
        server.stop(0);
    }
}
