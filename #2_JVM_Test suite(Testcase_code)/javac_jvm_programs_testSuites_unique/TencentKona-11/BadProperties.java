



import java.nio.channels.AsynchronousSocketChannel;
import java.io.IOException;

public class BadProperties {
    public static void main(String[] args) throws IOException {
        AsynchronousSocketChannel.open();
    }
}
