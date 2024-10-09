import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;

public class TestEncodingDecodingLength {

    public static void main(String[] args) {
        int size = Integer.MAX_VALUE - 8;
        byte[] inputBytes = new byte[size];
        byte[] outputBytes = new byte[size];
        Base64.Encoder encoder = Base64.getEncoder();
        checkOOM("encode(byte[])", () -> encoder.encode(inputBytes));
        checkIAE("encode(byte[] byte[])", () -> encoder.encode(inputBytes, outputBytes));
        checkOOM("encodeToString(byte[])", () -> encoder.encodeToString(inputBytes));
        checkOOM("encode(ByteBuffer)", () -> encoder.encode(ByteBuffer.wrap(inputBytes)));
        Arrays.fill(inputBytes, (byte) 86);
        Base64.Decoder decoder = Base64.getDecoder();
        decoder.decode(inputBytes);
        decoder.decode(inputBytes, outputBytes);
        decoder.decode(ByteBuffer.wrap(inputBytes));
    }

    private static final void checkOOM(String methodName, Runnable r) {
        try {
            r.run();
            throw new RuntimeException("OutOfMemoryError should have been thrown by: " + methodName);
        } catch (OutOfMemoryError er) {
        }
    }

    private static final void checkIAE(String methodName, Runnable r) {
        try {
            r.run();
            throw new RuntimeException("IllegalArgumentException should have been thrown by: " + methodName);
        } catch (IllegalArgumentException iae) {
        }
    }
}
