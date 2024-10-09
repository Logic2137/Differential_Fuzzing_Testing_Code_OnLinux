import java.nio.channels.*;

public class BufferSize {

    public static void main(String[] args) throws Exception {
        ServerSocketChannel sc = ServerSocketChannel.open();
        try {
            sc.socket().setReceiveBufferSize(-1);
            throw new Exception("Illegal size accepted");
        } catch (IllegalArgumentException iae) {
        }
        try {
            sc.socket().setReceiveBufferSize(0);
            throw new Exception("Illegal size accepted");
        } catch (IllegalArgumentException iae) {
        }
        sc.close();
    }
}
