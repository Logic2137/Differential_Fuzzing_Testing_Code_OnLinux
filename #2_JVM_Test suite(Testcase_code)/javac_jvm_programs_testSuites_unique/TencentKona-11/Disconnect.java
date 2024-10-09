



import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.io.IOException;

public class Disconnect {
    public static void main(String[] args) throws IOException {
        
        try (DatagramChannel dc = DatagramChannel.open()) {
            test(dc);
            test(dc);
        }

        
        try (DatagramChannel dc = DatagramChannel.open(StandardProtocolFamily.INET)) {
            test(dc);
            test(dc);
        }
    }

    
    static void test(DatagramChannel dc) throws IOException {
        try (DatagramChannel server = DatagramChannel.open()) {
            server.bind(new InetSocketAddress(0));

            InetAddress lh = InetAddress.getLocalHost();
            dc.connect(new InetSocketAddress(lh, server.socket().getLocalPort()));

            dc.write(ByteBuffer.wrap("hello".getBytes()));

            ByteBuffer bb = ByteBuffer.allocate(100);
            server.receive(bb);

            dc.disconnect();

            try {
                dc.write(ByteBuffer.wrap("another message".getBytes()));
                throw new RuntimeException("write should fail, not connected");
            } catch (NotYetConnectedException expected) {
            }
        }
    }
}
