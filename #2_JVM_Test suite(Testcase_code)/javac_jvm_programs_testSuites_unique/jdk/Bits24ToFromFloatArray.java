

import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;

import com.sun.media.sound.AudioFloatConverter;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_UNSIGNED;


public final class Bits24ToFromFloatArray {

    private static final int SIZE = 24;

    private static final float[] FLOATS = {-1.0f, 0, 1.0f};

    private static int MIN_S = -8_388_608;
    private static int MAX_S = 8_388_607;

    private static int MID_U = 0xFFFFFF / 2 + 1;
    private static int MAX_U = 0xFFFFFF;

    
    private static final byte[] SIGNED_BIG = {
            (byte) ((MIN_S >> 16) & 0xff),
            (byte) ((MIN_S >> 8) & 0xff),
            (byte) ((MIN_S >> 0) & 0xff),
            0, 0, 0,
            (byte) ((MAX_S >> 16) & 0xff),
            (byte) ((MAX_S >> 8) & 0xff),
            (byte) ((MAX_S >> 0) & 0xff),
    };

    private static final byte[] UNSIGNED_BIG = {
            0, 0, 0,
            (byte) ((MID_U >> 16) & 0xff),
            (byte) ((MID_U >> 8) & 0xff),
            (byte) ((MID_U >> 0) & 0xff),
            (byte) ((MAX_U >> 16) & 0xff),
            (byte) ((MAX_U >> 8) & 0xff),
            (byte) ((MAX_U >> 0) & 0xff),

    };

    
    private static final byte[] SIGNED_LITTLE = {
            (byte) ((MIN_S >> 0) & 0xff),
            (byte) ((MIN_S >> 8) & 0xff),
            (byte) ((MIN_S >> 16) & 0xff),
            0, 0, 0,
            (byte) ((MAX_S >> 0) & 0xff),
            (byte) ((MAX_S >> 8) & 0xff),
            (byte) ((MAX_S >> 16) & 0xff),
            };

    private static final byte[] UNSIGNED_LITTLE = {
            0, 0, 0,
            (byte) ((MID_U >> 0) & 0xff),
            (byte) ((MID_U >> 8) & 0xff),
            (byte) ((MID_U >> 16) & 0xff),
            (byte) ((MAX_U >> 0) & 0xff),
            (byte) ((MAX_U >> 8) & 0xff),
            (byte) ((MAX_U >> 16) & 0xff),
            };

    public static void main(final String[] args) {
        test(PCM_UNSIGNED, UNSIGNED_BIG, true);
        test(PCM_UNSIGNED, UNSIGNED_LITTLE, false);
        test(PCM_SIGNED, SIGNED_LITTLE, false);
        test(PCM_SIGNED, SIGNED_BIG, true);
    }

    private static void test(final Encoding enc, final byte[] expected,
                             boolean end) {
        System.err.println(enc);
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
