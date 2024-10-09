import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class SendUrgentData {

    public static void main(String[] args) throws Exception {
        ServerSocketChannelThread serverThread = new ServerSocketChannelThread("SendUrgentDataServer");
        serverThread.start();
        boolean b = serverThread.isAlive();
        String host = null;
        int port = 0;
        boolean inline = false;
        if (args.length > 0 && args[0].equals("-server")) {
            System.out.println(serverThread.getAddress());
            Thread.currentThread().suspend();
        } else {
            if (args.length > 0 && args[0].equals("-client")) {
                host = args[1];
                port = Integer.parseInt(args[2]);
                if (args.length > 3) {
                    inline = args[2].equals("-inline");
                }
            } else {
                host = "localhost";
                port = serverThread.getAddress().getPort();
                if (args.length > 0) {
                    inline = args[0].equals("-inline");
                }
            }
        }
        System.out.println("OOB Inline : " + inline);
        SocketAddress sa = new InetSocketAddress(host, port);
        try (SocketChannel sc = SocketChannel.open(sa)) {
            sc.configureBlocking(false);
            sc.socket().setOOBInline(inline);
            sc.socket().sendUrgentData(0);
            System.out.println("wrote 1 OOB byte");
            ByteBuffer bb = ByteBuffer.wrap(new byte[100 * 1000]);
            int blocked = 0;
            long total = 0;
            int n;
            do {
                n = sc.write(bb);
                if (n == 0) {
                    System.out.println("blocked, wrote " + total + " so far");
                    if (++blocked == 10) {
                        break;
                    }
                    Thread.sleep(100);
                } else {
                    total += n;
                    bb.rewind();
                }
            } while (n > 0);
            long attempted = 0;
            while (attempted < total) {
                bb.rewind();
                n = sc.write(bb);
                System.out.println("wrote " + n + " normal bytes");
                attempted += bb.capacity();
                String osName = System.getProperty("os.name").toLowerCase();
                try {
                    sc.socket().sendUrgentData(0);
                } catch (IOException ex) {
                    if (osName.contains("linux")) {
                        if (!ex.getMessage().contains("Socket buffer full")) {
                            throw new RuntimeException("Unexpected message", ex);
                        }
                    } else if (osName.contains("os x") || osName.contains("mac")) {
                        if (!ex.getMessage().equals("No buffer space available")) {
                            throw new RuntimeException("Unexpected message", ex);
                        }
                    } else if (osName.contains("windows")) {
                        if (!ex.getMessage().equals("Socket buffer full")) {
                            throw new RuntimeException("Unexpected message", ex);
                        }
                    } else {
                        throw new RuntimeException("Unexpected IOException", ex);
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    break;
                }
            }
        } finally {
            serverThread.close();
        }
    }

    static class ServerSocketChannelThread extends Thread {

        private ServerSocketChannel ssc;

        private ServerSocketChannelThread(String name) {
            super(name);
            try {
                ssc = ServerSocketChannel.open();
                ssc.bind(new InetSocketAddress((0)));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        public void run() {
            while (ssc.isOpen()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
            try {
                ssc.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("ServerSocketChannelThread exiting ...");
        }

        public InetSocketAddress getAddress() throws IOException {
            if (ssc == null) {
                throw new IllegalStateException("ServerSocketChannel not created");
            }
            return (InetSocketAddress) ssc.getLocalAddress();
        }

        public void close() {
            try {
                ssc.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
