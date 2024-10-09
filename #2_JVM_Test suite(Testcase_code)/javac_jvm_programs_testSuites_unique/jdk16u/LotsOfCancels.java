

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class LotsOfCancels {

    static long testStartTime;

    public static void main(String[] args) throws Exception {
        
        runTest(500, 2700, 1000);
    }

    static void log(String msg) {
        System.out.println(getLogPrefix() + msg);
    }

    static String getLogPrefix() {
        return durationMillis(testStartTime) + ": ";
    }

    
    static long durationMillis(long startNanos) {
        return (System.nanoTime() - startNanos) / (1000L * 1000L);
    }

    static void runTest(int initCount, int massCount, int maxSelectTime)
            throws Exception {
        testStartTime = System.nanoTime();

        InetSocketAddress address = new InetSocketAddress(InetAddress.getLoopbackAddress(), 7359);

        
        log("Setting up server");
        Selector serverSelector = Selector.open();
        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.socket().bind(address, 5000);
        server.register(serverSelector, SelectionKey.OP_ACCEPT);
        serverSelector.selectNow();

        log("Setting up client");
        ClientThread client = new ClientThread(address);
        client.start();
        Thread.sleep(100);

        
        log("Starting initial client connections");
        client.connectClients(initCount);
        Thread.sleep(500);  

        
        
        log("Accepting initial connections");
        List<SocketChannel> serverChannels1 =
            acceptAndAddAll(serverSelector, server, initCount);
        if (serverChannels1.size() != initCount) {
            throw new Exception("Accepted " + serverChannels1.size() +
                                " instead of " + initCount);
        }
        serverSelector.selectNow();

        
        log("Requesting mass client connections");
        client.connectClients(massCount);
        Thread.sleep(500);  

        
        
        log("Accepting mass connections");
        List<SocketChannel> serverChannels2 =
            acceptAndAddAll(serverSelector, server, massCount);
        if (serverChannels2.size() != massCount) {
            throw new Exception("Accepted " + serverChannels2.size() +
                                " instead of " + massCount);
        }

        
        log("Closing initial connections");
        closeAll(serverChannels1);

        
        log("Running the final select call");
        long startTime = System.nanoTime();
        serverSelector.selectNow();
        long duration = durationMillis(startTime);
        log("Init count = " + initCount +
            ", mass count = " + massCount +
            ", duration = " + duration + "ms");

        if (duration > maxSelectTime) {
            System.out.println
                ("\n\n\n\n\nFAILURE: The final selectNow() took " +
                 duration + "ms " +
                 "- seems like O(N^2) bug is still here\n\n");
            System.exit(1);
        }
    }

    static List<SocketChannel> acceptAndAddAll(Selector selector,
                                               ServerSocketChannel server,
                                               int expected)
            throws Exception {
        int retryCount = 0;
        int acceptCount = 0;
        List<SocketChannel> channels = new ArrayList<SocketChannel>();
        while (channels.size() < expected) {
            SocketChannel channel = server.accept();
            if (channel == null) {
                log("accept() returned null " +
                    "after accepting " + acceptCount + " more connections");
                acceptCount = 0;
                if (retryCount < 10) {
                    
                    retryCount++;
                    Thread.sleep(500);
                    continue;
                }
                break;
            }
            retryCount = 0;
            acceptCount++;
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
            channels.add(channel);
        }
        
        for (SocketChannel channel : channels) {
            channel.register(selector, SelectionKey.OP_WRITE);
        }
        return channels;
    }

    static void closeAll(List<SocketChannel> channels)
            throws Exception {
        for (SocketChannel channel : channels) {
            channel.close();
        }
    }

    static class ClientThread extends Thread {
        private final SocketAddress address;
        private final Selector selector;
        private int connectionsNeeded;
        private int totalCreated;

        ClientThread(SocketAddress address) throws Exception {
            this.address = address;
            selector = Selector.open();
            setDaemon(true);
        }

        void connectClients(int count) throws Exception {
            synchronized (this) {
                connectionsNeeded += count;
            }
            selector.wakeup();
        }

        @Override
        public void run() {
            try {
                handleClients();
            } catch (Throwable e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        private void handleClients() throws Exception {
            int selectCount = 0;
            while (true) {
                int createdCount = 0;
                synchronized (this) {
                    if (connectionsNeeded > 0) {

                        while (connectionsNeeded > 0 && createdCount < 20) {
                            connectionsNeeded--;
                            createdCount++;
                            totalCreated++;

                            SocketChannel channel = SocketChannel.open();
                            channel.configureBlocking(false);
                            channel.connect(address);
                            if (!channel.finishConnect()) {
                                channel.register(selector,
                                                 SelectionKey.OP_CONNECT);
                            }
                        }

                        log("Started total of " +
                            totalCreated + " client connections");
                        Thread.sleep(200);
                    }
                }

                if (createdCount > 0) {
                    selector.selectNow();
                } else {
                    selectCount++;
                    long startTime = System.nanoTime();
                    selector.select();
                    long duration = durationMillis(startTime);
                    log("Exited clientSelector.select(), loop #"
                        + selectCount + ", duration = " + duration + "ms");
                }

                int keyCount = -1;
                Iterator<SelectionKey> keys =
                    selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    synchronized (key) {
                        keyCount++;
                        keys.remove();
                        if (!key.isValid()) {
                            log("Ignoring client key #" + keyCount);
                            continue;
                        }
                        int readyOps = key.readyOps();
                        if (readyOps == SelectionKey.OP_CONNECT) {
                            key.interestOps(0);
                            ((SocketChannel) key.channel()).finishConnect();
                        } else {
                            log("readyOps() on client key #" + keyCount +
                                " returned " + readyOps);
                        }
                    }
                }
            }
        }
    }
}
