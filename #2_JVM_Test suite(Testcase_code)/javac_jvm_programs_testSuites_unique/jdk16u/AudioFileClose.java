

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;


public final class AudioFileClose {

    public static void main(final String[] args) throws Exception {
        final File file = Files.createTempFile("JavaSound", "Test").toFile();
        try (OutputStream fos = new FileOutputStream(file)) {
            fos.write(new byte[200]);
        }
        try {
            final InputStream stream = AudioSystem.getAudioInputStream(file);
            stream.close();
        } catch (final IOException | UnsupportedAudioFileException ignored) {
        }
        Files.delete(Paths.get(file.getAbsolutePath()));
    }
}
