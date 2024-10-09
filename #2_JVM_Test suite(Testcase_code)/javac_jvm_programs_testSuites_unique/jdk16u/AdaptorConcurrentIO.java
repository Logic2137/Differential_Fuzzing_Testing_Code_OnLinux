



import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AdaptorConcurrentIO {

    public static void main(String[] args) throws Exception {
        testConcurrentSendReceive(0);
        testConcurrentSendReceive(60_000);
    }

    
    static void testConcurrentSendReceive(int timeout) throws Exception {
        try (DatagramChannel dc = DatagramChannel.open()) {
            InetAddress lb = InetAddress.getLoopbackAddress();
            dc.bind(new InetSocketAddress(lb, 0));
            DatagramSocket s = dc.socket();
            s.setSoTimeout(timeout);

            ExecutorService pool = Executors.newSingleThreadExecutor();
            try {
                Future<String> result = pool.submit(() -> {
                    byte[] data = new byte[100];
                    DatagramPacket p = new DatagramPacket(data, 0, data.length);
                    s.receive(p);
                    return new String(p.getData(), p.getOffset(), p.getLength(), "UTF-8");
                });

                Thread.sleep(200); 

                byte[] data = "hello".getBytes("UTF-8");
                DatagramPacket p = new DatagramPacket(data, 0, data.length);
                p.setSocketAddress(s.getLocalSocketAddress());
                s.send(p);

                String msg = result.get();
                if (!msg.equals("hello"))
                    throw new RuntimeException("Unexpected message: " + msg);
            } finally {
                pool.shutdown();
            }
        }
    }
}

