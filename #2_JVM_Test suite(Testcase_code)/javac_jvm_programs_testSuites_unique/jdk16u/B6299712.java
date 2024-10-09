



import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.*;
import java.io.*;
import java.util.*;


public class B6299712 {
    static HttpServer server;

    public static void main(String[] args) throws Exception {
        ResponseCache.setDefault(new DeployCacheHandler());
        ProxySelector.setDefault(ProxySelector.of(null)); 
        startHttpServer();

        makeHttpCall();
    }

    public static void startHttpServer() throws IOException {
        InetAddress address = InetAddress.getLocalHost();
        server = HttpServer.create(new InetSocketAddress(address, 0), 0);
        server.createContext("/", new DefaultHandler());
        server.createContext("/redirect", new RedirectHandler());
        server.start();
    }

    public static void makeHttpCall() throws IOException {
        try {
            System.out.println("http server listen on: "
                    + server.getAddress().getPort());
            URL url = new URL("http",
                               InetAddress.getLocalHost().getHostAddress(),
                               server.getAddress().getPort(), "/");
            HttpURLConnection uc = (HttpURLConnection)url.openConnection();
            if (uc.getResponseCode() != 200)
                throw new RuntimeException("Expected Response Code was 200,"
                        + "received: " + uc.getResponseCode());
            uc.disconnect();
        } finally {
            server.stop(0);
        }
    }

    static class RedirectHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.sendResponseHeaders(200, -1);
            exchange.close();
        }

    }

    static class DefaultHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Location", "/redirect");
            exchange.sendResponseHeaders(302, -1);
            exchange.close();
        }

    }

    static class DeployCacheHandler extends java.net.ResponseCache {

        public synchronized CacheResponse get(final URI uri, String rqstMethod,
                Map<String, List<String>> requestHeaders) throws IOException
        {
            System.out.println("get!!!: " + uri);
            if (!uri.toString().endsWith("redirect")) {
                return null;
            }
            System.out.println("Serving request from cache");
            return new DeployCacheResponse(new EmptyInputStream(),
                                           new HashMap<String, List<String>>());
        }

        public synchronized CacheRequest put(URI uri, URLConnection conn)
            throws IOException
        {
            URL url = uri.toURL();
            return new DeployCacheRequest(url, conn);

        }
    }

    static class DeployCacheRequest extends java.net.CacheRequest {

        private URL _url;
        private URLConnection _conn;

        DeployCacheRequest(URL url, URLConnection conn) {
            _url = url;
            _conn = conn;
        }

        public void abort() {

        }

        public OutputStream getBody() throws IOException {

            return null;
        }
    }

    static class DeployCacheResponse extends java.net.CacheResponse {
        protected InputStream is;
        protected Map<String, List<String>> headers;

        DeployCacheResponse(InputStream is, Map<String, List<String>> headers) {
            this.is = is;
            this.headers = headers;
        }

        public InputStream getBody() throws IOException {
            return is;
        }

        public Map<String, List<String>> getHeaders() throws IOException {
            List<String> val = new ArrayList<>();
            val.add("HTTP/1.1 200 OK");
            headers.put(null, val);
            return headers;
        }
    }

    static class EmptyInputStream extends InputStream {

        public int read() throws IOException {
            return -1;
        }
    }
}
