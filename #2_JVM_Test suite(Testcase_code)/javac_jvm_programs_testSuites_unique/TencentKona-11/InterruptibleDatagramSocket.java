

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;



public class InterruptibleDatagramSocket {
    private static void test0(DatagramSocket s) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Thread testThread = Thread.currentThread();

        Thread coordinator = new Thread(() -> {
            try {
                latch.await();
                sleep(500);
                testThread.interrupt();
            } catch (InterruptedException e) {
            }
        });
        byte[] data = {0, 1, 2};
        DatagramPacket p = new DatagramPacket(data, data.length,
                s.getLocalAddress(), s.getLocalPort());
        s.setSoTimeout(2000);
        coordinator.start();
        latch.countDown();
        try {
            s.receive(p);
        } finally {
            try {
                coordinator.join();
            } catch (InterruptedException e) {
            }
        }
    }

    static void test(DatagramSocket s, boolean interruptible) throws Exception {
        try {
            test0(s);
            throw new RuntimeException("Receive shouldn't have succeeded");
        } catch (SocketTimeoutException e) {
            if (interruptible)
                throw e;
            System.out.println("Got expected SocketTimeoutException: " + e);
        } catch (SocketException e) {
            if ((e.getCause() instanceof ClosedByInterruptException) && interruptible) {
                System.out.println("Got expected ClosedByInterruptException: " + e);
            } else {
                throw e;
            }
        } catch (ClosedByInterruptException e) {
            if (!interruptible)
                throw e;
            System.out.println("Got expected ClosedByInterruptException: " + e);
        }
        if (s.isClosed() && !interruptible)
            throw new RuntimeException("DatagramSocket should not be closed");
        else if (!s.isClosed() && interruptible)
            throw new RuntimeException("DatagramSocket should be closed");
    }

    public static void main(String[] args) throws Exception {
        try (DatagramSocket s = new DatagramSocket()) {
            test(s, false);
        }
        try (DatagramSocket s = new MulticastSocket()) {
            test(s, false);
        }
        try (DatagramSocket s = DatagramChannel.open().socket()) {
            test(s, true);
        }
    }
}
