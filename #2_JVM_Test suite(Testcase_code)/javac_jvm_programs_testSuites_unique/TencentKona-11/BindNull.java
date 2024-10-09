import java.io.*;
import java.net.*;
import java.nio.channels.*;

public class BindNull {

    public static void main(String[] args) throws IOException {
        try (DatagramChannel dc = DatagramChannel.open()) {
            dc.bind(null);
        }
        try (DatagramChannel dc = DatagramChannel.open(StandardProtocolFamily.INET)) {
            dc.bind(null);
        }
        try (DatagramChannel dc = DatagramChannel.open(StandardProtocolFamily.INET6)) {
            dc.bind(null);
        } catch (UnsupportedOperationException uoe) {
        }
    }
}
