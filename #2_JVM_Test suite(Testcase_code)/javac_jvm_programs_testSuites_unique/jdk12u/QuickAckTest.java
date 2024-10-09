

 
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import jdk.net.ExtendedSocketOptions;
import jdk.net.Sockets;

public class QuickAckTest {

    private static final String LOCAL_HOST = "127.0.0.1";

    public static void main(String args[]) throws IOException {

        try (ServerSocket ss = new ServerSocket(0);
                Socket s = new Socket(LOCAL_HOST, ss.getLocalPort());
                DatagramSocket ds = new DatagramSocket(0);
                MulticastSocket mc = new MulticastSocket(0)) {

            if (ss.supportedOptions().contains(ExtendedSocketOptions.TCP_QUICKACK)) {
                Sockets.setOption(ss, ExtendedSocketOptions.TCP_QUICKACK, true);
                if (!ss.getOption(ExtendedSocketOptions.TCP_QUICKACK)) {
                    throw new RuntimeException("Test failed, TCP_QUICKACK should"
                            + " have been set");
                }
            }
            if (s.supportedOptions().contains(ExtendedSocketOptions.TCP_QUICKACK)) {
                Sockets.setOption(s, ExtendedSocketOptions.TCP_QUICKACK, true);
                if (!s.getOption(ExtendedSocketOptions.TCP_QUICKACK)) {
                    throw new RuntimeException("Test failed, TCP_QUICKACK should"
                            + " have been set");
                }
            }
            if (ds.supportedOptions().contains(ExtendedSocketOptions.TCP_QUICKACK)) {
                throw new RuntimeException("Test failed, TCP_QUICKACK is applicable"
                        + " for TCP Sockets only.");
            }
            if (mc.supportedOptions().contains(ExtendedSocketOptions.TCP_QUICKACK)) {
                throw new RuntimeException("Test failed, TCP_QUICKACK is applicable"
                        + " for TCP Sockets only");
            }
        }
    }
}
