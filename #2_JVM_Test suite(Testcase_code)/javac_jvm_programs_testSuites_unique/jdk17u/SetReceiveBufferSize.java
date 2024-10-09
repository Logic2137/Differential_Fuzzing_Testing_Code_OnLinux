import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.ServerSocket;

public class SetReceiveBufferSize {

    public static void main(String[] args) throws Exception {
        SetReceiveBufferSize s = new SetReceiveBufferSize();
    }

    public SetReceiveBufferSize() throws Exception {
        ServerSocket ss = new ServerSocket();
        InetAddress loopback = InetAddress.getLoopbackAddress();
        ss.bind(new InetSocketAddress(loopback, 0));
        Socket s = new Socket(loopback, ss.getLocalPort());
        Socket accepted = ss.accept();
        try {
            s.setReceiveBufferSize(0);
        } catch (IllegalArgumentException e) {
            return;
        } catch (Exception ex) {
        } finally {
            ss.close();
            s.close();
            accepted.close();
        }
        throw new RuntimeException("IllegalArgumentException not thrown!");
    }
}
