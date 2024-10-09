

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.*;


public class B8025710 {

    private final static AtomicBoolean connectInServer = new AtomicBoolean();
    private static final String keystorefile =
            System.getProperty("test.src", "./")
            + "/../../../../../javax/net/ssl/etc/keystore";
    private static final String passphrase = "passphrase";

    public static void main(String[] args) throws Exception {
        new B8025710().runTest();

        if (connectInServer.get())
            throw new RuntimeException("TEST FAILED: server got proxy header");
        else
            System.out.println("TEST PASSED");
    }

    private void runTest() throws Exception {
        ProxyServer proxyServer = new ProxyServer();
        HttpServer httpServer = new HttpServer();
        httpServer.start();
        proxyServer.start();

        URL url = new URL("https", InetAddress.getLocalHost().getHostName(),
                httpServer.getPort(), "/");

        Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyServer.getAddress());

        HttpsURLConnection.setDefaultSSLSocketFactory(createTestSSLSocketFactory());

        
        for (int i = 0; i < 2; i++) {
            System.out.println("Client: Requesting " + url.toExternalForm()
                    + " via " + proxy.toString()
                    + " (attempt " + (i + 1) + " of 2)");

            HttpsURLConnection connection =
                    (HttpsURLConnection) url.openConnection(proxy);

            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("User-Agent", "Test/1.0");
            connection.getOutputStream().write("Hello, world!".getBytes("UTF-8"));

            if (connection.getResponseCode() != 200) {
                System.err.println("Client: Unexpected response code "
                        + connection.getResponseCode());
                break;
            }

            String response = readLine(connection.getInputStream());
            if (!"Hi!".equals(response)) {
                System.err.println("Client: Unexpected response body: "
                        + response);
            }
        }
        httpServer.close();
        proxyServer.close();
        httpServer.join();
        proxyServer.join();
    }

    class ProxyServer extends Thread implements Closeable {

        private final ServerSocket proxySocket;
        private final Pattern connectLinePattern =
                Pattern.compile("^CONNECT ([^: ]+):([0-9]+) HTTP/[0-9.]+$");
        private final String PROXY_RESPONSE =
                "HTTP/1.0 200 Connection Established\r\n"
                + "Proxy-Agent: TestProxy/1.0\r\n"
                + "\r\n";

        ProxyServer() throws Exception {
            super("ProxyServer Thread");

            
            proxySocket = ServerSocketFactory.getDefault().createServerSocket();
            proxySocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 0));
        }

        public SocketAddress getAddress() { return  proxySocket.getLocalSocketAddress(); }

        @Override
        public void close() throws IOException {
            proxySocket.close();
        }

        @Override
        public void run() {
            ArrayList<Thread> threads = new ArrayList<>();
            int connectionCount = 0;
            try {
                while (connectionCount++ < 2) {
                    final Socket clientSocket = proxySocket.accept();
                    final int proxyConnectionCount = connectionCount;
                    System.out.println("Proxy: NEW CONNECTION "
                            + proxyConnectionCount);

                    Thread t = new Thread("ProxySocket" + proxyConnectionCount) {
                        @Override
                        public void run() {
                            try {
                                String firstLine =
                                        readHeader(clientSocket.getInputStream());

                                Matcher connectLineMatcher =
                                        connectLinePattern.matcher(firstLine);
                                if (!connectLineMatcher.matches()) {
                                    System.out.println("Proxy: Unexpected"
                                            + " request to the proxy: "
                                            + firstLine);
                                    return;
                                }

                                String host    = connectLineMatcher.group(1);
                                String portStr = connectLineMatcher.group(2);
                                int port       = Integer.parseInt(portStr);

                                Socket serverSocket = SocketFactory.getDefault()
                                        .createSocket(host, port);

                                clientSocket.getOutputStream()
                                        .write(PROXY_RESPONSE.getBytes("UTF-8"));

                                ProxyTunnel copyToClient =
                                        new ProxyTunnel(serverSocket, clientSocket);
                                ProxyTunnel copyToServer =
                                        new ProxyTunnel(clientSocket, serverSocket);

                                copyToClient.start();
                                copyToServer.start();

                                copyToClient.join();
                                
                                
                                
                                
                                clientSocket.shutdownOutput();

                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException ignored) { }

                                
                                copyToServer.close();
                                copyToClient.close();
                            } catch (IOException | NumberFormatException
                                    | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    threads.add(t);
                    t.start();
                }
                for (Thread t: threads)
                    t.join();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    
    class ProxyTunnel extends Thread {
        private final Socket sockIn;
        private final Socket sockOut;
        private final InputStream input;
        private final OutputStream output;

        public ProxyTunnel(Socket sockIn, Socket sockOut) throws IOException {
            super("ProxyTunnel");
            this.sockIn  = sockIn;
            this.sockOut = sockOut;
            input  = sockIn.getInputStream();
            output = sockOut.getOutputStream();
        }

        public void run() {
            byte[] buf = new byte[8192];
            int bytesRead;

            try {
                while ((bytesRead = input.read(buf)) >= 0) {
                    output.write(buf, 0, bytesRead);
                    output.flush();
                }
            } catch (IOException ignored) {
                close();
            }
        }

        public void close() {
            try {
                if (!sockIn.isClosed())
                    sockIn.close();
                if (!sockOut.isClosed())
                    sockOut.close();
            } catch (IOException ignored) { }
        }
    }

    
    class HttpServer extends Thread implements Closeable {

        private final ServerSocket serverSocket;
        private final SSLSocketFactory sslSocketFactory;
        private final String serverResponse =
                "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/plain\r\n"
                + "Content-Length: 3\r\n"
                + "\r\n"
                + "Hi!";
        private int connectionCount = 0;

        HttpServer() throws Exception {
            super("HttpServer Thread");

            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(keystorefile), passphrase.toCharArray());
            KeyManagerFactory factory = KeyManagerFactory.getInstance("SunX509");
            factory.init(ks, passphrase.toCharArray());
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(factory.getKeyManagers(), null, null);

            sslSocketFactory = ctx.getSocketFactory();

            
            serverSocket = ServerSocketFactory.getDefault().createServerSocket();
            serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 0));
        }

        public int getPort() { return  serverSocket.getLocalPort(); }

        @Override
        public void close() throws IOException { serverSocket.close(); }

        @Override
        public void run() {
            try {
                while (connectionCount++ < 2) {
                    Socket socket = serverSocket.accept();
                    System.out.println("Server: NEW CONNECTION "
                            + connectionCount);

                    SSLSocket sslSocket = (SSLSocket) sslSocketFactory
                            .createSocket(socket,null, getPort(), false);
                    sslSocket.setUseClientMode(false);
                    sslSocket.startHandshake();

                    String firstLine = readHeader(sslSocket.getInputStream());
                    if (firstLine != null && firstLine.contains("CONNECT")) {
                        System.out.println("Server: BUG! HTTP CONNECT"
                                + " encountered: " + firstLine);
                        connectInServer.set(true);
                    }

                    
                    
                    OutputStream out = sslSocket.getOutputStream();
                    out.write(serverResponse.getBytes("UTF-8"));
                    socket.shutdownOutput();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    
    private static String readHeader(InputStream inputStream)
            throws IOException {
        String line;
        String firstLine = null;
        while ((line = readLine(inputStream)) != null && line.length() > 0) {
            if (firstLine == null) {
                firstLine = line;
            }
        }

        return firstLine;
    }

    
    private static String readLine(InputStream inputStream)
            throws IOException {
        final StringBuilder line = new StringBuilder();
        int ch;
        while ((ch = inputStream.read()) != -1) {
            if (ch == '\r') {
                continue;
            }

            if (ch == '\n') {
                break;
            }

            line.append((char) ch);
        }

        return line.toString();
    }

    private SSLSocketFactory createTestSSLSocketFactory() {

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession sslSession) {
                
                return true;
            }
        });

        
        
        final TrustManager[] trustAllCertChains = new TrustManager[] {
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs,
                        String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs,
                        String authType) {
                }
            }
        };

        final SSLContext sc;
        try {
            sc = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        try {
            sc.init(null, trustAllCertChains, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }

        return sc.getSocketFactory();
    }
}
