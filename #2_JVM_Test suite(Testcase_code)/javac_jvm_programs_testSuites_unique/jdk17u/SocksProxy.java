import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.function.Consumer;
import javax.net.ServerSocketFactory;

class SocksProxy implements Runnable, AutoCloseable {

    private ServerSocket server;

    private Consumer<Socket> socketConsumer;

    private SocksProxy(ServerSocket server, Consumer<Socket> socketConsumer) {
        this.server = server;
        this.socketConsumer = socketConsumer;
    }

    static SocksProxy startProxy(Consumer<Socket> socketConsumer) throws IOException {
        Objects.requireNonNull(socketConsumer, "socketConsumer cannot be null");
        ServerSocket server = ServerSocketFactory.getDefault().createServerSocket(0);
        System.setProperty("socksProxyHost", InetAddress.getLoopbackAddress().getHostAddress());
        System.setProperty("socksProxyPort", String.valueOf(server.getLocalPort()));
        System.setProperty("socksProxyVersion", "5");
        SocksProxy proxy = new SocksProxy(server, socketConsumer);
        Thread proxyThread = new Thread(proxy, "Proxy");
        proxyThread.setDaemon(true);
        proxyThread.start();
        return proxy;
    }

    @Override
    public void run() {
        while (!server.isClosed()) {
            try (Socket socket = server.accept()) {
                System.out.println("Server: accepted connection");
                if (socketConsumer != null) {
                    socketConsumer.accept(socket);
                }
            } catch (IOException e) {
                if (!server.isClosed()) {
                    throw new RuntimeException("Server: accept connection failed", e);
                } else {
                    System.out.println("Server is closed.");
                }
            }
        }
    }

    @Override
    public void close() throws Exception {
        if (!server.isClosed()) {
            server.close();
        }
    }

    int getPort() {
        return server.getLocalPort();
    }
}
