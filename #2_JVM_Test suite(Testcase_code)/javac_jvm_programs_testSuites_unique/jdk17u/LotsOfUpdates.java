import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class LotsOfUpdates {

    public static void main(String[] args) throws IOException {
        Selector sel = Selector.open();
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        SelectionKey key = sc.register(sel, 0);
        for (int i = 0; i < 50000; i++) {
            key.interestOps(0);
        }
        sel.selectNow();
    }
}
