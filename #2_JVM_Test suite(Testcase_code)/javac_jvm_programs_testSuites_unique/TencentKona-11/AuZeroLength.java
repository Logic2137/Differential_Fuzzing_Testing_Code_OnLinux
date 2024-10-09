

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;


public class AuZeroLength {

    public static String getString(byte b) {
        
        
        
        return String.valueOf(b);
    }


    public static void printFile(String filename) throws Exception {
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        String s = "";
        for (int i=0; i<data.length; i++) {
            s+=getString(data[i])+", ";
            if (s.length()>72) {
                System.out.println(s);
                s="";
            }
        }
        System.out.println(s);
    }

    public static void test(byte[] file) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(file);
        AudioFileFormat aff = AudioSystem.getAudioFileFormat(inputStream);

        if (aff.getFrameLength() != 0) {
            throw new Exception("File length is "+aff.getFrameLength()+" instead of 0. test FAILED");
        }
        System.out.println(aff.getType()+" file length is 0.");
    }

    public static void main(String[] args) throws Exception {
        test(ZERO_AU);
        test(ZERO_WAV);
        test(ZERO_AIFF);

        System.out.println("Test passed.");
    }

    public static byte[] ZERO_AU = {
        46, 115, 110, 100, 0, 0, 0, 32, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, -84, 68, 0,
        0, 0, 1, 116, 101, 115, 116, 46, 119, 97, 118
    };

    public static byte[] ZERO_WAV = {
        82, 73, 70, 70, 36, 0, 0, 0, 87, 65, 86, 69, 102, 109, 116, 32, 16, 0, 0,
        0, 1, 0, 1, 0, 68, -84, 0, 0, -120, 88, 1, 0, 2, 0, 16, 0, 100, 97, 116,
        97, 0, 0, 0, 0
    };

    public static byte[] ZERO_AIFF = {
        70, 79, 82, 77, 0, 0, 0, 46, 65, 73, 70, 70, 67, 79, 77, 77, 0, 0, 0, 18,
        0, 1, 0, 0, 0, 0, 0, 16, 64, 14, -84, 68, 0, 0, 0, 0, 0, 0, 83, 83, 78, 68,
        0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0
    };

}
