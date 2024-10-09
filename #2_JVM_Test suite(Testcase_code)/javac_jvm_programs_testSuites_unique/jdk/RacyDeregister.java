



import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;





public class RacyDeregister {

    
    static final int TIMEOUT_THRESHOLD_MILLIS = 1200*900;

    
    static long t0;

    static boolean notified;
    static final Object selectorLock = new Object();
    static final Object notifyLock = new Object();
    
    static volatile Boolean succTermination = null;

    public static void main(String[] args) throws Exception {
        t0 = System.currentTimeMillis();

        InetAddress addr = InetAddress.getByName(null);
        ServerSocketChannel sc = ServerSocketChannel.open();
        sc.socket().bind(new InetSocketAddress(addr, 0));

        SocketChannel.open(new InetSocketAddress(addr,
                sc.socket().getLocalPort()));

        SocketChannel accepted = sc.accept();
        accepted.configureBlocking(false);

        SocketChannel.open(new InetSocketAddress(addr,
                sc.socket().getLocalPort()));
        SocketChannel accepted2 = sc.accept();
        accepted2.configureBlocking(false);

        final Selector sel = Selector.open();
        SelectionKey key2 = accepted2.register(sel, SelectionKey.OP_READ);
        final SelectionKey[] key = new SelectionKey[]{
            accepted.register(sel, SelectionKey.OP_READ)};

        
        new Thread() {

            public void run() {
                try {
                    for (int k = 0; k < 15; k++) {
                        System.out.format("outer loop %3d at %7d ms%n", k,
                            System.currentTimeMillis() - t0);
                        System.out.flush();
                        for (int i = 0; i < 10000; i++) {
                            synchronized (notifyLock) {
                                synchronized (selectorLock) {
                                    sel.wakeup();
                                    key[0].interestOps(SelectionKey.OP_READ
                                            | SelectionKey.OP_WRITE);
                                }
                                notified = false;
                                long beginTime = System.currentTimeMillis();
                                while (true) {
                                    notifyLock.wait(5000);
                                    if (notified) {
                                        break;
                                    }
                                    long endTime = System.currentTimeMillis();
                                    if (endTime - beginTime > 5000) {
                                        for (int j = 0; j < 60; j++) {
                                            Thread.sleep(1000);
                                            if (notified) {
                                                long t =
                                                    System.currentTimeMillis();
                                                System.err.printf
                                                    ("Notified after %d ms%n",
                                                     t - beginTime);
                                                System.err.flush();
                                                break;
                                            }
                                        }
                                        succTermination = false;
                                        
                                        sel.wakeup();
                                        return;
                                    }
                                }
                            }
                        }
                        long t = System.currentTimeMillis();
                        if (t - t0 > TIMEOUT_THRESHOLD_MILLIS) {
                            System.err.format
                                ("Timeout after %d outer loop iterations%n", k);
                            System.err.flush();
                            succTermination = false;
                            
                            sel.wakeup();
                            return;
                        }
                    }
                    succTermination = true;
                    
                    sel.wakeup();
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.flush();
                    succTermination = true;
                    
                    sel.wakeup();
                }
            }
        }.start();

        
        while (true) {
            sel.select();
            if (Boolean.TRUE.equals(succTermination)) {
                System.out.println("Test passed");
                System.out.flush();
                sel.close();
                sc.close();
                break;
            } else if (Boolean.FALSE.equals(succTermination)) {
                System.err.println("Failed to pass the test");
                System.err.flush();
                sel.close();
                sc.close();
                throw new RuntimeException("Failed to pass the test");
            }
            synchronized (selectorLock) {
            }
            if (sel.selectedKeys().contains(key[0]) && key[0].isWritable()) {
                synchronized (notifyLock) {
                    notified = true;
                    notifyLock.notify();
                    key[0].cancel();
                    sel.selectNow();
                    key2 = accepted2.register(sel, SelectionKey.OP_READ);
                    key[0] = accepted.register(sel, SelectionKey.OP_READ);
                }
            }
            key2.cancel();
            sel.selectedKeys().clear();
        }
    }
}
