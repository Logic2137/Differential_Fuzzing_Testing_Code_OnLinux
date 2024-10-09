import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ReadAfterReset {

    private static final PrintStream out = System.out;

    private static final int NUM_BYTES_TO_WRITE = 1000;

    public static void main(String[] args) throws IOException {
        try (ServerSocket ss = new ServerSocket()) {
            ss.bind(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0));
            out.println("Test connection ...");
            try (Socket s = new Socket()) {
                s.connect(ss.getLocalSocketAddress());
                int nwrote = acceptAndResetConnection(ss);
                int nread = readUntilIOException(s);
                if (nread != nwrote) {
                    throw new RuntimeException("Client read " + nread + ", expected " + nwrote);
                }
            }
            out.println();
            out.println("Test connection ...");
            try (Socket s = new Socket()) {
                s.connect(ss.getLocalSocketAddress());
                int nwrote = acceptAndResetConnection(ss);
                writeUntilIOException(s);
                int nread = readUntilIOException(s);
                if (nread != nwrote) {
                    throw new RuntimeException("Client read " + nread + ", expected " + nwrote);
                }
            }
        }
    }

    static int acceptAndResetConnection(ServerSocket ss) throws IOException {
        int count = NUM_BYTES_TO_WRITE;
        try (Socket peer = ss.accept()) {
            peer.getOutputStream().write(new byte[count]);
            peer.setSoLinger(true, 0);
            out.format("Server wrote %d bytes and reset connection%n", count);
        }
        return count;
    }

    static void writeUntilIOException(Socket s) {
        try {
            byte[] bytes = new byte[100];
            while (true) {
                s.getOutputStream().write(bytes);
                out.format("Client wrote %d bytes%n", bytes.length);
            }
        } catch (IOException ioe) {
            out.format("Client write failed: %s (expected)%n", ioe);
        }
    }

    static int readUntilIOException(Socket s) {
        int nread = 0;
        try {
            byte[] bytes = new byte[100];
            while (true) {
                int n = s.getInputStream().read(bytes);
                if (n < 0) {
                    out.println("Client read EOF");
                    break;
                } else {
                    out.format("Client read %s bytes%n", n);
                    nread += n;
                }
            }
        } catch (IOException ioe) {
            out.format("Client read failed: %s (expected)%n", ioe);
        }
        return nread;
    }
}
