import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TruncatedRequestBody {

    static volatile boolean error = false;

    static CountDownLatch latch = new CountDownLatch(2);

    static class Handler implements HttpHandler {

        @Override
        public void handle(HttpExchange exch) throws IOException {
            InputStream is = exch.getRequestBody();
            int c, count = 0;
            byte[] buf = new byte[128];
            try {
                while ((c = is.read(buf)) > 0) count += c;
            } catch (IOException e) {
                System.out.println("Exception caught");
                latch.countDown();
                throw e;
            }
            error = true;
            latch.countDown();
            System.out.println("Read " + count + " bytes");
            is.close();
            exch.sendResponseHeaders(200, -1);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Logger logger = Logger.getLogger("com.sun.net.httpserver");
        ConsoleHandler h = new ConsoleHandler();
        h.setLevel(Level.ALL);
        logger.setLevel(Level.ALL);
        logger.addHandler(h);
        InetSocketAddress addr = new InetSocketAddress(0);
        HttpServer server = HttpServer.create(addr, 10);
        HttpContext ct = server.createContext("/", new Handler());
        ExecutorService ex = Executors.newCachedThreadPool();
        server.setExecutor(ex);
        server.start();
        int port = server.getAddress().getPort();
        Socket sock = new Socket("127.0.0.1", port);
        String s1 = "POST /foo HTTP/1.1\r\nContent-length: 200000\r\n" + "\r\nfoo bar99";
        OutputStream os = sock.getOutputStream();
        os.write(s1.getBytes(StandardCharsets.ISO_8859_1));
        Thread.sleep(500);
        sock.close();
        String s2 = "POST /foo HTTP/1.1\r\nTransfer-encoding: chunked\r\n\r\n" + "100\r\nFoo bar";
        sock = new Socket("127.0.0.1", port);
        os = sock.getOutputStream();
        os.write(s2.getBytes(StandardCharsets.ISO_8859_1));
        Thread.sleep(500);
        sock.close();
        latch.await();
        server.stop(0);
        ex.shutdownNow();
        if (error)
            throw new RuntimeException("Test failed");
    }
}
