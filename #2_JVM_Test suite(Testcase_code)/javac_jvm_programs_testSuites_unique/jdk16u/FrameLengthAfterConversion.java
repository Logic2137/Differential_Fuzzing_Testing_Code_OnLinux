

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileWriter;
import javax.sound.sampled.spi.FormatConversionProvider;

import static java.util.ServiceLoader.load;
import static javax.sound.sampled.AudioFileFormat.Type.AIFC;
import static javax.sound.sampled.AudioFileFormat.Type.AIFF;
import static javax.sound.sampled.AudioFileFormat.Type.AU;
import static javax.sound.sampled.AudioFileFormat.Type.SND;
import static javax.sound.sampled.AudioFileFormat.Type.WAVE;
import static javax.sound.sampled.AudioSystem.NOT_SPECIFIED;


public final class FrameLengthAfterConversion {

    
    private static final List<AudioFormat> formats = new ArrayList<>(23000);

    private static final AudioFormat.Encoding[] encodings = {
            AudioFormat.Encoding.ALAW, AudioFormat.Encoding.ULAW,
            AudioFormat.Encoding.PCM_SIGNED, AudioFormat.Encoding.PCM_UNSIGNED,
            AudioFormat.Encoding.PCM_FLOAT, new AudioFormat.Encoding("Test")
    };

    private static final int[] sampleBits = {
            1, 4, 8, 11, 16, 20, 24, 32
    };

    private static final int[] channels = {
            1, 2, 3, 4, 5
    };

    private static final AudioFileFormat.Type[] types = {
            WAVE, AU, AIFF, AIFC, SND,
            new AudioFileFormat.Type("TestName", "TestExt")
    };

    private static final int FRAME_LENGTH = 10;

    static {
        for (final int sampleSize : sampleBits) {
            for (final int channel : channels) {
                for (final AudioFormat.Encoding enc : encodings) {
                    final int frameSize = ((sampleSize + 7) / 8) * channel;
                    formats.add(new AudioFormat(enc, 44100, sampleSize, channel,
                                                frameSize, 44100, true));
                    formats.add(new AudioFormat(enc, 44100, sampleSize, channel,
                                                frameSize, 44100, false));
                }
            }
        }
    }

    public static void main(final String[] args) throws IOException {
        for (final FormatConversionProvider fcp : load(
                FormatConversionProvider.class)) {
            System.out.println("fcp = " + fcp);
            for (final AudioFormat from : formats) {
                for (final AudioFormat to : formats) {
                    testAfterConversion(fcp, to, getStream(from, true));
                }
            }
        }

        for (final AudioFileWriter afw : load(AudioFileWriter.class)) {
            System.out.println("afw = " + afw);
            for (final AudioFileFormat.Type type : types) {
                for (final AudioFormat from : formats) {
                    testAfterSaveToStream(afw, type, getStream(from, true));
                }
            }
        }

        for (final AudioFileWriter afw : load(AudioFileWriter.class)) {
            System.out.println("afw = " + afw);
            for (final AudioFileFormat.Type type : types) {
                for (final AudioFormat from : formats) {
                    testAfterSaveToFile(afw, type, getStream(from, true));
                }
            }
        }

        for (final AudioFileWriter afw : load(AudioFileWriter.class)) {
            System.out.println("afw = " + afw);
            for (final AudioFileFormat.Type type : types) {
                for (final AudioFormat from : formats) {
                    testAfterSaveToFile(afw, type, getStream(from, false));
                }
            }
        }
    }

    
    private static void testAfterSaveToStream(final AudioFileWriter afw,
                                              final AudioFileFormat.Type type,
                                              final AudioInputStream ais)
            throws IOException {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            afw.write(ais, type, out);
            final InputStream input = new ByteArrayInputStream(
                    out.toByteArray());
            validate(AudioSystem.getAudioInputStream(input).getFrameLength());
        } catch (IllegalArgumentException | UnsupportedAudioFileException
                ignored) {
        }
    }

    
    private static void testAfterSaveToFile(final AudioFileWriter afw,
                                            final AudioFileFormat.Type type,
                                            AudioInputStream ais)
            throws IOException {
        final File temp = File.createTempFile("sound", ".tmp");
        try {
            afw.write(ais, type, temp);
            ais = AudioSystem.getAudioInputStream(temp);
            final long frameLength = ais.getFrameLength();
            ais.close();
            validate(frameLength);
        } catch (IllegalArgumentException | UnsupportedAudioFileException
                ignored) {
        } finally {
            Files.delete(Paths.get(temp.getAbsolutePath()));
        }
    }

    
    private static void testAfterConversion(final FormatConversionProvider fcp,
                                            final AudioFormat to,
                                            final AudioInputStream ais) {
        if (fcp.isConversionSupported(to, ais.getFormat())) {
            validate(fcp.getAudioInputStream(to, ais).getFrameLength());
        }
    }

    
    private static void validate(final long frameLength) {
        if (frameLength != FRAME_LENGTH) {
            System.err.println("Expected: " + FRAME_LENGTH);
            System.err.println("Actual: " + frameLength);
            throw new RuntimeException();
        }
    }

    private static AudioInputStream getStream(final AudioFormat format,
                                              final boolean frameLength) {
        final int dataSize = FRAME_LENGTH * format.getFrameSize();
        final InputStream in = new ByteArrayInputStream(new byte[dataSize]);
        if (frameLength) {
            return new AudioInputStream(in, format, FRAME_LENGTH);
        } else {
            return new AudioInputStream(in, format, NOT_SPECIFIED);
        }
    }
}
