import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.net.*;
import java.util.concurrent.*;
import java.io.IOException;

public class Unbounded {

    static final int CONCURRENCY_COUNT = 256;

    static volatile boolean failed;

    static volatile boolean finished;

    public static void main(String[] args) throws Exception {
        AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(0));
        AsynchronousSocketChannel[] clients = new AsynchronousSocketChannel[CONCURRENCY_COUNT];
        AsynchronousSocketChannel[] peers = new AsynchronousSocketChannel[CONCURRENCY_COUNT];
        int port = ((InetSocketAddress) (listener.getLocalAddress())).getPort();
        SocketAddress sa = new InetSocketAddress(InetAddress.getLocalHost(), port);
        for (int i = 0; i < CONCURRENCY_COUNT; i++) {
            clients[i] = AsynchronousSocketChannel.open();
            Future<Void> result = clients[i].connect(sa);
            peers[i] = listener.accept().get();
            result.get();
        }
        System.out.println("All connection established.");
        final CyclicBarrier barrier = new CyclicBarrier(CONCURRENCY_COUNT + 1);
        for (AsynchronousSocketChannel client : clients) {
            ByteBuffer buf = ByteBuffer.allocateDirect(100);
            client.read(buf, client, new CompletionHandler<Integer, AsynchronousSocketChannel>() {

                public void completed(Integer bytesRead, AsynchronousSocketChannel ch) {
                    try {
                        ch.close();
                        barrier.await();
                    } catch (Exception x) {
                        throw new AssertionError(x);
                    }
                }

                public void failed(Throwable exc, AsynchronousSocketChannel ch) {
                    failed = true;
                    System.err.println("read failed: " + exc);
                    completed(0, ch);
                }
            });
        }
        System.out.println("All read operations outstanding.");
        for (AsynchronousSocketChannel peer : peers) {
            peer.write(ByteBuffer.wrap("welcome".getBytes())).get();
            peer.shutdownOutput();
            peer.close();
        }
        System.out.println("Waiting for all threads to reach barrier");
        barrier.await();
        finished = true;
        listener.close();
        if (failed)
            throw new RuntimeException("I/O operation failed, see log for details");
    }
}
