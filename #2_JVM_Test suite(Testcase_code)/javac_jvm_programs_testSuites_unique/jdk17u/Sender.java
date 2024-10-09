import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;

public class Sender {

    static PrintStream log = System.err;

    static volatile SocketAddress clientISA = null;

    public static void main(String[] args) throws Exception {
        test();
    }

    static void test() throws Exception {
        Server server = new Server();
        Client client = new Client(server.port());
        Thread serverThread = new Thread(server);
        serverThread.start();
        Thread clientThread = new Thread(client);
        clientThread.start();
        serverThread.join();
        clientThread.join();
        server.throwException();
        client.throwException();
    }

    public static class Client implements Runnable {

        final int port;

        Exception e = null;

        Client(int port) {
            this.port = port;
        }

        void throwException() throws Exception {
            if (e != null)
                throw e;
        }

        public void run() {
            try {
                DatagramChannel dc = DatagramChannel.open();
                ByteBuffer bb = ByteBuffer.allocateDirect(12);
                bb.order(ByteOrder.BIG_ENDIAN);
                bb.putInt(1).putLong(1);
                bb.flip();
                InetAddress address = InetAddress.getLocalHost();
                InetSocketAddress isa = new InetSocketAddress(address, port);
                dc.connect(isa);
                clientISA = dc.getLocalAddress();
                dc.write(bb);
            } catch (Exception ex) {
                e = ex;
            }
        }
    }

    public static class Server implements Runnable {

        final DatagramChannel dc;

        Exception e = null;

        Server() throws IOException {
            dc = DatagramChannel.open().bind(new InetSocketAddress(0));
        }

        int port() {
            return dc.socket().getLocalPort();
        }

        void throwException() throws Exception {
            if (e != null)
                throw e;
        }

        void showBuffer(String s, ByteBuffer bb) {
            log.println(s);
            bb.rewind();
            for (int i = 0; i < bb.limit(); i++) {
                byte element = bb.get();
                log.print(element);
            }
            log.println();
        }

        public void run() {
            SocketAddress sa = null;
            try {
                ByteBuffer bb = ByteBuffer.allocateDirect(12);
                bb.clear();
                dc.configureBlocking(false);
                while (sa == null) {
                    sa = dc.receive(bb);
                    if (sa != null && clientISA != null && !clientISA.equals(sa)) {
                        log.println("Ignore a possible stray diagram from " + sa);
                        sa = null;
                    }
                }
                showBuffer("Received:", bb);
                sa = null;
                for (int i = 0; i < 100; i++) {
                    bb.clear();
                    sa = dc.receive(bb);
                    if (sa != null)
                        throw new RuntimeException("Test failed");
                }
                dc.close();
            } catch (Exception ex) {
                e = ex;
            }
        }
    }
}
