



import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.ConnectException;
import java.net.SocketException;
import java.util.concurrent.Phaser;



public class Race {
    final static int THREADS = 100;

    public static void main(String[] args) throws Exception {
        try (ServerSocket ss = new ServerSocket()) {
            InetAddress loopback = InetAddress.getLoopbackAddress();
            ss.bind(new InetSocketAddress(loopback, 0));
            final int port = ss.getLocalPort();
            final Phaser phaser = new Phaser(THREADS + 1);
            for (int i=0; i<100; i++) {
                try {
                    final Socket s = new Socket(loopback, port);
                    s.setSoLinger(false, 0);
                    try (Socket sa = ss.accept()) {
                        sa.setSoLinger(false, 0);
                        final InputStream is = s.getInputStream();
                        Thread[] threads = new Thread[THREADS];
                        for (int j=0; j<THREADS; j++) {
                            threads[j] = new Thread() {
                            public void run() {
                                try {
                                    phaser.arriveAndAwaitAdvance();
                                    while (is.read() != -1)
                                        Thread.sleep(50);
                                } catch (Exception x) {
                                    if (!(x instanceof SocketException
                                          && x.getMessage().equalsIgnoreCase("socket closed")))
                                        x.printStackTrace();
                                    
                                }
                            }};
                        }
                        for (int j=0; j<100; j++)
                            threads[j].start();
                        phaser.arriveAndAwaitAdvance();
                        s.close();
                        for (int j=0; j<100; j++)
                            threads[j].join();
                    }
                } catch (ConnectException e) {
                    System.err.println("Exception " + e + " Port: " + port);
                }
            }
        }
    }
}
