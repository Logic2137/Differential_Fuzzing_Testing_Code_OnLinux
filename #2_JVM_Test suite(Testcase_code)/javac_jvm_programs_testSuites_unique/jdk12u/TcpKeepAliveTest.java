

 
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import jdk.net.ExtendedSocketOptions;

public class TcpKeepAliveTest {

    private static final String LOCAL_HOST = "127.0.0.1";
    private static final int DEFAULT_KEEP_ALIVE_PROBES = 7;
    private static final int DEFAULT_KEEP_ALIVE_TIME = 1973;
    private static final int DEFAULT_KEEP_ALIVE_INTVL = 53;

    public static void main(String args[]) throws IOException {

        try (ServerSocket ss = new ServerSocket(0);
                Socket s = new Socket(LOCAL_HOST, ss.getLocalPort());
                DatagramSocket ds = new DatagramSocket(0);
                MulticastSocket mc = new MulticastSocket(0)) {
            if (ss.supportedOptions().contains(ExtendedSocketOptions.TCP_KEEPIDLE)) {
                ss.setOption(ExtendedSocketOptions.TCP_KEEPIDLE, DEFAULT_KEEP_ALIVE_TIME);
                if (ss.getOption(ExtendedSocketOptions.TCP_KEEPIDLE) != DEFAULT_KEEP_ALIVE_TIME) {
                    throw new RuntimeException("Test failed, TCP_KEEPIDLE should have been " + DEFAULT_KEEP_ALIVE_TIME);
                }
            }
            if (ss.supportedOptions().contains(ExtendedSocketOptions.TCP_KEEPCOUNT)) {
                ss.setOption(ExtendedSocketOptions.TCP_KEEPCOUNT, DEFAULT_KEEP_ALIVE_PROBES);
                if (ss.getOption(ExtendedSocketOptions.TCP_KEEPCOUNT) != DEFAULT_KEEP_ALIVE_PROBES) {
                    throw new RuntimeException("Test failed, TCP_KEEPCOUNT should have been " + DEFAULT_KEEP_ALIVE_PROBES);
                }
            }
            if (ss.supportedOptions().contains(ExtendedSocketOptions.TCP_KEEPINTERVAL)) {
                ss.setOption(ExtendedSocketOptions.TCP_KEEPINTERVAL, DEFAULT_KEEP_ALIVE_INTVL);
                if (ss.getOption(ExtendedSocketOptions.TCP_KEEPINTERVAL) != DEFAULT_KEEP_ALIVE_INTVL) {
                    throw new RuntimeException("Test failed, TCP_KEEPINTERVAL should have been " + DEFAULT_KEEP_ALIVE_INTVL);
                }
            }
            if (s.supportedOptions().contains(ExtendedSocketOptions.TCP_KEEPIDLE)) {
                s.setOption(ExtendedSocketOptions.TCP_KEEPIDLE, DEFAULT_KEEP_ALIVE_TIME);
                if (s.getOption(ExtendedSocketOptions.TCP_KEEPIDLE) != DEFAULT_KEEP_ALIVE_TIME) {
                    throw new RuntimeException("Test failed, TCP_KEEPIDLE should have been " + DEFAULT_KEEP_ALIVE_TIME);
                }
            }
            if (s.supportedOptions().contains(ExtendedSocketOptions.TCP_KEEPCOUNT)) {
                s.setOption(ExtendedSocketOptions.TCP_KEEPCOUNT, DEFAULT_KEEP_ALIVE_PROBES);
                if (s.getOption(ExtendedSocketOptions.TCP_KEEPCOUNT) != DEFAULT_KEEP_ALIVE_PROBES) {
                    throw new RuntimeException("Test failed, TCP_KEEPCOUNT should have been " + DEFAULT_KEEP_ALIVE_PROBES);
                }
            }
            if (s.supportedOptions().contains(ExtendedSocketOptions.TCP_KEEPINTERVAL)) {
                s.setOption(ExtendedSocketOptions.TCP_KEEPINTERVAL, DEFAULT_KEEP_ALIVE_INTVL);
                if (s.getOption(ExtendedSocketOptions.TCP_KEEPINTERVAL) != DEFAULT_KEEP_ALIVE_INTVL) {
                    throw new RuntimeException("Test failed, TCP_KEEPINTERVAL should have been " + DEFAULT_KEEP_ALIVE_INTVL);
                }
            }
            if (ds.supportedOptions().contains(ExtendedSocketOptions.TCP_KEEPCOUNT)) {
                throw new RuntimeException("Test failed, TCP_KEEPCOUNT is applicable"
                        + " for TCP Sockets only.");
            }
            if (ds.supportedOptions().contains(ExtendedSocketOptions.TCP_KEEPIDLE)) {
                throw new RuntimeException("Test failed, TCP_KEEPIDLE is applicable"
                        + " for TCP Sockets only.");
            }
            if (ds.supportedOptions().contains(ExtendedSocketOptions.TCP_KEEPINTERVAL)) {
                throw new RuntimeException("Test failed, TCP_KEEPINTERVAL is applicable"
                        + " for TCP Sockets only.");
            }
            if (mc.supportedOptions().contains(ExtendedSocketOptions.TCP_KEEPCOUNT)) {
                throw new RuntimeException("Test failed, TCP_KEEPCOUNT is applicable"
                        + " for TCP Sockets only");
            }
            if (mc.supportedOptions().contains(ExtendedSocketOptions.TCP_KEEPIDLE)) {
                throw new RuntimeException("Test failed, TCP_KEEPIDLE is applicable"
                        + " for TCP Sockets only");
            }
            if (mc.supportedOptions().contains(ExtendedSocketOptions.TCP_KEEPINTERVAL)) {
                throw new RuntimeException("Test failed, TCP_KEEPINTERVAL is applicable"
                        + " for TCP Sockets only");
            }
        }
    }
}
