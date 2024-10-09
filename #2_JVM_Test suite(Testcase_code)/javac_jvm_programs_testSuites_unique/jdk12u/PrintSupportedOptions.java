



import java.io.IOException;
import java.net.SocketOption;
import java.nio.channels.*;

public class PrintSupportedOptions {

    @FunctionalInterface
    interface NetworkChannelSupplier<T extends NetworkChannel> {
        T get() throws IOException;
    }

    public static void main(String[] args) throws IOException {
        test(() -> SocketChannel.open());
        test(() -> ServerSocketChannel.open());
        test(() -> DatagramChannel.open());

        test(() -> AsynchronousSocketChannel.open());
        test(() -> AsynchronousServerSocketChannel.open());
    }

    @SuppressWarnings("unchecked")
    static <T extends NetworkChannel>
    void test(NetworkChannelSupplier<T> supplier) throws IOException {
        try (T ch = supplier.get()) {
            System.out.println(ch);
            for (SocketOption<?> opt : ch.supportedOptions()) {
                Object value = ch.getOption(opt);
                System.out.format(" %s -> %s%n", opt.name(), value);
                if (value != null) {
                    ch.setOption((SocketOption<Object>) opt, value);
                }
            }
        }
    }
}
