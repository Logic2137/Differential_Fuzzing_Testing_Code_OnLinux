

import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;

import com.sun.media.sound.AudioFloatConverter;

import static javax.sound.sampled.AudioFormat.Encoding.*;


public final class Bits16ToFromFloatArray {

    private static final int SIZE = 16;

    private static final float[] FLOATS = {-1.0f, 0, 1.0f};

    private static short MID_U = (short) (Short.MAX_VALUE + 1);
    private static short MAX_U = -1;

    
    private static final byte[] SIGNED_BIG = {
            (byte) (Short.MIN_VALUE >> 8), (byte) (Short.MIN_VALUE & 0xff), 0,
            0, (byte) (Short.MAX_VALUE >> 8), (byte) (Short.MAX_VALUE & 0xff)
    };

    private static final byte[] UNSIGNED_BIG = {
            0, 0, (byte) (MID_U >> 8), (byte) (MID_U & 0xff),
            (byte) (MAX_U >> 8), (byte) (MAX_U >> 8)
    };

    
    private static final byte[] SIGNED_LITTLE = {
            (byte) (Short.MIN_VALUE & 0xff), (byte) (Short.MIN_VALUE >> 8), 0,
            0, (byte) (Short.MAX_VALUE & 0xff), (byte) (Short.MAX_VALUE >> 8)
    };

    private static final byte[] UNSIGNED_LITTLE = {
            0, 0, (byte) (MID_U & 0xff), (byte) (MID_U >> 8),
            (byte) (MAX_U >> 8), (byte) (MAX_U >> 8)
    };

    public static void main(final String[] args) {
        test(PCM_UNSIGNED, UNSIGNED_BIG, true);
        test(PCM_UNSIGNED, UNSIGNED_LITTLE, false);
        test(PCM_SIGNED, SIGNED_LITTLE, false);
        test(PCM_SIGNED, SIGNED_BIG, true);
    }

    private static void test(final Encoding enc, final byte[] expected,
                             boolean end) {
        System.err.println("enc = " + enc);
        AudioFormat af = new AudioFormat(enc, 44100, SIZE, 1, SIZE / 8, 44100,
                                         end);
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
