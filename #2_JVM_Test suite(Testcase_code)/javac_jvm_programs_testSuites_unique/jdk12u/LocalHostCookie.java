


import com.sun.net.httpserver.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;


public class LocalHostCookie {

    public static void main(String[] args) throws Exception {
        new LocalHostCookie().runTest();
    }

    public void runTest() throws Exception {
        Server s = null;
        try {
            s = new Server();
            s.startServer();
            URL url = new URL("http","localhost", s.getPort(), "/");
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            urlConnection.getInputStream();

            CookieHandler cookieHandler = CookieHandler.getDefault();
            if (cookieHandler == null) {
                cookieHandler = new java.net.CookieManager();
                CookieHandler.setDefault(cookieHandler);
            }
            cookieHandler.put(urlConnection.getURL().toURI(),
                    urlConnection.getHeaderFields());
            Map<String, List<String>> map =
                    cookieHandler.get(urlConnection.getURL().toURI(),
                    urlConnection.getHeaderFields());
            if (map.containsKey("Cookie")) {
                List<String> list = map.get("Cookie");
                
                if (list == null || list.size() ==  0) {
                    throw new RuntimeException("Test failed!");
                }
            }
        } finally {
            if (s != null) {
                s.stopServer();
            }
        }
    }

    class Server {
        HttpServer server;

        public void startServer() {
            InetSocketAddress addr = new InetSocketAddress(0);
            try {
                server = HttpServer.create(addr, 0);
            } catch (IOException ioe) {
                throw new RuntimeException("Server could not be created");
            }

            server.createContext("/", new MyCookieHandler());
            server.start();
        }

        public int getPort() {
            return server.getAddress().getPort();
        }

        public void stopServer() {
            if (server != null) {
                server.stop(0);
            }
        }
    }

    class MyCookieHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("GET")){
                Headers responseHeaders = exchange.getResponseHeaders();
                responseHeaders.set("Content-Type", "text/plain");
                responseHeaders.set("Date", "June 13th 2012");
                
                responseHeaders.set("Set-Cookie2", "name=value");
                exchange.sendResponseHeaders(200, 0);
                OutputStream os = exchange.getResponseBody();
                String str = "This is what the server sent!";
                os.write(str.getBytes());
                os.flush();
                os.close();
            }
        }
    }
}

