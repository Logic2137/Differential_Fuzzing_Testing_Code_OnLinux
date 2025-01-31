



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import sun.net.www.MessageHeader;

public class HttpProxy {
    final String proxyHost;
    final int proxyPort;
    static final int SO_TIMEOUT = 15000;

    public static void main(String[] args) throws Exception {
        String host;
        int port;
        if (args.length == 0) {
            
            ConnectProxyTunnelServer proxy = new ConnectProxyTunnelServer();
            proxy.start();
            host = "localhost";
            port = proxy.getLocalPort();
            out.println("Running with internal proxy: " + host + ":" + port);
        } else if (args.length == 2) {
            host = args[0];
            port = Integer.valueOf(args[1]);
            out.println("Running against specified proxy server: " + host + ":" + port);
        } else {
            System.err.println("Usage: java HttpProxy [<proxy host> <proxy port>]");
            return;
        }

        HttpProxy p = new HttpProxy(host, port);
        p.test();
    }

    public HttpProxy(String proxyHost, int proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    void test() throws Exception {
        InetSocketAddress proxyAddress = new InetSocketAddress(proxyHost, proxyPort);
        Proxy httpProxy = new Proxy(Proxy.Type.HTTP, proxyAddress);

        try (ServerSocket ss = new ServerSocket(0);
             Socket sock = new Socket(httpProxy)) {
            sock.setSoTimeout(SO_TIMEOUT);
            sock.setTcpNoDelay(false);

            InetSocketAddress externalAddress =
                new InetSocketAddress(InetAddress.getLocalHost(), ss.getLocalPort());

            out.println("Trying to connect to server socket on " + externalAddress);
            sock.connect(externalAddress);
            try (Socket externalSock = ss.accept()) {
                
                check(sock.isBound(), "Socket is not bound");
                check(sock.isConnected(), "Socket is not connected");
                check(!sock.isClosed(), "Socket should not be closed");
                check(sock.getSoTimeout() == SO_TIMEOUT,
                        "Socket should have a previously set timeout");
                check(sock.getTcpNoDelay() ==  false, "NODELAY should be false");

                simpleDataExchange(sock, externalSock);
            }
        }
    }

    static void check(boolean condition, String message) {
        if (!condition) out.println(message);
    }

    static Exception unexpected(Exception e) {
        out.println("Unexcepted Exception: " + e);
        e.printStackTrace();
        return e;
    }

    
    
    void simpleDataExchange(Socket s1, Socket s2) throws Exception {
        try (final InputStream i1 = s1.getInputStream();
             final InputStream i2 = s2.getInputStream();
             final OutputStream o1 = s1.getOutputStream();
             final OutputStream o2 = s2.getOutputStream()) {
            startSimpleWriter("simpleWriter1", o1, 100);
            startSimpleWriter("simpleWriter2", o2, 200);
            simpleRead(i2, 100);
            simpleRead(i1, 200);
        }
    }

    void startSimpleWriter(String threadName, final OutputStream os, final int start) {
        (new Thread(new Runnable() {
            public void run() {
                try { simpleWrite(os, start); }
                catch (Exception e) {unexpected(e); }
            }}, threadName)).start();
    }

    void simpleWrite(OutputStream os, int start) throws Exception {
        byte b[] = new byte[2];
        for (int i=start; i<start+100; i++) {
            b[0] = (byte) (i / 256);
            b[1] = (byte) (i % 256);
            os.write(b);
        }
    }

    void simpleRead(InputStream is, int start) throws Exception {
        byte b[] = new byte [2];
        for (int i=start; i<start+100; i++) {
            int x = is.read(b);
            if (x == 1)
                x += is.read(b,1,1);
            if (x!=2)
                throw new Exception("read error");
            int r = bytes(b[0], b[1]);
            if (r != i)
                throw new Exception("read " + r + " expected " +i);
        }
    }

    int bytes(byte b1, byte b2) {
        int i1 = (int)b1 & 0xFF;
        int i2 = (int)b2 & 0xFF;
        return i1 * 256 + i2;
    }

    static class ConnectProxyTunnelServer extends Thread {

        private final ServerSocket ss;

        public ConnectProxyTunnelServer() throws IOException {
            ss = new ServerSocket(0);
        }

        @Override
        public void run() {
            try (Socket clientSocket = ss.accept()) {
                processRequest(clientSocket);
            } catch (Exception e) {
                out.println("Proxy Failed: " + e);
                e.printStackTrace();
            } finally {
                try { ss.close(); } catch (IOException x) { unexpected(x); }
            }
        }

        
        public int getLocalPort() {
            return ss.getLocalPort();
        }

        
        private void processRequest(Socket clientSocket) throws Exception {
            MessageHeader mheader = new MessageHeader(clientSocket.getInputStream());
            String statusLine = mheader.getValue(0);

            if (!statusLine.startsWith("CONNECT")) {
                out.println("proxy server: processes only "
                                  + "CONNECT method requests, recieved: "
                                  + statusLine);
                return;
            }

            
            InetSocketAddress serverAddr = getConnectInfo(statusLine);

            
            try (Socket serverSocket = new Socket(serverAddr.getAddress(),
                                                  serverAddr.getPort())) {
                Forwarder clientFW = new Forwarder(clientSocket.getInputStream(),
                                                   serverSocket.getOutputStream());
                Thread clientForwarderThread = new Thread(clientFW, "ClientForwarder");
                clientForwarderThread.start();
                send200(clientSocket);
                Forwarder serverFW = new Forwarder(serverSocket.getInputStream(),
                                                   clientSocket.getOutputStream());
                serverFW.run();
                clientForwarderThread.join();
            }
        }

        private void send200(Socket clientSocket) throws IOException {
            OutputStream out = clientSocket.getOutputStream();
            PrintWriter pout = new PrintWriter(out);

            pout.println("HTTP/1.1 200 OK");
            pout.println();
            pout.flush();
        }

        
        static InetSocketAddress getConnectInfo(String connectStr)
            throws Exception
        {
            try {
                int starti = connectStr.indexOf(' ');
                int endi = connectStr.lastIndexOf(' ');
                String connectInfo = connectStr.substring(starti+1, endi).trim();
                
                endi = connectInfo.indexOf(':');
                String name = connectInfo.substring(0, endi);
                int port = Integer.parseInt(connectInfo.substring(endi+1));
                return new InetSocketAddress(name, port);
            } catch (Exception e) {
                out.println("Proxy recieved a request: " + connectStr);
                throw unexpected(e);
            }
        }
    }

    
    static class Forwarder implements Runnable
    {
        private final InputStream in;
        private final OutputStream os;

        Forwarder(InputStream in, OutputStream os) {
            this.in = in;
            this.os = os;
        }

        @Override
        public void run() {
            try {
                byte[] ba = new byte[1024];
                int count;
                while ((count = in.read(ba)) != -1) {
                    os.write(ba, 0, count);
                }
            } catch (IOException e) {
                unexpected(e);
            }
        }
    }
}
