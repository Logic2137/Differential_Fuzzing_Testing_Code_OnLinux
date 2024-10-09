

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.spi.AudioFileWriter;

import static java.util.ServiceLoader.load;
import static javax.sound.sampled.AudioFileFormat.Type.AIFC;
import static javax.sound.sampled.AudioFileFormat.Type.AIFF;
import static javax.sound.sampled.AudioFileFormat.Type.AU;
import static javax.sound.sampled.AudioFileFormat.Type.SND;
import static javax.sound.sampled.AudioFileFormat.Type.WAVE;


public final class WriteUnsupportedAudioFormat {

    
    private static final List<AudioFormat> formats = new ArrayList<>(23000);

    private static final AudioFormat.Encoding[] encodings = {
            AudioFormat.Encoding.ALAW, AudioFormat.Encoding.ULAW,
            AudioFormat.Encoding.PCM_SIGNED, AudioFormat.Encoding.PCM_UNSIGNED,
            AudioFormat.Encoding.PCM_FLOAT, new AudioFormat.Encoding("Test")
    };

    private static final int[] sampleRates = {
             8000, 11025, 16000, 22050, 32000,
            37800, 44056, 44100, 47250, 48000, 50000, 50400, 88200, 96000,
            176400, 192000, 352800, 2822400, 5644800
    };

    private static final int[] sampleBits = {
             8, 16, 24,
            32
    };

    public static final int BUFFER_LEN = 127;

    private static final int[] channels = {
             1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    };

    static final Type[] types = {
            WAVE, AU, AIFF, AIFC, SND, new Type("TestName", "TestExt")
    };

    private static final File FILE;

    static {
        try {
            FILE = File.createTempFile("sound", null);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        for (final Boolean end : new boolean[]{false, true}) {
            for (final int sampleSize : sampleBits) {
                for (final int sampleRate : sampleRates) {
                    for (final int channel : channels) {
                        for (final AudioFormat.Encoding enc : encodings) {

                            if (enc.equals(AudioFormat.Encoding.PCM_FLOAT)
                                    && sampleSize != 32) {
                                continue;
                            }
                            if (enc.equals(AudioFormat.Encoding.ALAW)
                                    && sampleSize != 8) {
                                continue;
                            }
                            if (enc.equals(AudioFormat.Encoding.ULAW)
                                    && sampleSize != 8) {
                                continue;
                            }

                            final int frameSize = ((sampleSize + 7) / 8)
                                    * channel;
                            formats.add(
                                    new AudioFormat(enc, sampleRate, sampleSize,
                                                    channel, frameSize,
                                                    sampleRate, end));
                        }
                    }
                }
            }
        }
    }

    public static void main(final String[] args) throws Exception {
        for (final AudioFileFormat.Type type : types) {
            for (final AudioFormat format : formats) {
                testAS(type, format);
                for (final AudioFileWriter afw : load(AudioFileWriter.class)) {
                    testAFW(afw, type, format);
                }
            }
        }
        Files.delete(Paths.get(FILE.getAbsolutePath()));
    }

    
    private static void testAS(final AudioFileFormat.Type type,
                               final AudioFormat format) throws Exception {
        final AudioInputStream ais = getStream(format);
        final OutputStream buffer = new ByteArrayOutputStream(BUFFER_LEN);

        if (AudioSystem.isFileTypeSupported(type, ais)) {
            if (!AudioSystem.isFileTypeSupported(type)) {
                throw new RuntimeException(type + ", " + format);
            }
            try {
                AudioSystem.write(ais, type, buffer);
                AudioSystem.write(ais, type, FILE);
            } catch (final IllegalArgumentException e) {
                throw new RuntimeException(type + ", " + format, e);
            }
        } else {
            try {
                AudioSystem.write(ais, type, buffer);
                throw new RuntimeException(type + ", " + format);
            } catch (final IllegalArgumentException ignored) {
            }
            try {
                AudioSystem.write(ais, type, FILE);
                throw new RuntimeException(type + ", " + format);
            } catch (final IllegalArgumentException ignored) {
            }
        }
    }

    
    private static void testAFW(final AudioFileWriter afw,
                                final AudioFileFormat.Type type,
                                final AudioFormat format) throws Exception {
        final AudioInputStream ais = getStream(format);
        final OutputStream buffer = new ByteArrayOutputStream(BUFFER_LEN);

        if (afw.isFileTypeSupported(type, ais)) {
            if (!afw.isFileTypeSupported(type)) {
                throw new RuntimeException(type + "," + format + ',' + afw);
            }
            try {
                afw.write(ais, type, buffer);
                afw.write(ais, type, FILE);
            } catch (final IllegalArgumentException e) {
                throw new RuntimeException(type + "," + format + ',' + afw, e);
            }
        } else {
            try {
                afw.write(ais, type, buffer);
                throw new RuntimeException(type + "," + format + ',' + afw);
            } catch (final IllegalArgumentException ignored) {
            }
            try {
                afw.write(ais, type, FILE);
                throw new RuntimeException(type + "," + format + ',' + afw);
            } catch (final IllegalArgumentException ignored) {
            }
        }
    }

    private static AudioInputStream getStream(final AudioFormat format) {
        final InputStream in = new ByteArrayInputStream(new byte[BUFFER_LEN]);
        return new AudioInputStream(in, format, 10);
    }
}
