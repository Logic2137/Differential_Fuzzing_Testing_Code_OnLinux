import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class AUwithULAW {

    public static void main(String[] args) throws Exception {
        System.out.println();
        System.out.println();
        System.out.println("4391108: Writing au files with ulaw encoding is broken");
        byte[] fakedata = new byte[1234];
        InputStream is = new ByteArrayInputStream(fakedata);
        AudioFormat inFormat = new AudioFormat(AudioFormat.Encoding.ULAW, 8000, 8, 1, 1, 8000, false);
        AudioInputStream ais = new AudioInputStream(is, inFormat, fakedata.length);
        ByteArrayOutputStream out = new ByteArrayOutputStream(1500);
        System.out.println("  ulaw data will be written as AU to stream...");
        int t = AudioSystem.write(ais, AudioFileFormat.Type.AU, out);
        byte[] writtenData = out.toByteArray();
        is = new ByteArrayInputStream(writtenData);
        System.out.println("  Get AudioFileFormat of written file");
        AudioFileFormat fileformat = AudioSystem.getAudioFileFormat(is);
        AudioFileFormat.Type type = fileformat.getType();
        System.out.println("  The file format type: " + type);
        if (fileformat.getFrameLength() != fakedata.length && fileformat.getFrameLength() != AudioSystem.NOT_SPECIFIED) {
            throw new Exception("The written file's frame length is " + fileformat.getFrameLength() + " but should be " + fakedata.length + " !");
        }
        ais = AudioSystem.getAudioInputStream(is);
        System.out.println("  Got Stream with format: " + ais.getFormat());
        System.out.println("  test passed.");
    }
}
