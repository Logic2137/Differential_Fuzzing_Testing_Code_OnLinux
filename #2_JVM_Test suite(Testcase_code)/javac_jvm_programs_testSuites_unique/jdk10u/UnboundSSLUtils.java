

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIMatcher;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;


class UnboundSSLUtils {

    static final String KTAB_FILENAME = "krb5.keytab.data";
    static final String HOST = "localhost";
    static final String REALM = "TEST.REALM";
    static final String KRBTGT_PRINCIPAL = "krbtgt/" + REALM;
    static final String TEST_SRC = System.getProperty("test.src", ".");
    static final String TLS_KRB5_FILTER = "TLS_KRB5";
    static final String USER = "USER";
    static final String USER_PASSWORD = "password";
    static final String FS = System.getProperty("file.separator");
    static final String SNI_PATTERN = ".*";
    static final String USER_PRINCIPAL = USER + "@" + REALM;
    static final String KRB5_CONF_FILENAME = "krb5.conf";
    static final int DELAY = 1000;

   static String[] filterStringArray(String[] src, String filter) {
        return Arrays.stream(src).filter((item) -> item.startsWith(filter))
                .toArray(size -> new String[size]);
    }

    
    static void startServerWithJaas(final SSLEchoServer server,
            String config) throws LoginException, PrivilegedActionException {
        LoginContext context = new LoginContext(config);
        context.login();
        System.out.println("Server: successful authentication");
        Subject.doAs(context.getSubject(),
                (PrivilegedExceptionAction<Object>) () -> {
            SSLEchoServer.startServer(server);
            return null;
        });
    }

}

class SSLClient {

    private final static byte[][] arrays = {
        new byte[] {-1, 0, 2},
        new byte[] {}
    };

    private final SSLSocket socket;

    private SSLClient(SSLSocket socket) {
        this.socket = socket;
    }

    void connect() throws IOException {
        System.out.println("Client: connect to server");
        try (BufferedInputStream bis = new BufferedInputStream(
                        socket.getInputStream());
                BufferedOutputStream bos = new BufferedOutputStream(
                        socket.getOutputStream())) {

            for (byte[] bytes : arrays) {
                System.out.println("Client: send byte array: "
                        + Arrays.toString(bytes));

                bos.write(bytes);
                bos.flush();

                byte[] recieved = new byte[bytes.length];
                int read = bis.read(recieved, 0, bytes.length);
                if (read < 0) {
                    throw new IOException("Client: couldn't read a response");
                }

                System.out.println("Client: recieved byte array: "
                        + Arrays.toString(recieved));

                if (!Arrays.equals(bytes, recieved)) {
                    throw new IOException("Client: sent byte array "
                                + "is not equal with recieved byte array");
                }
            }
            socket.getSession().invalidate();
        } finally {
            if (!socket.isClosed()) {
                socket.close();
            }
        }
    }

    static SSLClient init(String host, int port, String cipherSuiteFilter,
            String sniHostName) throws NoSuchAlgorithmException, IOException {
        SSLContext sslContext = SSLContext.getDefault();
        SSLSocketFactory ssf = (SSLSocketFactory) sslContext.getSocketFactory();
        SSLSocket socket = (SSLSocket) ssf.createSocket(host, port);
        SSLParameters params = new SSLParameters();

        if (cipherSuiteFilter != null) {
            String[] cipherSuites = UnboundSSLUtils.filterStringArray(
                    ssf.getSupportedCipherSuites(), cipherSuiteFilter);
            System.out.println("Client: enabled cipher suites: "
                    + Arrays.toString(cipherSuites));
            params.setCipherSuites(cipherSuites);
        }

        if (sniHostName != null) {
            System.out.println("Client: set SNI hostname: " + sniHostName);
            SNIHostName serverName = new SNIHostName(sniHostName);
            List<SNIServerName> serverNames = new ArrayList<>();
            serverNames.add(serverName);
            params.setServerNames(serverNames);
        }

        socket.setSSLParameters(params);

        return new SSLClient(socket);
    }

}

class SSLEchoServer implements Runnable, AutoCloseable {

    private final SSLServerSocket ssocket;
    private volatile boolean stopped = false;
    private volatile boolean ready = false;

    
    static void startServer(SSLEchoServer server) {
        Thread serverThread = new Thread(server, "SSL echo server thread");
        serverThread.setDaemon(true);
        serverThread.start();
    }

    private SSLEchoServer(SSLServerSocket ssocket) {
        this.ssocket = ssocket;
    }

    
    @Override
    public void run() {
        System.out.println("Server: started");
        while (!stopped) {
            ready = true;
            try (SSLSocket socket = (SSLSocket) ssocket.accept()) {
                System.out.println("Server: client connection accepted");
                try (
                    BufferedInputStream bis = new BufferedInputStream(
                            socket.getInputStream());
                    BufferedOutputStream bos = new BufferedOutputStream(
                            socket.getOutputStream())
                ) {
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = bis.read(buffer)) > 0) {
                        bos.write(buffer, 0, read);
                        System.out.println("Server: recieved " + read
                                + " bytes: "
                                + Arrays.toString(Arrays.copyOf(buffer, read)));
                        bos.flush();
                    }
                }
            } catch (IOException e) {
                if (stopped) {
                    
                    
                    break;
                }
                System.out.println("Server: couldn't accept client connection: "
                        + e);
            }
        }
        System.out.println("Server: finished");
    }

    boolean isReady() {
        return ready;
    }

    void stop() {
        stopped = true;
        ready = false;

        
        try {
            if (!ssocket.isClosed()) {
                ssocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Unexpected exception: " + e);
        }
    }

    @Override
    public void close() {
        stop();
    }

    int getPort() {
        return ssocket.getLocalPort();
    }

    
    static SSLEchoServer init(String cipherSuiteFilter,
            String sniPattern) throws NoSuchAlgorithmException, IOException {
        SSLContext context = SSLContext.getDefault();
        SSLServerSocketFactory ssf =
                (SSLServerSocketFactory) context.getServerSocketFactory();
        SSLServerSocket ssocket =
                (SSLServerSocket) ssf.createServerSocket(0);

        
        if (cipherSuiteFilter != null) {
            String[] ciphersuites = UnboundSSLUtils.filterStringArray(
                    ssf.getSupportedCipherSuites(), cipherSuiteFilter);
            System.out.println("Server: enabled cipher suites: "
                    + Arrays.toString(ciphersuites));
            ssocket.setEnabledCipherSuites(ciphersuites);
        }

        
        if (sniPattern != null) {
            System.out.println("Server: set SNI matcher: " + sniPattern);
            SNIMatcher matcher = SNIHostName.createSNIMatcher(sniPattern);
            List<SNIMatcher> matchers = new ArrayList<>();
            matchers.add(matcher);
            SSLParameters params = ssocket.getSSLParameters();
            params.setSNIMatchers(matchers);
            ssocket.setSSLParameters(params);
        }

        return new SSLEchoServer(ssocket);
    }

}

