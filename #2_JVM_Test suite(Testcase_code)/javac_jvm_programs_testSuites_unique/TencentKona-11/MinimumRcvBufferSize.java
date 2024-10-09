import java.nio.channels.*;
import java.net.*;

public class MinimumRcvBufferSize {

    public static void main(String[] args) throws Exception {
        boolean error = false;
        ServerSocketChannel channel = ServerSocketChannel.open();
        int before = channel.getOption(StandardSocketOptions.SO_RCVBUF);
        channel.setOption(StandardSocketOptions.SO_RCVBUF, Integer.MAX_VALUE);
        int after = channel.getOption(StandardSocketOptions.SO_RCVBUF);
        if (before > after) {
            System.err.println("Test failed: SO_RCVBUF");
            error = true;
        }
        SocketChannel channel1 = SocketChannel.open();
        before = channel1.getOption(StandardSocketOptions.SO_SNDBUF);
        channel1.setOption(StandardSocketOptions.SO_SNDBUF, Integer.MAX_VALUE);
        after = channel1.getOption(StandardSocketOptions.SO_SNDBUF);
        if (before > after) {
            System.err.println("Test failed: SO_SNDBUF");
            error = true;
        }
        if (error)
            throw new RuntimeException("Test failed");
    }
}
