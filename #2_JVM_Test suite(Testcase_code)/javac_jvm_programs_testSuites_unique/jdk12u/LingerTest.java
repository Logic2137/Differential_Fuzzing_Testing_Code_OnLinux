



import java.net.*;
import java.io.*;

public class LingerTest {

    static class Sender implements Runnable {
        Socket s;

        public Sender(Socket s) {
            this.s = s;
        }

        public void run() {
            System.out.println ("Sender starts");
            try {
                s.getOutputStream().write(new byte[128*1024]);
            }
            catch (IOException ioe) {
            }
            System.out.println ("Sender ends");
        }
    }

    static class Closer implements Runnable {
        Socket s;

        public Closer(Socket s) {
            this.s = s;
        }

        public void run() {
            System.out.println ("Closer starts");
            try {
                s.close();
            }
            catch (IOException ioe) {
            }
            System.out.println ("Closer ends");
        }
    }

    static class Other implements Runnable {
        int port;
        long delay;
        boolean connected = false;

        public Other(int port, long delay) {
            this.port = port;
            this.delay = delay;
        }

        public void run() {
            System.out.println ("Other starts: sleep " + delay);
            try {
                Thread.sleep(delay);
                System.out.println ("Other opening socket");
                Socket s = new Socket("localhost", port);
                synchronized (this) {
                    connected = true;
                }
                s.close();
            }
            catch (Exception ioe) {
                ioe.printStackTrace();
            }
            System.out.println ("Other ends");
        }

        public synchronized boolean connected() {
            return connected;
        }
    }

    public static void main(String args[]) throws Exception {
        ServerSocket ss = new ServerSocket(0);

        Socket s1 = new Socket("localhost", ss.getLocalPort());
        Socket s2 = ss.accept();

        
        
        s1.setSendBufferSize(128*1024);
        s1.setSoLinger(true, 30);
        s2.setReceiveBufferSize(1*1024);

        
        Thread senderThread = new Thread(new Sender(s1));
        senderThread.start();

        
        Other other = new Other(ss.getLocalPort(), 5000);
        Thread otherThread = new Thread(other);
        otherThread.start();

        
        System.out.println ("Main sleep 1000");
        Thread.sleep(1000);
        System.out.println ("Main continue");

        
        Thread closerThread = new Thread(new Closer(s1));
        closerThread.start();

        System.out.println ("Main sleep 15000");
        
        Thread.sleep(15000);
        System.out.println ("Main closing serversocket");

        ss.close();
        
        if (!other.connected()) {
            throw new RuntimeException("Other thread is blocked");
        }

        
        senderThread.join(60_000);
        otherThread.join(60_000);
        closerThread.join(60_000);

        System.out.println ("Main ends");
    }
}
