import com.sun.net.httpserver.*;
import java.util.concurrent.*;
import java.io.*;
import java.net.*;

public class B6433018 {

    static final String CRLF = "\r\n";

    static final String cmd = "POST /test/item HTTP/1.1" + CRLF + "Keep-Alive: 300" + CRLF + "Proxy-Connection: keep-alive" + CRLF + "Content-Type: text/xml" + CRLF + "Content-Length: 22" + CRLF + "Pragma: no-cache" + CRLF + "Cache-Control: no-cache" + CRLF + CRLF + "<item desc=\"excuse\" />" + CRLF + "GET /test/items HTTP/1.1" + CRLF + "Host: araku:9999" + CRLF + "Accept-Language: en-us,en;q=0.5" + CRLF + "Accept-Encoding: gzip,deflate" + CRLF + "Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7" + CRLF + "Keep-Alive: 300" + CRLF + "Proxy-Connection: keep-alive" + CRLF + "Pragma: no-cache" + CRLF + "Cache-Control: no-cache" + CRLF + CRLF;

    public static void main(String[] args) throws Exception {
        CountDownLatch finished = new CountDownLatch(2);
        Handler handler = new Handler(finished);
        InetAddress loopback = InetAddress.getLoopbackAddress();
        InetSocketAddress addr = new InetSocketAddress(loopback, 0);
        HttpServer server = HttpServer.create(addr, 0);
        HttpContext ctx = server.createContext("/test", handler);
        server.start();
        int port = server.getAddress().getPort();
        try (Socket s = new Socket(loopback, port);
            OutputStream os = s.getOutputStream()) {
            os.write(cmd.getBytes());
            finished.await(30, TimeUnit.SECONDS);
        } finally {
            server.stop(2);
        }
        if (finished.getCount() != 0)
            throw new RuntimeException("did not receive the 2 requests");
        System.out.println("OK");
    }

    static class Handler implements HttpHandler {

        private final CountDownLatch finished;

        Handler(CountDownLatch finished) {
            this.finished = finished;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            try (InputStream is = t.getRequestBody()) {
                Headers map = t.getRequestHeaders();
                Headers rmap = t.getResponseHeaders();
                while (is.read() != -1) ;
            }
            t.sendResponseHeaders(200, -1);
            t.close();
            finished.countDown();
        }
    }
}
