import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import static java.nio.channels.SelectionKey.*;
import java.io.IOException;

public class ChangingInterests {

    static int[] OPS = { 0, OP_WRITE, OP_READ, (OP_WRITE | OP_READ) };

    static String toOpsString(int ops) {
        String s = "";
        if ((ops & OP_READ) > 0)
            s += "POLLIN";
        if ((ops & OP_WRITE) > 0) {
            if (s.length() > 0)
                s += "|";
            s += "POLLOUT";
        }
        if (s.length() == 0)
            s = "0";
        return "(" + s + ")";
    }

    static void makeReadable(SocketChannel out, SocketChannel in) throws IOException {
        out.write(ByteBuffer.wrap(new byte[2]));
        ByteBuffer oneByte = ByteBuffer.wrap(new byte[1]);
        do {
            int n = in.read(oneByte);
            if (n == 1) {
                break;
            } else if (n == 0) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignore) {
                }
            } else {
                throw new RuntimeException("Expected to read 0 or 1 byte; actual number was " + n);
            }
        } while (true);
    }

    static void drain(SocketChannel sc) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(100);
        int n;
        while ((n = sc.read(buf)) > 0) {
            buf.rewind();
        }
    }

    static void testChange(SelectionKey key, int from, int to) throws IOException {
        Selector sel = key.selector();
        assertTrue(sel.keys().size() == 1, "Only one channel should be registered");
        key.interestOps(from);
        sel.selectNow();
        sel.selectedKeys().clear();
        key.interestOps(to);
        System.out.println("select...");
        int selected = sel.selectNow();
        System.out.println("" + selected + " channel(s) selected");
        int expected = (to == 0) ? 0 : 1;
        assertTrue(selected == expected, "Expected " + expected);
        for (SelectionKey k : sel.selectedKeys()) {
            assertTrue(k == key, "Unexpected key selected");
            boolean readable = k.isReadable();
            boolean writable = k.isWritable();
            System.out.println("key readable: " + readable);
            System.out.println("key writable: " + writable);
            if ((to & OP_READ) == 0) {
                assertTrue(!readable, "Not expected to be readable");
            } else {
                assertTrue(readable, "Expected to be readable");
            }
            if ((to & OP_WRITE) == 0) {
                assertTrue(!writable, "Not expected to be writable");
            } else {
                assertTrue(writable, "Expected to be writable");
            }
            sel.selectedKeys().clear();
        }
    }

    static void testForSpin(Selector sel) throws IOException {
        System.out.println("Test for spin...");
        long start = System.currentTimeMillis();
        int count = 3;
        while (count-- > 0) {
            int selected = sel.select(1000);
            System.out.println("" + selected + " channel(s) selected");
            assertTrue(selected == 0, "Channel should not be selected");
        }
        long dur = System.currentTimeMillis() - start;
        assertTrue(dur > 1000, "select was too short");
    }

    public static void main(String[] args) throws IOException {
        InetAddress lh = InetAddress.getLocalHost();
        ServerSocketChannel ssc = ServerSocketChannel.open().bind(new InetSocketAddress(0));
        final SocketChannel sc = SocketChannel.open();
        sc.setOption(StandardSocketOptions.TCP_NODELAY, true);
        sc.connect(new InetSocketAddress(lh, ssc.socket().getLocalPort()));
        SocketChannel peer = ssc.accept();
        peer.setOption(StandardSocketOptions.TCP_NODELAY, true);
        sc.configureBlocking(false);
        makeReadable(peer, sc);
        try (Selector sel = Selector.open()) {
            SelectionKey key = sc.register(sel, 0);
            sel.selectNow();
            for (int from : OPS) {
                for (int to : OPS) {
                    System.out.println(toOpsString(from) + " -> " + toOpsString(to));
                    testChange(key, from, to);
                    if (to == 0)
                        testForSpin(sel);
                    if (to == OP_READ) {
                        System.out.println("Drain channel...");
                        drain(sc);
                        testForSpin(sel);
                        System.out.println("Make channel readable again");
                        makeReadable(peer, sc);
                    }
                    System.out.println();
                }
            }
        } finally {
            sc.close();
            peer.close();
            ssc.close();
        }
    }

    static void assertTrue(boolean v, String msg) {
        if (!v)
            throw new RuntimeException(msg);
    }
}
