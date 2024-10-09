



import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Proxy;

public class SocksProxyVersion implements Runnable {
    final ServerSocket ss;
    volatile boolean failed;
    volatile boolean stopped = false;
    volatile int expected;

    public static void main(String[] args) throws Exception {
        if (InetAddress.getLocalHost().isLoopbackAddress()) {
            System.out.println("Test cannot run. getLocalHost returns a loopback address");
            return;
        }
        new SocksProxyVersion();
    }

    public SocksProxyVersion() throws Exception {
        ss = new ServerSocket(0);
        int port = ss.getLocalPort();
        Thread serverThread = new Thread(this);
        serverThread.start();
        try (ServerSocket socket = ss) {
            runTest(port);
        } finally {
            stopped = true;
        }

        serverThread.join();
        if (failed) {
            throw new RuntimeException("socksProxyVersion not being set correctly");
        }
    }

    final void runTest(int port) throws Exception {
        
        String addr = InetAddress.getLocalHost().getHostAddress();

        System.setProperty("socksProxyHost", addr);
        System.setProperty("socksProxyPort", Integer.toString(port));

        Proxy proxy = new Proxy(Proxy.Type.SOCKS,
                                new InetSocketAddress(addr, port));

        
        System.setProperty("socksProxyVersion", Integer.toString(4));
        this.expected = 4;
        check(new Socket(), addr, port);
        check(new Socket(proxy), addr, port);

        
        System.setProperty("socksProxyVersion", Integer.toString(5));
        this.expected = 5;
        check(new Socket(), addr, port);
        check(new Socket(proxy), addr, port);
    }

    private void check(Socket socket, String addr, int port)
        throws IOException
    {
        try (Socket s = socket) {
            socket.connect(new InetSocketAddress(addr, port));
        } catch (SocketException e) {
            
            
            
        }
    }

    @Override
    public void run() {
        int count = 0;
        try {
            while (!stopped) {
                try (Socket s = ss.accept()) {
                    int version = (s.getInputStream()).read();
                    if (version != expected) {
                        System.out.printf("Iteration: %d, Got: %d, expected: %d%n",
                                          count, version, expected);
                        failed = true;
                    }
                }
                count++;
            }
        } catch (IOException e) {
            if (!ss.isClosed()) {
                e.printStackTrace();
            }
            
        }
    }
}
