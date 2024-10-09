



import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.nio.channels.ServerSocketChannel;

public class ServerSocketAdaptorTest {

    public static void main(String[] args) throws IOException {

        String message = null;

        try (ServerSocket s = new ServerSocket()) {
            s.accept();
            throw new AssertionError();
        } catch (IOException e) {
            message = e.getMessage();
        }

        try (ServerSocket ss = ServerSocketChannel.open().socket()) {

            assert !ss.isBound() : "the assumption !ss.isBound() doesn't hold";

            try {
                ss.accept();
                throw new AssertionError();
            } catch (Exception e) {
                if (e instanceof SocketException && message.equals(e.getMessage())) {
                    return;
                } else {
                    throw new AssertionError(
                            "Expected to throw SocketException with a particular message", e);
                }
            }
        }
    }
}
