import java.net.*;
import java.io.*;

public class B6890349 extends Thread {

    public static final void main(String[] args) throws Exception {
        try {
            ServerSocket server = new ServerSocket();
            InetAddress loopback = InetAddress.getLoopbackAddress();
            InetSocketAddress address = new InetSocketAddress(loopback, 0);
            server.bind(address);
            int port = server.getLocalPort();
            System.out.println("listening on " + port);
            B6890349 t = new B6890349(server);
            t.start();
            URL u = new URL("http", InetAddress.getLoopbackAddress().getHostAddress(), port, "/foo\nbar");
            System.out.println("URL: " + u);
            HttpURLConnection urlc = (HttpURLConnection) u.openConnection(Proxy.NO_PROXY);
            InputStream is = urlc.getInputStream();
            throw new RuntimeException("Test failed");
        } catch (IOException e) {
            System.out.println("Caught expected exception: " + e);
        }
    }

    ServerSocket server;

    B6890349(ServerSocket server) {
        this.server = server;
    }

    String resp = "HTTP/1.1 200 Ok\r\nContent-length: 0\r\n\r\n";

    public void run() {
        try {
            Socket s = server.accept();
            OutputStream os = s.getOutputStream();
            os.write(resp.getBytes());
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
