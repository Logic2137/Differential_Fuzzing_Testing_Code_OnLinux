import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

abstract public class TLSBase {

    static String pathToStores = "../etc";

    static String keyStoreFile = "keystore";

    static String trustStoreFile = "truststore";

    static String passwd = "passphrase";

    SSLContext sslContext;

    static int serverPort;

    String name;

    TLSBase() {
        String keyFilename = System.getProperty("test.src", "./") + "/" + pathToStores + "/" + keyStoreFile;
        String trustFilename = System.getProperty("test.src", "./") + "/" + pathToStores + "/" + trustStoreFile;
        System.setProperty("javax.net.ssl.keyStore", keyFilename);
        System.setProperty("javax.net.ssl.keyStorePassword", passwd);
        System.setProperty("javax.net.ssl.trustStore", trustFilename);
        System.setProperty("javax.net.ssl.trustStorePassword", passwd);
    }

    byte[] read(SSLSocket sock) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String s = reader.readLine();
            System.err.println("(read) " + name + ": " + s);
            return s.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void write(SSLSocket sock, byte[] data) {
        try {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
            out.println(new String(data));
            out.flush();
            System.err.println("(write)" + name + ": " + new String(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Server extends TLSBase {

        SSLServerSocketFactory fac;

        SSLServerSocket ssock;

        ConcurrentHashMap<Integer, SSLSocket> clientMap = new ConcurrentHashMap<>();

        boolean exit = false;

        Thread t;

        Server() {
            super();
            name = "server";
            try {
                sslContext = SSLContext.getDefault();
                fac = sslContext.getServerSocketFactory();
                ssock = (SSLServerSocket) fac.createServerSocket(0);
                serverPort = ssock.getLocalPort();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
            t = new Thread(() -> {
                try {
                    while (true) {
                        System.err.println("Server ready on port " + serverPort);
                        SSLSocket c = (SSLSocket) ssock.accept();
                        clientMap.put(c.getPort(), c);
                        try {
                            write(c, read(c));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("Server Down");
                    ex.printStackTrace();
                }
            });
            t.start();
        }

        void done() {
            try {
                t.interrupt();
                ssock.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }

        byte[] read(Client client) {
            SSLSocket s = clientMap.get(Integer.valueOf(client.getPort()));
            if (s == null) {
                System.err.println("No socket found, port " + client.getPort());
            }
            return read(s);
        }

        void write(Client client, byte[] data) {
            write(clientMap.get(client.getPort()), data);
        }

        boolean writeRead(Client client, String s) {
            write(client, s.getBytes());
            return (Arrays.compare(s.getBytes(), client.read()) == 0);
        }

        SSLSession getSession(Client c) {
            SSLSocket s = clientMap.get(Integer.valueOf(c.getPort()));
            return s.getSession();
        }

        void close(Client c) throws IOException {
            SSLSocket s = clientMap.get(Integer.valueOf(c.getPort()));
            s.close();
        }
    }

    static class Client extends TLSBase {

        SSLSocket sock;

        Client() {
            super();
            try {
                sslContext = SSLContext.getDefault();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
            connect();
        }

        public SSLSocket connect() {
            try {
                sslContext = SSLContext.getDefault();
                sock = (SSLSocket) sslContext.getSocketFactory().createSocket();
                sock.connect(new InetSocketAddress("localhost", serverPort));
                System.err.println("Client connected using port " + sock.getLocalPort());
                name = "client(" + sock.toString() + ")";
                write("Hello");
                read();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return sock;
        }

        byte[] read() {
            return read(sock);
        }

        void write(byte[] data) {
            write(sock, data);
        }

        void write(String s) {
            write(sock, s.getBytes());
        }

        boolean writeRead(Server server, String s) {
            write(s.getBytes());
            return (Arrays.compare(s.getBytes(), server.read(this)) == 0);
        }

        int getPort() {
            return sock.getLocalPort();
        }

        void close() throws IOException {
            sock.close();
        }
    }
}
