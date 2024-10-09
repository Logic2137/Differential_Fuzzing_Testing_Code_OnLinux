import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Phaser;

public class Streams {

    static final int NUM_THREADS = 100;

    static volatile boolean failed;

    static final Phaser startingGate = new Phaser(NUM_THREADS + 1);

    public static void main(String[] args) throws Exception {
        try (ServerSocket ss = new ServerSocket()) {
            InetAddress loopback = InetAddress.getLoopbackAddress();
            ss.bind(new InetSocketAddress(loopback, 0));
            runTest(OutputStreamGetter.class, ss);
            runTest(InputStreamGetter.class, ss);
        }
        if (failed)
            throw new RuntimeException("Failed, check output");
    }

    static void runTest(Class<? extends StreamGetter> klass, ServerSocket ss) throws Exception {
        final int port = ss.getLocalPort();
        final InetAddress address = ss.getInetAddress();
        Socket[] sockets = new Socket[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            sockets[i] = address.isAnyLocalAddress() ? new Socket("localhost", port) : new Socket(address, port);
            try (Socket socket = ss.accept()) {
            }
        }
        Constructor<? extends StreamGetter> ctr = klass.getConstructor(Socket.class);
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) threads[i] = ctr.newInstance(sockets[i]);
        for (int i = 0; i < NUM_THREADS; i++) threads[i].start();
        startingGate.arriveAndAwaitAdvance();
        for (int i = 0; i < NUM_THREADS; i++) sockets[i].close();
        for (int i = 0; i < NUM_THREADS; i++) threads[i].join();
    }

    static abstract class StreamGetter extends Thread {

        final Socket socket;

        StreamGetter(Socket s) {
            socket = s;
        }

        @Override
        public void run() {
            try {
                startingGate.arriveAndAwaitAdvance();
                getStream();
            } catch (IOException x) {
            } catch (NullPointerException x) {
                x.printStackTrace();
                failed = true;
            }
        }

        abstract void getStream() throws IOException;
    }

    static class InputStreamGetter extends StreamGetter {

        public InputStreamGetter(Socket s) {
            super(s);
        }

        void getStream() throws IOException {
            socket.getInputStream();
        }
    }

    static class OutputStreamGetter extends StreamGetter {

        public OutputStreamGetter(Socket s) {
            super(s);
        }

        void getStream() throws IOException {
            socket.getOutputStream();
        }
    }
}
