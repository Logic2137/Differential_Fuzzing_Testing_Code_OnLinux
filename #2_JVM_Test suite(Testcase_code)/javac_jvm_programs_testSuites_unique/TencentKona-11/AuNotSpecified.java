

import java.io.ByteArrayInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;


public class AuNotSpecified {
    public static boolean failed = false;

    public static void main(String[] params) throws Exception {

        AudioInputStream is =
            AudioSystem.getAudioInputStream(new
                                            ByteArrayInputStream(new byte[] {
                                                (byte)0x2E, (byte)0x73, (byte)0x6E, (byte)0x64, (byte)0x00,
                                                (byte)0x00, (byte)0x00, (byte)0x18,
                                                (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0x00,
                                                (byte)0x00, (byte)0x00, (byte)0x03,
                                                (byte)0x00, (byte)0x00, (byte)0x1F, (byte)0x40, (byte)0x00,
                                                (byte)0x00, (byte)0x00, (byte)0x01,
                                                (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                                                (byte)0x00, (byte)0x00, (byte)0x00,
                                            }));
        if (is.getFrameLength() != AudioSystem.NOT_SPECIFIED) {
            System.out.println("frame length should be NOT_SPECIFIED, but is: "+is.getFrameLength());
            failed=true;
        }
        
        
        
        if (failed) throw new Exception("Test FAILED!");
        System.out.println("Test Passed.");
    }
}
