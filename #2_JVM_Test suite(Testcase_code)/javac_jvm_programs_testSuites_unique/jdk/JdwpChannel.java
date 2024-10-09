

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;


public class JdwpChannel {

    private Socket sock;

    public void connect(int jdwpPort) throws IOException {
        sock = new Socket("localhost", jdwpPort);
        handshake();
    }

    
    private void handshake() throws IOException {
        final byte[] HANDSHAKE = "JDWP-Handshake".getBytes();
        sock.getOutputStream().write(HANDSHAKE, 0, HANDSHAKE.length);

        byte[] reply = new byte[14];
        sock.getInputStream().read(reply, 0, 14);
        if (!Arrays.equals(HANDSHAKE, reply)) {
            throw new RuntimeException("Error during handshake. Reply was: " + new String(reply) + " expected " + new String(HANDSHAKE));
        }
    }

    public void disconnect() {
        try {
            sock.close();
        } catch (IOException x) {
        }
    }

    public void write(byte[] data, int length) throws IOException {
        sock.getOutputStream().write(data, 0, length);
    }

    public InputStream getInputStream() throws IOException {
        return sock.getInputStream();
    }

}
