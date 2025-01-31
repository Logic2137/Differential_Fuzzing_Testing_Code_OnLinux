import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.spi.FormatConversionProvider;
import static java.util.ServiceLoader.load;

public final class GetAudioStreamConversionSupported {

    static final AudioFormat.Encoding[] encodings = { AudioFormat.Encoding.ALAW, AudioFormat.Encoding.ULAW, AudioFormat.Encoding.PCM_SIGNED, AudioFormat.Encoding.PCM_UNSIGNED, AudioFormat.Encoding.PCM_FLOAT, new AudioFormat.Encoding("Test") };

    public static void main(final String[] args) {
        for (final int sampleSize : new int[] { 4, 8, 16, 24, 32 }) {
            for (final AudioFormat.Encoding enc : encodings) {
                for (final Boolean endian : new boolean[] { false, true }) {
                    testAS(enc, endian, sampleSize);
                    for (final FormatConversionProvider fcp : load(FormatConversionProvider.class)) {
                        testFCP(fcp, enc, endian, sampleSize);
                    }
                }
            }
        }
    }

    private static void testAS(final AudioFormat.Encoding enc, final Boolean endian, final int sampleSize) {
        final AudioInputStream ais = getStream(enc, endian, sampleSize);
        final AudioFormat format = ais.getFormat();
        if (!AudioSystem.isConversionSupported(enc, format)) {
            throw new RuntimeException("Format: " + format);
        }
        if (!AudioSystem.isConversionSupported(format, format)) {
            throw new RuntimeException("Format: " + format);
        }
        AudioSystem.getAudioInputStream(enc, ais);
        AudioSystem.getAudioInputStream(format, ais);
    }

    private static void testFCP(final FormatConversionProvider fcp, final AudioFormat.Encoding enc, final Boolean endian, final int sampleSize) {
        System.out.println("fcp = " + fcp);
        final AudioInputStream ais = getStream(enc, endian, sampleSize);
        final AudioFormat frmt = ais.getFormat();
        if (fcp.isConversionSupported(enc, frmt)) {
            try {
                fcp.getAudioInputStream(enc, ais);
            } catch (final IllegalArgumentException ex) {
                throw new RuntimeException("Format: " + frmt, ex);
            }
        } else {
            try {
                fcp.getAudioInputStream(enc, ais);
                throw new RuntimeException("Format: " + frmt);
            } catch (final IllegalArgumentException ignored) {
            }
            try {
                fcp.getAudioInputStream(frmt, ais);
                throw new RuntimeException("Format: " + frmt);
            } catch (final IllegalArgumentException ignored) {
            }
        }
        if (fcp.isConversionSupported(frmt, frmt)) {
            try {
                fcp.getAudioInputStream(enc, ais);
                fcp.getAudioInputStream(frmt, ais);
            } catch (final IllegalArgumentException ex) {
                throw new RuntimeException("Format: " + frmt, ex);
            }
        } else {
            try {
                fcp.getAudioInputStream(frmt, ais);
                throw new RuntimeException("Format: " + frmt);
            } catch (final IllegalArgumentException ignored) {
            }
        }
    }

    private static AudioInputStream getStream(final AudioFormat.Encoding enc, final Boolean end, final int sampleSize) {
        final AudioFormat ftmt = new AudioFormat(enc, 8000, sampleSize, 1, 1, 8000, end);
        final byte[] fakedata = new byte[100];
        final InputStream in = new ByteArrayInputStream(fakedata);
        return new AudioInputStream(in, ftmt, fakedata.length);
    }
}
