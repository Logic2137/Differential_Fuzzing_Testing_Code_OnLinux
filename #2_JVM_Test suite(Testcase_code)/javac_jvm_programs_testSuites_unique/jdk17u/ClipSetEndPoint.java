import java.io.ByteArrayInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

public class ClipSetEndPoint {

    private Clip theClip;

    boolean testPassed = true;

    public boolean runTest() {
        AudioInputStream theAudioInputStream = new AudioInputStream(new ByteArrayInputStream(new byte[2000]), new AudioFormat(8000.0f, 8, 1, false, false), 2000);
        AudioFormat theAudioFormat = theAudioInputStream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, theAudioFormat, AudioSystem.NOT_SPECIFIED);
        try {
            theClip = (Clip) AudioSystem.getLine(info);
            theClip.open(theAudioInputStream);
            int theStartLoopPoint = 0;
            int theEndLoopPoint = -1;
            theClip.setLoopPoints(theStartLoopPoint, theEndLoopPoint);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            testPassed = true;
        } catch (Exception e) {
            e.printStackTrace();
            testPassed = false;
        }
        return testPassed;
    }

    public static void main(String[] args) throws Exception {
        if (isSoundcardInstalled()) {
            ClipSetEndPoint thisTest = new ClipSetEndPoint();
            boolean testResult = thisTest.runTest();
            if (testResult) {
                System.out.println("Test passed");
            } else {
                System.out.println("Test failed");
                throw new Exception("Test failed");
            }
        }
    }

    public static boolean isSoundcardInstalled() {
        boolean result = false;
        try {
            Mixer.Info[] mixers = AudioSystem.getMixerInfo();
            if (mixers.length > 0) {
                result = AudioSystem.getSourceDataLine(null) != null;
            }
        } catch (Exception e) {
            System.err.println("Exception occured: " + e);
        }
        if (!result) {
            System.err.println("Soundcard does not exist or sound drivers not installed!");
            System.err.println("This test requires sound drivers for execution.");
        }
        return result;
    }
}
