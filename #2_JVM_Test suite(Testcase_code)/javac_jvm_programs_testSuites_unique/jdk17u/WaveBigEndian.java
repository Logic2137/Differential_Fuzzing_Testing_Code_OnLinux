import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class WaveBigEndian {

    static boolean failed = false;

    public static byte[] writeDataAndGetAIS(boolean bigEndian) throws Exception {
        if (bigEndian) {
            out("Create WAVE file from big endian data...");
        } else {
            out("Create WAVE file from little endian data...");
        }
        byte[] data = new byte[3000];
        for (int i = 0; i < data.length; i += 2) {
            if (bigEndian) {
                data[i] = (byte) i;
                data[i + 1] = (byte) (i + 1);
            } else {
                data[i] = (byte) (i + 1);
                data[i + 1] = (byte) i;
            }
        }
        AudioFormat format = new AudioFormat(44100.0f, 16, 1, true, bigEndian);
        InputStream is = new ByteArrayInputStream(data);
        AudioInputStream ais = new AudioInputStream(is, format, data.length);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int written = AudioSystem.write(ais, AudioFileFormat.Type.WAVE, os);
        data = os.toByteArray();
        out("Wrote " + written + " bytes, got " + data.length + " bytes in written file.");
        is = new ByteArrayInputStream(data);
        ais = AudioSystem.getAudioInputStream(is);
        out("Got AIS with length = " + ais.getFrameLength() + " frames.");
        return data;
    }

    public static void main(String[] args) throws Exception {
        byte[] data1 = writeDataAndGetAIS(false);
        byte[] data2 = writeDataAndGetAIS(true);
        if (data1.length != data2.length) {
            out("# data1.length != data2.length!");
            failed = true;
        } else {
            for (int i = 0; i < data1.length; i++) {
                if (data1[i] != data2[i]) {
                    out("# At index " + i + ": le=" + (data1[i] & 0xFF) + " be=" + (data2[i] & 0xFF) + " !");
                    failed = true;
                }
            }
        }
        if (failed)
            throw new Exception("Test FAILED!");
        out("Files are identical.");
        out("test passed");
    }

    static void out(String s) {
        System.out.println(s);
    }
}
