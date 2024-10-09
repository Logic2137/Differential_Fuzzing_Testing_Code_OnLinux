import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.io.IOException;

public class OutOfBand {

    public static void main(String[] args) throws Exception {
        ServerSocketChannel ssc = null;
        SocketChannel sc = null;
        Selector sel = null;
        Socket s = null;
        try {
            ssc = ServerSocketChannel.open().bind(new InetSocketAddress(0));
            s = new Socket(InetAddress.getLocalHost(), ssc.socket().getLocalPort());
            sc = ssc.accept();
            sel = Selector.open();
            sc.configureBlocking(false);
            sc.register(sel, SelectionKey.OP_READ);
            if (sc.socket().getOOBInline())
                throw new RuntimeException("SO_OOBINLINE enabled");
            test(s, false, 0, 0, sel);
            test(s, false, 512, 0, sel);
            test(s, false, 0, 512, sel);
            test(s, false, 512, 512, sel);
            sc.socket().setOOBInline(true);
            test(s, true, 0, 0, sel);
            test(s, true, 512, 0, sel);
            test(s, true, 0, 512, sel);
            test(s, true, 512, 512, sel);
        } finally {
            if (sel != null)
                sel.close();
            if (sc != null)
                sc.close();
            if (ssc != null)
                ssc.close();
            if (s != null)
                sc.close();
        }
    }

    static void test(Socket s, boolean urgentExpected, int bytesBefore, int bytesAfter, Selector sel) throws IOException {
        int bytesExpected = 0;
        if (bytesBefore > 0) {
            s.getOutputStream().write(new byte[bytesBefore]);
            bytesExpected += bytesBefore;
        }
        s.sendUrgentData(0xff);
        if (urgentExpected)
            bytesExpected++;
        if (bytesAfter > 0) {
            s.getOutputStream().write(new byte[bytesAfter]);
            bytesExpected += bytesAfter;
        }
        int spuriousWakeups = 0;
        int spuriousReads = 0;
        int bytesRead = 0;
        ByteBuffer bb = ByteBuffer.allocate(100);
        for (; ; ) {
            int n = sel.select(2000);
            if (n == 0) {
                if (bytesRead == bytesExpected) {
                    System.out.format("Selector wakeups %d\tSpurious reads %d%n", spuriousWakeups, spuriousReads);
                    return;
                }
                if (++spuriousWakeups >= 3)
                    throw new RuntimeException("Selector appears to be spinning" + " or data not received");
                continue;
            }
            if (n > 1)
                throw new RuntimeException("More than one key selected????");
            SelectionKey key = sel.selectedKeys().iterator().next();
            bb.clear();
            n = ((SocketChannel) key.channel()).read(bb);
            if (n == 0) {
                if (++spuriousReads >= 3)
                    throw new RuntimeException("Too many spurious reads");
            } else {
                bytesRead += n;
                if (bytesRead > bytesExpected)
                    throw new RuntimeException("Received more than expected");
            }
            sel.selectedKeys().clear();
        }
    }
}
