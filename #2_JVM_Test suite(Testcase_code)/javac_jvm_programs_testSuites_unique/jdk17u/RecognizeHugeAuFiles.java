import java.io.ByteArrayInputStream;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public final class RecognizeHugeAuFiles {

    private static final byte AU_HEADER = 44;

    private static final long MAX_UNSIGNED_INT = 0xffffffffL;

    private static final byte[][] auTypeBits = { { 1, 8 }, { 2, 8 }, { 3, 16 }, { 4, 24 }, { 5, 32 }, { 6, 32 }, { 27, 8 } };

    private static final int[] sampleRates = { 8000, 11025, 16000, 22050, 32000, 37800, 44056, 44100, 47250, 48000, 50000, 50400, 88200, 96000, 176400, 192000, 352800, 2822400, 5644800, Integer.MAX_VALUE };

    private static final int[] channels = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

    private static final long[] dataSizes = { 0, 1, 2, 3, Integer.MAX_VALUE - AU_HEADER, Integer.MAX_VALUE - 1, Integer.MAX_VALUE, (long) Integer.MAX_VALUE + 1, (long) Integer.MAX_VALUE + AU_HEADER, MAX_UNSIGNED_INT - AU_HEADER, MAX_UNSIGNED_INT - 1, MAX_UNSIGNED_INT };

    public static void main(final String[] args) throws Exception {
        for (final byte[] type : auTypeBits) {
            for (final int sampleRate : sampleRates) {
                for (final int channel : channels) {
                    for (final long dataSize : dataSizes) {
                        testAFF(type, sampleRate, channel, dataSize);
                        testAIS(type, sampleRate, channel, dataSize);
                    }
                }
            }
        }
    }

    private static void testAFF(final byte[] type, final int rate, final int channel, final long size) throws Exception {
        final byte[] header = createHeader(type, rate, channel, size);
        final ByteArrayInputStream fake = new ByteArrayInputStream(header);
        final AudioFileFormat aff = AudioSystem.getAudioFileFormat(fake);
        final AudioFormat format = aff.getFormat();
        if (aff.getType() != AudioFileFormat.Type.AU) {
            throw new RuntimeException("Error");
        }
        final long frameLength = size / format.getFrameSize();
        if (size != MAX_UNSIGNED_INT && frameLength <= Integer.MAX_VALUE) {
            if (aff.getFrameLength() != frameLength) {
                System.err.println("Expected: " + frameLength);
                System.err.println("Actual: " + aff.getFrameLength());
                throw new RuntimeException();
            }
        } else {
            if (aff.getFrameLength() != AudioSystem.NOT_SPECIFIED) {
                System.err.println("Expected: " + AudioSystem.NOT_SPECIFIED);
                System.err.println("Actual: " + aff.getFrameLength());
                throw new RuntimeException();
            }
        }
        final long byteLength = size + AU_HEADER;
        if (byteLength <= Integer.MAX_VALUE) {
            if (aff.getByteLength() != byteLength) {
                System.err.println("Expected: " + byteLength);
                System.err.println("Actual: " + aff.getByteLength());
                throw new RuntimeException();
            }
        } else {
            if (aff.getByteLength() != AudioSystem.NOT_SPECIFIED) {
                System.err.println("Expected: " + AudioSystem.NOT_SPECIFIED);
                System.err.println("Actual: " + aff.getByteLength());
                throw new RuntimeException();
            }
        }
        validateFormat(type[1], rate, channel, aff.getFormat());
    }

    private static void testAIS(final byte[] type, final int rate, final int channel, final long size) throws Exception {
        final byte[] header = createHeader(type, rate, channel, size);
        final ByteArrayInputStream fake = new ByteArrayInputStream(header);
        final AudioInputStream ais = AudioSystem.getAudioInputStream(fake);
        final AudioFormat format = ais.getFormat();
        final long frameLength = size / format.getFrameSize();
        if (size != MAX_UNSIGNED_INT) {
            if (frameLength != ais.getFrameLength()) {
                System.err.println("Expected: " + frameLength);
                System.err.println("Actual: " + ais.getFrameLength());
                throw new RuntimeException();
            }
        } else {
            if (ais.getFrameLength() != AudioSystem.NOT_SPECIFIED) {
                System.err.println("Expected: " + AudioSystem.NOT_SPECIFIED);
                System.err.println("Actual: " + ais.getFrameLength());
                throw new RuntimeException();
            }
        }
        if (ais.available() < 0) {
            System.err.println("available should be >=0: " + ais.available());
            throw new RuntimeException();
        }
        validateFormat(type[1], rate, channel, format);
    }

    private static void validateFormat(final byte bits, final int rate, final int channel, final AudioFormat format) {
        if (Float.compare(format.getSampleRate(), rate) != 0) {
            System.out.println("Expected: " + rate);
            System.out.println("Actual: " + format.getSampleRate());
            throw new RuntimeException();
        }
        if (format.getChannels() != channel) {
            System.out.println("Expected: " + channel);
            System.out.println("Actual: " + format.getChannels());
            throw new RuntimeException();
        }
        int frameSize = ((bits + 7) / 8) * channel;
        if (format.getFrameSize() != frameSize) {
            System.out.println("Expected: " + frameSize);
            System.out.println("Actual: " + format.getFrameSize());
            throw new RuntimeException();
        }
    }

    private static byte[] createHeader(final byte[] type, final int rate, final int channel, final long size) {
        return new byte[] { 0x2e, 0x73, 0x6e, 0x64, 0, 0, 0, AU_HEADER, (byte) (size >> 24), (byte) (size >> 16), (byte) (size >> 8), (byte) size, 0, 0, 0, type[0], (byte) (rate >> 24), (byte) (rate >> 16), (byte) (rate >> 8), (byte) (rate), (byte) (channel >> 24), (byte) (channel >> 16), (byte) (channel >> 8), (byte) (channel), 0, 0, 0, 0, 0, 0 };
    }
}
