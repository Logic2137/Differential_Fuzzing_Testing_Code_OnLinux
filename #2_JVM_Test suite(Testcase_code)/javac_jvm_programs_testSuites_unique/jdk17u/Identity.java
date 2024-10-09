import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.io.IOException;

public class Identity {

    static final Random rand = new Random();

    static final CountDownLatch done = new CountDownLatch(1);

    static final AtomicBoolean failed = new AtomicBoolean(false);

    static void fail(String msg) {
        failed.set(true);
        done.countDown();
        throw new RuntimeException(msg);
    }

    private static final ThreadLocal<Integer> myGroup = new ThreadLocal<Integer>() {

        @Override
        protected Integer initialValue() {
            return Integer.valueOf(-1);
        }
    };

    static final ThreadFactory createThreadFactory(final int groupId) {
        return new ThreadFactory() {

            @Override
            public Thread newThread(final Runnable r) {
                Thread t = new Thread(new Runnable() {

                    public void run() {
                        myGroup.set(groupId);
                        r.run();
                    }
                });
                t.setDaemon(true);
                return t;
            }
        };
    }

    public static void main(String[] args) throws Exception {
        final int groupCount = 3 + rand.nextInt(8);
        final AsynchronousChannelGroup[] groups = new AsynchronousChannelGroup[groupCount];
        final AsynchronousSocketChannel[] channels = new AsynchronousSocketChannel[groupCount];
        try (final AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open()) {
            listener.bind(new InetSocketAddress(0));
            listener.accept((Void) null, new CompletionHandler<AsynchronousSocketChannel, Void>() {

                public void completed(final AsynchronousSocketChannel ch, Void att) {
                    listener.accept((Void) null, this);
                    final ByteBuffer buf = ByteBuffer.allocate(100);
                    ch.read(buf, ch, new CompletionHandler<Integer, AsynchronousSocketChannel>() {

                        public void completed(Integer bytesRead, AsynchronousSocketChannel ch) {
                            if (bytesRead < 0) {
                                try {
                                    ch.close();
                                } catch (IOException ignore) {
                                }
                            } else {
                                buf.clear();
                                ch.read(buf, ch, this);
                            }
                        }

                        public void failed(Throwable exc, AsynchronousSocketChannel ch) {
                            try {
                                ch.close();
                            } catch (IOException ignore) {
                            }
                        }
                    });
                }

                public void failed(Throwable exc, Void att) {
                }
            });
            int port = ((InetSocketAddress) (listener.getLocalAddress())).getPort();
            SocketAddress sa = new InetSocketAddress(InetAddress.getLocalHost(), port);
            for (int i = 0; i < groupCount; i++) {
                ThreadFactory factory = createThreadFactory(i);
                AsynchronousChannelGroup group;
                if (rand.nextBoolean()) {
                    int nThreads = 1 + rand.nextInt(10);
                    group = AsynchronousChannelGroup.withFixedThreadPool(nThreads, factory);
                } else {
                    ExecutorService pool = Executors.newCachedThreadPool(factory);
                    group = AsynchronousChannelGroup.withCachedThreadPool(pool, rand.nextInt(5));
                }
                groups[i] = group;
                AsynchronousSocketChannel ch = AsynchronousSocketChannel.open(group);
                ch.connect(sa).get();
                channels[i] = ch;
            }
            final AtomicInteger writeCount = new AtomicInteger(100);
            channels[0].write(getBuffer(), 0, new CompletionHandler<Integer, Integer>() {

                public void completed(Integer bytesWritten, Integer groupId) {
                    if (bytesWritten != 1)
                        fail("Expected 1 byte to be written");
                    if (!myGroup.get().equals(groupId))
                        fail("Handler invoked by thread with the wrong identity");
                    if (writeCount.decrementAndGet() > 0) {
                        int id = rand.nextInt(groupCount);
                        channels[id].write(getBuffer(), id, this);
                    } else {
                        done.countDown();
                    }
                }

                public void failed(Throwable exc, Integer groupId) {
                    fail(exc.getMessage());
                }
            });
            done.await();
        } finally {
            for (AsynchronousSocketChannel ch : channels) ch.close();
            for (AsynchronousChannelGroup group : groups) group.shutdownNow();
            if (failed.get())
                throw new RuntimeException("Test failed - see log for details");
        }
    }

    static ByteBuffer getBuffer() {
        ByteBuffer buf;
        if (rand.nextBoolean()) {
            buf = ByteBuffer.allocateDirect(1);
        } else {
            buf = ByteBuffer.allocate(1);
        }
        buf.put((byte) 0);
        buf.flip();
        return buf;
    }
}
