import java.nio.ByteBuffer;
import java.net.*;
import java.nio.channels.*;
import java.util.Random;
import java.io.IOException;

public class StressLoopback {

    static final Random rand = new Random();

    public static void main(String[] args) throws Exception {
        AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(0));
        int port = ((InetSocketAddress) (listener.getLocalAddress())).getPort();
        InetAddress lh = InetAddress.getLocalHost();
        SocketAddress remote = new InetSocketAddress(lh, port);
        int count = 2 + rand.nextInt(9);
        Source[] source = new Source[count];
        Sink[] sink = new Sink[count];
        for (int i = 0; i < count; i++) {
            AsynchronousSocketChannel ch = AsynchronousSocketChannel.open();
            ch.connect(remote).get();
            source[i] = new Source(ch);
            sink[i] = new Sink(listener.accept().get());
        }
        for (int i = 0; i < count; i++) {
            sink[i].start();
            source[i].start();
        }
        Thread.sleep(20 * 1000);
        boolean failed = false;
        long total = 0L;
        for (int i = 0; i < count; i++) {
            long nwrote = source[i].finish();
            long nread = sink[i].finish();
            if (nread != nwrote)
                failed = true;
            System.out.format("%d -> %d (%s)\n", nwrote, nread, (failed) ? "FAIL" : "PASS");
            total += nwrote;
        }
        if (failed)
            throw new RuntimeException("Test failed - see log for details");
        System.out.format("Total sent %d MB\n", total / (1024L * 1024L));
    }

    static class Source {

        private final AsynchronousByteChannel channel;

        private final ByteBuffer sentBuffer;

        private volatile long bytesSent;

        private volatile boolean finished;

        Source(AsynchronousByteChannel channel) {
            this.channel = channel;
            int size = 1024 + rand.nextInt(10000);
            this.sentBuffer = (rand.nextBoolean()) ? ByteBuffer.allocateDirect(size) : ByteBuffer.allocate(size);
        }

        void start() {
            sentBuffer.position(0);
            sentBuffer.limit(sentBuffer.capacity());
            channel.write(sentBuffer, (Void) null, new CompletionHandler<Integer, Void>() {

                public void completed(Integer nwrote, Void att) {
                    bytesSent += nwrote;
                    if (finished) {
                        closeUnchecked(channel);
                    } else {
                        sentBuffer.position(0);
                        sentBuffer.limit(sentBuffer.capacity());
                        channel.write(sentBuffer, (Void) null, this);
                    }
                }

                public void failed(Throwable exc, Void att) {
                    exc.printStackTrace();
                    closeUnchecked(channel);
                }
            });
        }

        long finish() {
            finished = true;
            waitUntilClosed(channel);
            return bytesSent;
        }
    }

    static class Sink {

        private final AsynchronousByteChannel channel;

        private final ByteBuffer readBuffer;

        private volatile long bytesRead;

        Sink(AsynchronousByteChannel channel) {
            this.channel = channel;
            int size = 1024 + rand.nextInt(10000);
            this.readBuffer = (rand.nextBoolean()) ? ByteBuffer.allocateDirect(size) : ByteBuffer.allocate(size);
        }

        void start() {
            channel.read(readBuffer, (Void) null, new CompletionHandler<Integer, Void>() {

                public void completed(Integer nread, Void att) {
                    if (nread < 0) {
                        closeUnchecked(channel);
                    } else {
                        bytesRead += nread;
                        readBuffer.clear();
                        channel.read(readBuffer, (Void) null, this);
                    }
                }

                public void failed(Throwable exc, Void att) {
                    exc.printStackTrace();
                    closeUnchecked(channel);
                }
            });
        }

        long finish() {
            waitUntilClosed(channel);
            return bytesRead;
        }
    }

    static void waitUntilClosed(Channel c) {
        while (c.isOpen()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignore) {
            }
        }
    }

    static void closeUnchecked(Channel c) {
        try {
            c.close();
        } catch (IOException ignore) {
        }
    }
}
