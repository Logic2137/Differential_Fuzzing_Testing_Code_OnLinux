






import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


public class LargePacketAfterHandshakeTest {
    static String pathToStores = "../../../../javax/net/ssl/etc";
    static String keyStoreFile = "keystore";
    static String trustStoreFile = "truststore";
    static String passwd = "passphrase";

    volatile static int serverport = -1;
    volatile static boolean serverReady = false;
    volatile static boolean clientDone = false;
    volatile static Exception serverException = null;

    public static void runServer() {
        try {
            System.out.println("Server: Started server thread.");
            SSLServerSocketFactory ssf =
                (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
            SSLServerSocket s = (SSLServerSocket)ssf.createServerSocket(0);
            serverport = s.getLocalPort();
            System.out.println("Server: Started, listening on port " +
                    serverport + ".");
            serverReady = true;
            SSLSocket c = (SSLSocket)s.accept();
            s.close();
            System.out.println(
                "Server: Accepted client connection and closed server socket.");
            BufferedReader r = new BufferedReader(
                    new InputStreamReader(c.getInputStream()));
            BufferedWriter w = new BufferedWriter(
                    new OutputStreamWriter(c.getOutputStream()));
            String echostring = r.readLine();
            System.out.println("Server: Read " + echostring.length() +
                    " chars of input data.");
            c.startHandshake();
            System.out.println("Server: Kicked new handshake.");
            w.write(echostring);
            w.newLine();
            w.flush();
            System.out.println("Server: Echoed " + echostring.length() +
                    " chars of input data.");
            while (!clientDone) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("Server: Caught InterruptedException.");
                }
            }
            r.close();
            w.close();
            c.close();
            System.out.println(
                    "Server: Closed streams and client socket, exiting.");
        } catch (Exception e) {
            System.out.println("Server: Caught Exception.");
            e.printStackTrace();
            serverReady = true;
            serverException = e;
        }
    }

    public static void runClient() throws IOException {
        try {
            SSLSocketFactory f =
                    (SSLSocketFactory)SSLSocketFactory.getDefault();
            System.out.println("Client: Initialized.");
            while (!serverReady) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("Client: Caught InterruptedException.");
                }
            }
            SSLSocket c = (SSLSocket)f.createSocket("localhost", serverport);
            BufferedWriter w = new BufferedWriter(
                    new OutputStreamWriter(c.getOutputStream()));
            BufferedReader r = new BufferedReader(
                    new InputStreamReader(c.getInputStream()));
            System.out.println("Client: Connected.");
            String echoPattern = "Otto";
            StringBuilder echoBuilder =
                    new StringBuilder(4500 + echoPattern.length());
            while (echoBuilder.length() < 4500) {
                echoBuilder.append(echoPattern);
            }
            String echostring = echoBuilder.toString();
            w.write(echostring);
            w.newLine();
            w.flush();
            System.out.println("Client: Sent " + echostring.length() +
                    " chars of data.");
            String echoresponse = r.readLine();
            clientDone = true;
            System.out.println("Client: Read " + echoresponse.length() +
                    " chars of data.");
            w.close();
            r.close();
            c.close();
            System.out.println("Client: Closed streams and socket, exiting.");
        } catch (IOException e) {
            System.out.println("Client: Caught Exception.");
            e.printStackTrace();
            clientDone = true;
            throw e;
        }
    }

    public static void main(String[] args) throws Exception {
        String keyFilename = System.getProperty("test.src", "./") + "/" +
                pathToStores + "/" + keyStoreFile;
        String trustFilename = System.getProperty("test.src", "./") + "/" +
                pathToStores + "/" + trustStoreFile;

        System.setProperty("javax.net.ssl.keyStore", keyFilename);
        System.setProperty("javax.net.ssl.keyStorePassword", passwd);
        System.setProperty("javax.net.ssl.trustStore", trustFilename);
        System.setProperty("javax.net.ssl.trustStorePassword", passwd);

        Thread serverThread = new Thread() {
            @Override
            public void run() {
                runServer();
            }
        };
        serverThread.start();
        runClient();
        while (serverThread.isAlive()) {
            try {
                serverThread.join();
            } catch (InterruptedException e) {
                System.out.println("Main: Caught InterruptedException " +
                        " waiting for server Thread.");
            }
        }
        if (serverException != null) {
            throw serverException;
        }
    }
}
