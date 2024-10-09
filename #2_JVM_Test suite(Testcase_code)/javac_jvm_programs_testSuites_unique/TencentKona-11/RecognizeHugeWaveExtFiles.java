

import java.io.ByteArrayInputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;


public final class RecognizeHugeWaveExtFiles {

    
    private static final  long MAX_UNSIGNED_INT = 0xffffffffL;

    
    private static final int[][] waveTypeBits = {
            {0xFFFE, 8}
    };

    
    private static final int[] sampleRates = {
            8000, 11025, 16000, 22050, 32000, 37800, 44056, 44100, 47250, 48000,
            50000, 50400, 88200, 96000, 176400, 192000, 352800, 2822400,
            5644800, Integer.MAX_VALUE
    };

    
    private static final int[] channels = {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    };

    
    private static final long[] dataSizes = {
            0, 1, 2, 3, Integer.MAX_VALUE - 1, Integer.MAX_VALUE,
            (long) Integer.MAX_VALUE + 1, MAX_UNSIGNED_INT - 1, MAX_UNSIGNED_INT
    };

    public static void main(final String[] args) throws Exception {
        for (final int[] type : waveTypeBits) {
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

    
    private static void testAFF(final int[] type, final int rate,
                                final int channel, final long size)
            throws Exception {
        final byte[] header = createHeader(type, rate, channel, size);
        final ByteArrayInputStream fake = new ByteArrayInputStream(header);
        final AudioFileFormat aff = AudioSystem.getAudioFileFormat(fake);
        final AudioFormat format = aff.getFormat();

        if (aff.getType() != AudioFileFormat.Type.WAVE) {
            throw new RuntimeException("Error");
        }

        final long frameLength = size / format.getFrameSize();
        if (frameLength <= Integer.MAX_VALUE) {
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
        validateFormat(type[1], rate, channel, aff.getFormat());
    }

    
    private static void testAIS(final int[] type, final int rate,
                                final int channel, final long size)
            throws Exception {
        final byte[] header = createHeader(type, rate, channel, size);
        final ByteArrayInputStream fake = new ByteArrayInputStream(header);
        final AudioInputStream ais = AudioSystem.getAudioInputStream(fake);
        final AudioFormat format = ais.getFormat();
        final long frameLength = size / format.getFrameSize();
        if (frameLength != ais.getFrameLength()) {
            System.err.println("Expected: " + frameLength);
            System.err.println("Actual: " + ais.getFrameLength());
            throw new RuntimeException();
        }
        if (ais.available() < 0) {
            System.err.println("available should be >=0: " + ais.available());
            throw new RuntimeException();
        }

        validateFormat(type[1], rate, channel, format);
    }

    
    private static void validateFormat(final int bits, final int rate,
                                       final int channel,
                                       final AudioFormat format) {

        if (Float.compare(format.getSampleRate(), rate) != 0) {
            System.err.println("Expected: " + rate);
            System.err.println("Actual: " + format.getSampleRate());
            throw new RuntimeException();
        }
        if (format.getChannels() != channel) {
            System.err.println("Expected: " + channel);
            System.err.println("Actual: " + format.getChannels());
            throw new RuntimeException();
        }
        int frameSize = ((bits + 7) / 8) * channel;
        if (format.getFrameSize() != frameSize) {
            System.err.println("Expected: " + frameSize);
            System.err.println("Actual: " + format.getFrameSize());
            throw new RuntimeException();
        }
    }

    
    private static byte[] createHeader(final int[] type, final int rate,
                                       final int channel, final long size) {
        final int frameSize = ((type[1] + 7) / 8) * channel;
        return new byte[]{
                
                0x52, 0x49, 0x46, 0x46,
                
                -1, -1, -1, -1,
                
                0x57, 0x41, 0x56, 0x45,
                
                0x66, 0x6d, 0x74, 0x20,
                
                40, 0, 0, 0,
                
                (byte) (type[0]), (byte) (type[0] >> 8),
                
                (byte) (channel), (byte) (channel >> 8),
                
                (byte) (rate), (byte) (rate >> 8), (byte) (rate >> 16),
                (byte) (rate >> 24),
                
                1, 0, 0, 0,
                
                (byte) (frameSize), (byte) (frameSize >> 8),
                
                (byte) type[1], 0,
                
                22, 0,
                
                8, 0,
                
                0, 0, 0, 0,
                
                
                0x3, 0x0, 0x0, 0x0,
                
                0x0, 0x0,
                
                0x10, 0,
                
                (byte) 0x80,
                
                0x0,
                
                0x0,
                
                (byte) 0xaa,
                
                0x0,
                
                0x38,
                
                (byte) 0x9b,
                
                0x71,
                
                0x64, 0x61, 0x74, 0x61,
                
                (byte) (size), (byte) (size >> 8), (byte) (size >> 16),
                (byte) (size >> 24)
                
                , 0, 0, 0, 0, 0
        };
    }
}
