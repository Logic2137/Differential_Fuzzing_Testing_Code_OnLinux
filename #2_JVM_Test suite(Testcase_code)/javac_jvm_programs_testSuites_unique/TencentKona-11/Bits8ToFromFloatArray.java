import java.util.Arrays;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import com.sun.media.sound.AudioFloatConverter;
import static javax.sound.sampled.AudioFormat.Encoding.*;

public final class Bits8ToFromFloatArray {

    private static final int SIZE = 8;

    private static final float[] FLOATS = { -1.0f, 0, 1.0f };

    private static final byte[] SIGNED = { Byte.MIN_VALUE, 0, Byte.MAX_VALUE };

    private static final byte[] UNSIGNED = { 0, (byte) (Byte.MAX_VALUE + 1), (byte) -1 };

    public static void main(final String[] args) {
        test(PCM_UNSIGNED, UNSIGNED);
        test(PCM_SIGNED, SIGNED);
    }

    private static void test(final Encoding enc, final byte[] expected) {
        AudioFormat af = new AudioFormat(enc, 44100, SIZE, 1, SIZE / 8, 44100, true);
        byte[] bytes = new byte[FLOATS.length * af.getFrameSize()];
        AudioFloatConverter conv = AudioFloatConverter.getConverter(af);
        conv.toByteArray(FLOATS, bytes);
        if (!Arrays.equals(bytes, expected)) {
            System.err.println("Actual: " + Arrays.toString(bytes));
            System.err.println("Expected: " + Arrays.toString(expected));
            throw new RuntimeException();
        }
        float[] floats = new float[bytes.length / af.getFrameSize()];
        conv.toFloatArray(bytes, floats);
        if (!Arrays.equals(floats, FLOATS)) {
            System.err.println("Actual: " + Arrays.toString(floats));
            System.err.println("Expected: " + Arrays.toString(FLOATS));
            throw new RuntimeException();
        }
    }
}
