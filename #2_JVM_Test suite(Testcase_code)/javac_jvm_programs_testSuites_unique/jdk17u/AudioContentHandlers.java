import java.applet.AudioClip;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import static javax.sound.sampled.AudioFileFormat.Type.AIFC;
import static javax.sound.sampled.AudioFileFormat.Type.AIFF;
import static javax.sound.sampled.AudioFileFormat.Type.AU;
import static javax.sound.sampled.AudioFileFormat.Type.SND;
import static javax.sound.sampled.AudioFileFormat.Type.WAVE;

public final class AudioContentHandlers {

    private static final List<AudioFormat> formats = new ArrayList<>();

    private static final AudioFormat.Encoding[] encodings = { AudioFormat.Encoding.ALAW, AudioFormat.Encoding.ULAW, AudioFormat.Encoding.PCM_SIGNED, AudioFormat.Encoding.PCM_UNSIGNED, AudioFormat.Encoding.PCM_FLOAT };

    private static final AudioFileFormat.Type[] types = { WAVE, AU, AIFF, AIFC, SND };

    static {
        for (final AudioFormat.Encoding enc : encodings) {
            formats.add(new AudioFormat(enc, 44100, 8, 1, 1, 44100, true));
            formats.add(new AudioFormat(enc, 44100, 8, 1, 1, 44100, false));
        }
    }

    public static void main(final String[] args) throws Exception {
        for (final AudioFileFormat.Type type : types) {
            for (final AudioFormat format : formats) {
                File file = new File("audio." + type.getExtension());
                try {
                    AudioSystem.write(getStream(format), type, file);
                } catch (IOException | IllegalArgumentException ignored) {
                    continue;
                }
                AudioClip content;
                try {
                    content = (AudioClip) file.toURL().getContent();
                    generateOOME();
                } finally {
                    Files.delete(file.toPath());
                }
                if (content == null) {
                    throw new RuntimeException("Content is null");
                }
            }
        }
    }

    private static AudioInputStream getStream(final AudioFormat format) {
        final InputStream in = new ByteArrayInputStream(new byte[100]);
        return new AudioInputStream(in, format, 10);
    }

    private static void generateOOME() throws Exception {
        List<Object> leak = new LinkedList<>();
        try {
            while (true) {
                leak.add(new byte[1024 * 1024]);
            }
        } catch (OutOfMemoryError ignored) {
        }
        Thread.sleep(2000);
    }
}
