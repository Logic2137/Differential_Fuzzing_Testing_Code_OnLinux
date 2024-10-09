import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.DatagramChannel;

public class EmptyBuffer {

    private static final PrintStream log = System.err;

    public static void main(String[] args) throws Exception {
        test();
    }

    private static void test() throws Exception {
        DatagramChannel dc = DatagramChannel.open();
        InetAddress localHost = InetAddress.getLocalHost();
        dc.bind(new InetSocketAddress(localHost, 0));
        Server server = new Server(dc.getLocalAddress());
        Thread serverThread = new Thread(server);
        serverThread.start();
        try {
            InetSocketAddress isa = new InetSocketAddress(localHost, server.port());
            dc.connect(isa);
            ByteBuffer bb = ByteBuffer.allocateDirect(12);
            bb.order(ByteOrder.BIG_ENDIAN);
            bb.putInt(1).putLong(1);
            bb.flip();
            dc.write(bb);
            bb.rewind();
            dc.write(bb);
            bb.rewind();
            dc.write(bb);
            Thread.sleep(2000);
            serverThread.interrupt();
            server.throwException();
        } finally {
            dc.close();
        }
    }

    private static class Server implements Runnable {

        private final DatagramChannel dc;

        private final SocketAddress clientAddress;

        private Exception e = null;

        Server(SocketAddress clientAddress) throws IOException {
            this.dc = DatagramChannel.open().bind(new InetSocketAddress(0));
            this.clientAddress = clientAddress;
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

        @Override
        public void run() {
            try {
                ByteBuffer bb = ByteBuffer.allocateDirect(12);
                bb.clear();
                int numberReceived = 0;
                while (!Thread.interrupted()) {
                    SocketAddress sa;
                    try {
                        sa = dc.receive(bb);
                    } catch (ClosedByInterruptException cbie) {
                        log.println("Took expected exit");
                        if (numberReceived != 3)
                            throw new RuntimeException("Failed: Too few datagrams");
                        break;
                    }
                    if (sa != null) {
                        log.println("Client: " + sa);
                        if (sa.equals(clientAddress)) {
                            showBuffer("RECV", bb);
                            numberReceived++;
                        }
                        if (numberReceived > 3)
                            throw new RuntimeException("Failed: Too many datagrams");
                        sa = null;
                    }
                }
            } catch (Exception ex) {
                e = ex;
            } finally {
                try {
                    dc.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
}
