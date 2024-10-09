import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.spi.FormatConversionProvider;
import static java.util.ServiceLoader.load;

public final class GetTargetIsSupported {

    private static final List<AudioFormat> formats = new ArrayList<>(23000);

    private static final Encoding[] encodings = { Encoding.ALAW, Encoding.ULAW, Encoding.PCM_SIGNED, Encoding.PCM_UNSIGNED, Encoding.PCM_FLOAT, new Encoding("Test") };

    private static final int[] sampleRates = { AudioSystem.NOT_SPECIFIED, 8000, 11025, 16000, 22050, 32000, 37800, 44056, 44100, 47250, 48000, 50000, 50400, 88200, 96000, 176400, 192000, 352800, 2822400, 5644800 };

    private static final int[] sampleBits = { AudioSystem.NOT_SPECIFIED, 4, 8, 11, 16, 20, 24, 32, 48, 64, 128 };

    private static final int[] channels = { AudioSystem.NOT_SPECIFIED, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

    static {
        for (final Boolean end : new boolean[] { false, true }) {
            for (final int sampleSize : sampleBits) {
                for (final int sampleRate : sampleRates) {
                    for (final int channel : channels) {
                        for (final Encoding enc : encodings) {
                            formats.add(new AudioFormat(enc, sampleRate, sampleSize, channel, 1, sampleRate, end));
                        }
                    }
                }
            }
        }
    }

    public static void main(final String[] args) {
        for (final AudioFormat format : formats) {
            testAS(format);
            for (final FormatConversionProvider fcp : load(FormatConversionProvider.class)) {
                testFCP(fcp, format);
            }
        }
    }

    private static void testAS(final AudioFormat source) {
        Encoding[] all = AudioSystem.getTargetEncodings(source.getEncoding());
        Encoding[] part = AudioSystem.getTargetEncodings(source);
        for (final Encoding enc : part) {
            AudioFormat[] targets = AudioSystem.getTargetFormats(enc, source);
            for (final AudioFormat target : targets) {
                if (!AudioSystem.isConversionSupported(target, source)) {
                    throw new RuntimeException("Error:" + enc);
                }
                if (!enc.equals(target.getEncoding())) {
                    throw new RuntimeException("Error:" + enc);
                }
            }
            if (!AudioSystem.isConversionSupported(enc, source)) {
                throw new RuntimeException("Error:" + enc);
            }
            if (!Arrays.asList(all).contains(enc)) {
                throw new RuntimeException("Error:" + enc);
            }
            if (source.getEncoding().equals(enc)) {
                if (!isContains(source, targets)) {
                    throw new RuntimeException("Error:" + enc);
                }
            } else {
                if (targets.length == 0) {
                    throw new RuntimeException("Error:" + enc);
                }
            }
        }
        for (final Encoding enc : encodings) {
            AudioFormat[] targets = AudioSystem.getTargetFormats(enc, source);
            for (final AudioFormat target : targets) {
                if (!AudioSystem.isConversionSupported(target, source)) {
                    throw new RuntimeException("Error:" + enc);
                }
                if (!enc.equals(target.getEncoding())) {
                    throw new RuntimeException("Error:" + enc);
                }
            }
            if (AudioSystem.isConversionSupported(enc, source)) {
                if (!Arrays.asList(all).contains(enc)) {
                    throw new RuntimeException("Error:" + enc);
                }
                if (!Arrays.asList(part).contains(enc)) {
                    System.out.println("enc = " + enc);
                    System.out.println("part = " + Arrays.toString(part));
                    System.out.println("source = " + source);
                    throw new RuntimeException("Error:" + enc);
                }
                if (source.getEncoding().equals(enc)) {
                    if (!isContains(source, targets)) {
                        throw new RuntimeException("Error:" + enc);
                    }
                } else {
                    if (targets.length == 0) {
                        throw new RuntimeException("Error:" + enc);
                    }
                }
            } else {
                if (targets.length != 0) {
                    throw new RuntimeException("Error:" + enc);
                }
                if (Arrays.asList(part).contains(enc)) {
                    throw new RuntimeException("Error:" + enc);
                }
            }
        }
    }

    private static void testFCP(final FormatConversionProvider fcp, final AudioFormat source) {
        final Encoding[] all = fcp.getTargetEncodings();
        for (final Encoding enc : all) {
            if (!fcp.isTargetEncodingSupported(enc)) {
                throw new RuntimeException("Error:" + enc);
            }
        }
        final Encoding[] part = fcp.getTargetEncodings(source);
        for (final Encoding enc : part) {
            AudioFormat[] targets = fcp.getTargetFormats(enc, source);
            for (final AudioFormat target : targets) {
                if (!fcp.isConversionSupported(target, source)) {
                    throw new RuntimeException("Error:" + enc);
                }
                if (!enc.equals(target.getEncoding())) {
                    throw new RuntimeException("Error:" + enc);
                }
            }
            if (!fcp.isConversionSupported(enc, source)) {
                throw new RuntimeException("Error:" + enc);
            }
            if (targets.length == 0) {
                throw new RuntimeException("Error:" + enc);
            }
            if (!Arrays.asList(all).contains(enc)) {
                throw new RuntimeException("Error:" + enc);
            }
        }
        for (final Encoding enc : encodings) {
            AudioFormat[] targets = fcp.getTargetFormats(enc, source);
            for (final AudioFormat target : targets) {
                if (!fcp.isConversionSupported(target, source)) {
                    throw new RuntimeException("Error:" + enc);
                }
                if (!enc.equals(target.getEncoding())) {
                    throw new RuntimeException("Error:" + enc);
                }
            }
            if (fcp.isConversionSupported(enc, source)) {
                if (targets.length == 0) {
                    throw new RuntimeException("Error:" + enc);
                }
                if (!Arrays.asList(all).contains(enc)) {
                    throw new RuntimeException("Error:" + enc);
                }
                if (!Arrays.asList(part).contains(enc)) {
                    throw new RuntimeException("Error:" + enc);
                }
            } else {
                if (targets.length != 0) {
                    throw new RuntimeException("Error:" + enc);
                }
                if (Arrays.asList(part).contains(enc)) {
                    throw new RuntimeException("Error:" + enc);
                }
            }
        }
    }

    private static boolean isContains(AudioFormat obj, AudioFormat[] array) {
        for (final AudioFormat format : array) {
            if (obj.matches(format)) {
                return true;
            }
        }
        return false;
    }
}
