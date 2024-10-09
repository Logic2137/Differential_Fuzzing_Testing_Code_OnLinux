

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;


public class WriteAuUnspecifiedLength {

    public static void main(String argv[]) throws Exception {
        AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
        InputStream is = new ByteArrayInputStream(new byte[1000]);
        AudioInputStream ais = new AudioInputStream(is, format, AudioSystem.NOT_SPECIFIED);
        AudioSystem.write(ais, AudioFileFormat.Type.AU, new ByteArrayOutputStream());
        System.out.println("Test passed.");
    }
}
