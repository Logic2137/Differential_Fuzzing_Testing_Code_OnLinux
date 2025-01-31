



import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.nio.channels.*;

public class CloseWhenKeyIdle {

    
    static volatile boolean wakeupDone = false;

    
    static class Waker implements Runnable {
        private Selector sel;
        private long delay;

        Waker(Selector sel, long delay) {
            this.sel = sel;
            this.delay = delay;
        }

        public void run() {
            try {
                Thread.sleep(delay);
                wakeupDone = true;
                sel.wakeup();
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws Exception {

        

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(0));

        SocketAddress remote = new InetSocketAddress(InetAddress.getLocalHost(),
            ssc.socket().getLocalPort());

        SocketChannel sc1 = SocketChannel.open(remote);
        SocketChannel sc2 = ssc.accept();

        

        Selector sel = Selector.open();

        sc1.configureBlocking(false);
        SelectionKey k = sc1.register(sel, 0);
        sel.selectNow();

        

        sc2.socket().setSoLinger(true, 0);
        sc2.close();

        

        Thread t = new Thread(new Waker(sel, 5000));
        t.setDaemon(true);
        t.start();

        

        int spinCount = 0;
        boolean failed = false;
        for (;;) {
            int n = sel.select();
            if (n > 0) {
                System.err.println("Channel should not be selected!!!");
                failed = true;
                break;
            }

            
            if (wakeupDone)
                break;

            
            
            spinCount++;
            if (spinCount >= 3) {
                System.err.println("Selector appears to be spinning");
                failed = true;
                break;
            }
        }

        sc1.close();
        sel.close();

        if (failed)
            throw new RuntimeException("Test failed");

        System.out.println("PASS");
    }

}
