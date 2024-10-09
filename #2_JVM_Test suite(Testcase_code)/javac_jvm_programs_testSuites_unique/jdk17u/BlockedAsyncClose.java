import javax.net.ssl.*;
import java.io.*;
import java.net.SocketException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BlockedAsyncClose implements Runnable {

    SSLSocket socket;

    SSLServerSocket ss;

    private final CountDownLatch closeCondition = new CountDownLatch(1);

    static String pathToStores = "../../../../javax/net/ssl/etc";

    static String keyStoreFile = "keystore";

    static String trustStoreFile = "truststore";

    static String passwd = "passphrase";

    public static void main(String[] args) throws Exception {
        String keyFilename = System.getProperty("test.src", "./") + "/" + pathToStores + "/" + keyStoreFile;
        String trustFilename = System.getProperty("test.src", "./") + "/" + pathToStores + "/" + trustStoreFile;
        System.setProperty("javax.net.ssl.keyStore", keyFilename);
        System.setProperty("javax.net.ssl.keyStorePassword", passwd);
        System.setProperty("javax.net.ssl.trustStore", trustFilename);
        System.setProperty("javax.net.ssl.trustStorePassword", passwd);
        new BlockedAsyncClose();
    }

    public BlockedAsyncClose() throws Exception {
        SSLServerSocketFactory sslssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        InetAddress loopback = InetAddress.getLoopbackAddress();
        ss = (SSLServerSocket) sslssf.createServerSocket();
        ss.bind(new InetSocketAddress(loopback, 0));
        SSLSocketFactory sslsf = (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = (SSLSocket) sslsf.createSocket(loopback, ss.getLocalPort());
        SSLSocket serverSoc = (SSLSocket) ss.accept();
        ss.close();
        (new Thread(this)).start();
        serverSoc.startHandshake();
        boolean closeIsReady = closeCondition.await(90L, TimeUnit.SECONDS);
        if (!closeIsReady) {
            System.out.println("Ignore, the closure is not ready yet in 90 seconds.");
            return;
        }
        socket.setSoLinger(true, 10);
        System.out.println("Calling Socket.close");
        Thread.sleep(1000);
        socket.close();
        System.out.println("ssl socket get closed");
        System.out.flush();
    }

    public void run() {
        byte[] ba = new byte[1024];
        for (int i = 0; i < ba.length; i++) {
            ba[i] = 0x7A;
        }
        try {
            OutputStream os = socket.getOutputStream();
            int count = 0;
            count += ba.length;
            System.out.println(count + " bytes to be written");
            os.write(ba);
            System.out.println(count + " bytes written");
            closeCondition.countDown();
            while (true) {
                count += ba.length;
                System.out.println(count + " bytes to be written");
                os.write(ba);
                System.out.println(count + " bytes written");
            }
        } catch (SocketException se) {
            System.out.println("interrupted? " + se);
        } catch (Exception e) {
            if (socket.isClosed() || socket.isOutputShutdown()) {
                System.out.println("interrupted, the socket is closed");
            } else {
                throw new RuntimeException("interrupted?", e);
            }
        }
    }
}
