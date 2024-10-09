



import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.ServerSocket;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class BadProxySelector {
    public static void main(String[] args) throws Exception {
        ProxySelector.setDefault(new HTTPProxySelector());
        try (ServerSocket ss = new ServerSocket(0);
             Socket s1 = new Socket(ss.getInetAddress(), ss.getLocalPort());
             Socket s2 = ss.accept()) {
        }

       ProxySelector.setDefault(new NullHTTPProxySelector());
        try (ServerSocket ss = new ServerSocket(0);
             Socket s1 = new Socket(ss.getInetAddress(), ss.getLocalPort());
             Socket s2 = ss.accept()) {
        }
    }

    
    private static class HTTPProxySelector extends ProxySelector {
        @Override
        public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {}

        @Override
        public List<Proxy> select(URI uri) {
            List<Proxy> proxies = new ArrayList<>();
            proxies.add(new Proxy(Proxy.Type.HTTP,
                                  new InetSocketAddress("localhost", 0)));
            proxies.add(new Proxy(Proxy.Type.HTTP,
                                  new InetSocketAddress("localhost", 0)));
            return proxies;
        }
    }

    private static class NullHTTPProxySelector extends ProxySelector {
        @Override
        public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {}

        @Override
        public List<Proxy> select(URI uri) {
            List<Proxy> proxies = new ArrayList<>();
            proxies.add(null);
            proxies.add(new Proxy(Proxy.Type.HTTP,
                                  new InetSocketAddress("localhost", 0)));
            return proxies;
        }
    }
}
