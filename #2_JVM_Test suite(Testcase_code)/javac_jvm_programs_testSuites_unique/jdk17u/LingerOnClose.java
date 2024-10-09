import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class LingerOnClose {

    private enum TestMode {

        BLOCKING, NON_BLOCKING, NON_BLOCKING_AND_REGISTERED
    }

    public static void main(String[] args) throws IOException {
        test(TestMode.BLOCKING, -1);
        test(TestMode.BLOCKING, 0);
        test(TestMode.BLOCKING, 1);
        test(TestMode.NON_BLOCKING, -1);
        test(TestMode.NON_BLOCKING, 0);
        test(TestMode.NON_BLOCKING, 1);
        test(TestMode.NON_BLOCKING_AND_REGISTERED, -1);
        test(TestMode.NON_BLOCKING_AND_REGISTERED, 0);
        test(TestMode.NON_BLOCKING_AND_REGISTERED, 1);
    }

    static void test(TestMode mode, int interval) throws IOException {
        SocketChannel sc = null;
        SocketChannel peer = null;
        Selector sel = null;
        try (ServerSocketChannel ssc = ServerSocketChannel.open()) {
            ssc.bind(new InetSocketAddress(InetAddress.getLocalHost(), 0));
            sc = SocketChannel.open(ssc.getLocalAddress());
            peer = ssc.accept();
            if (mode != TestMode.BLOCKING)
                sc.configureBlocking(false);
            if (mode == TestMode.NON_BLOCKING_AND_REGISTERED) {
                sel = Selector.open();
                sc.register(sel, SelectionKey.OP_READ);
                sel.selectNow();
            }
            sc.setOption(StandardSocketOptions.SO_LINGER, interval);
            sc.close();
            if (mode == TestMode.NON_BLOCKING_AND_REGISTERED)
                sel.selectNow();
            ByteBuffer bb = ByteBuffer.allocate(100);
            try {
                int n = peer.read(bb);
                if (interval == 0) {
                    throw new RuntimeException("RST expected");
                } else if (n != -1) {
                    throw new RuntimeException("EOF expected");
                }
            } catch (IOException ioe) {
                if (interval != 0) {
                    throw ioe;
                }
            }
        } finally {
            if (sc != null)
                sc.close();
            if (peer != null)
                peer.close();
            if (sel != null)
                sel.close();
        }
    }
}
