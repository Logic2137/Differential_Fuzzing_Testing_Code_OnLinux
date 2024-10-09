

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;


public final class RecognizeWaveExtensible {

    private static byte[] data = {
            82, 73, 70, 70, 72, 0, 0, 0, 87, 65, 86, 69, 102, 109, 116, 32, 40,
            0, 0, 0, -2, -1, 1, 0, 64, 31, 0, 0, 0, 125, 0, 0, 4, 0, 32, 0, 22,
            0, 32, 0, 4, 0, 0, 0, 1, 0, 0, 0, 0, 0, 16, 0, -128, 0, 0, -86, 0,
            56, -101, 113, 102, 97, 99, 116, 4, 0, 0, 0, 0, 0, 0, 0, 100, 97,
            116, 97, 0, 0, 0, 0
    };

    public static void main(final String[] args) throws Exception {
        final InputStream is = new ByteArrayInputStream(data);
        final AudioFileFormat aff = AudioSystem.getAudioFileFormat(is);
        System.out.println("AudioFileFormat: " + aff);
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(is)) {
            System.out.println("AudioFormat: " + ais.getFormat());
        }
        System.out.println("new String(data) = " + new String(data));
    }
}
