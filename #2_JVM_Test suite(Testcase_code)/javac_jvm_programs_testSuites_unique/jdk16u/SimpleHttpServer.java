

import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


class SimpleHttpServer {
    private static final String userdir = System.getProperty("user.dir", ".");
    private static final Path multirelease = Paths.get(userdir, "multi-release.jar");

    private final HttpServer server;
    private final InetAddress address;

    public SimpleHttpServer() throws IOException {
        this(null);
    }

    public SimpleHttpServer(InetAddress addr) throws IOException {
        address = addr;
        server = HttpServer.create();
    }

    public void start() throws IOException {
        server.bind(new InetSocketAddress(address, 0), 0);
        server.createContext("/multi-release.jar", t -> {
            try (InputStream is = t.getRequestBody()) {
                is.readAllBytes();  
                byte[] bytes = Files.readAllBytes(multirelease);
                t.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = t.getResponseBody()) {
                    os.write(bytes);
                }
            }
        });
        server.setExecutor(null); 
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    int getPort() {
        return server.getAddress().getPort();
    }
}

