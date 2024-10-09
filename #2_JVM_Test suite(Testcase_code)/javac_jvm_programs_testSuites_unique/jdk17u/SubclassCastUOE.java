import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

public class SubclassCastUOE {

    public static void main(String[] args) throws Exception {
        ByteBuffer buf = ByteBuffer.allocateDirect(37);
        if (!(buf instanceof MappedByteBuffer)) {
            throw new RuntimeException("Direct buffer not a MappedByteBuffer");
        }
        if (((MappedByteBuffer) buf).load() != buf) {
            throw new RuntimeException("load() did not return same buffer");
        }
        if (!((MappedByteBuffer) buf).isLoaded()) {
            throw new RuntimeException("isLoaded() returned false");
        }
        if (((MappedByteBuffer) buf).force() != buf) {
            throw new RuntimeException("force() did not return same buffer");
        }
    }
}
