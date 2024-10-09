

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;


public class AlawUlaw {
    static ByteArrayInputStream in;
    static int byteLength = 1000;

    static boolean failed = false;

    public static void main(String[] args) throws Exception {
        
        byte[] soundData = new byte[byteLength];
        for (int i=0; i<soundData.length; i++) {
            soundData[i] = (byte) ((i % 256) - 128);
        }

        
        in = new ByteArrayInputStream(soundData);
        in.mark(1);

        test1(PCM_FORMAT_1, ULAW_FORMAT_1, ULAW_FORMAT_2);
        test1(PCM_FORMAT_2, ULAW_FORMAT_1, ULAW_FORMAT_2);
        test2(ULAW_FORMAT_1, ULAW_FORMAT_2, PCM_FORMAT_1);
        test2(ULAW_FORMAT_1, ULAW_FORMAT_2, PCM_FORMAT_2);

        test1(PCM_FORMAT_1, ALAW_FORMAT_1, ALAW_FORMAT_2);
        test1(PCM_FORMAT_2, ALAW_FORMAT_1, ALAW_FORMAT_2);
        test2(ALAW_FORMAT_1, ALAW_FORMAT_2, PCM_FORMAT_1);
        test2(ALAW_FORMAT_1, ALAW_FORMAT_2, PCM_FORMAT_2);

        if (failed) {
                throw new Exception("Test failed!");
        }
    }

    public static String printFormat(AudioFormat format) {
        return format.toString()+"  "+(format.isBigEndian()?"big":"little")+" endian";
    }


    public static void test1(AudioFormat inFormat, AudioFormat outFormat1, AudioFormat outFormat2) throws Exception {
        AudioInputStream inStream = new AudioInputStream(in, inFormat, -1);
        System.out.println("Input Format: " + printFormat(inStream.getFormat()));

        
        AudioInputStream stream1 = AudioSystem.getAudioInputStream(outFormat1, inStream);
        System.out.println("Output Format 1: " + printFormat(stream1.getFormat()));

        
        AudioInputStream stream2 = AudioSystem.getAudioInputStream(outFormat2, inStream);
        System.out.println("Output Format 2: " + printFormat(stream2.getFormat()));

        compareStreams(stream1, stream2);
    }

    public static void test2(AudioFormat inFormat1, AudioFormat inFormat2, AudioFormat outFormat) throws Exception {
        AudioInputStream inStream1 = new AudioInputStream(in, inFormat1, -1);
        System.out.println("Input Format1: " + printFormat(inStream1.getFormat()));

        
        AudioInputStream stream1 = AudioSystem.getAudioInputStream(outFormat, inStream1);
        System.out.println("Output Format 1: " + printFormat(stream1.getFormat()));

        AudioInputStream inStream2 = new AudioInputStream(in, inFormat2, -1);
        System.out.println("Input Format1: " + printFormat(inStream2.getFormat()));

        
        AudioInputStream stream2 = AudioSystem.getAudioInputStream(outFormat, inStream2);
        System.out.println("Output Format 2: " + printFormat(stream2.getFormat()));

        compareStreams(stream1, stream2);
    }

    public static void compareStreams(InputStream stream1, InputStream stream2) throws Exception {
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();

        in.reset();
        writeDirectly(stream1, baos1);
        in.reset();
        writeDirectly(stream2, baos2);

        if (baos1.size() != baos2.size()) {
            System.out.println("   stream1 has length = "+baos1.size()+", stream2 has length = "+baos2.size());
        }
        int len = baos1.size();
        if (len > baos2.size()) {
                len = baos2.size();
        }
        byte[] data1=baos1.toByteArray();
        byte[] data2=baos2.toByteArray();
        for (int i=0; i<len; i++) {
                if (data1[i] != data2[i]) {
                        System.out.println("  FAILED! Difference encountered at position "+i);
                        failed = true;
                        return;
                }
        }
        if (baos1.size() != baos2.size()) {
                System.out.println("  No difference, but different length!");
                failed = true;
                return;
        }
        System.out.println("   PASSED");
    }

    public static void writeDirectly(InputStream in, OutputStream out) throws Exception {
            
            byte tmp[] = new byte[16384];
            while (true) {
                int bytesRead = in.read(tmp, 0, tmp.length);
                if (bytesRead == -1) {
                        break;
                }
                out.write(tmp, 0, bytesRead);
            } 
    }

    public static final AudioFormat PCM_FORMAT_1 =
        new AudioFormat( AudioFormat.Encoding.PCM_SIGNED,
                         8000f, 
                         16, 
                         1, 
                         2, 
                         8000f, 
                         false); 
    public static final AudioFormat PCM_FORMAT_2 =
        new AudioFormat( AudioFormat.Encoding.PCM_SIGNED,
                         8000f, 
                         16, 
                         1, 
                         2, 
                         8000f, 
                         true); 

    public static final AudioFormat ULAW_FORMAT_1 =
        new AudioFormat( AudioFormat.Encoding.ULAW,
                         8000f, 
                         8, 
                         1, 
                         1, 
                         8000f, 
                         false); 

    public static final AudioFormat ULAW_FORMAT_2 =
        new AudioFormat( AudioFormat.Encoding.ULAW,
                         8000f, 
                         8, 
                         1, 
                         1, 
                         8000f, 
                         true); 

    public static final AudioFormat ALAW_FORMAT_1 =
        new AudioFormat( AudioFormat.Encoding.ALAW,
                         8000f, 
                         8, 
                         1, 
                         1, 
                         8000f, 
                         false); 

    public static final AudioFormat ALAW_FORMAT_2 =
        new AudioFormat( AudioFormat.Encoding.ALAW,
                         8000f, 
                         8, 
                         1, 
                         1, 
                         8000f, 
                         true); 
}
