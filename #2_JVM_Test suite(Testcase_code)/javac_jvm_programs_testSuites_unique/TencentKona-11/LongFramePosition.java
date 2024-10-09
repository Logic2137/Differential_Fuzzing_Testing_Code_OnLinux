

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


public class LongFramePosition {

    public static void main(String[] args) throws Exception {
        boolean failed = false;
        try {
            AudioFormat format = new AudioFormat(44100.0f, 16, 2, true, false);
            SourceDataLine sdl = AudioSystem.getSourceDataLine(format);
            try {
                sdl.open(format);
                sdl.start();
                sdl.write(new byte[16384], 0, 16384);
                Thread.sleep(1000);
                int intPos = sdl.getFramePosition();
                long longPos = sdl.getLongFramePosition();
                System.out.println("After 1 second: getFramePosition() = "+intPos);
                System.out.println("            getLongFramePosition() = "+longPos);
                if (intPos <= 0 || longPos <= 0) {
                    failed = true;
                    System.out.println("## FAILED: frame position did not advance, or negative!");
                }
                if (Math.abs(intPos - longPos) > 100) {
                    failed = true;
                    System.out.println("## FAILED: frame positions are not the same!");
                }
            } finally {
                sdl.close();
            }
        } catch (LineUnavailableException | IllegalArgumentException e) {
            System.out.println(e);
            System.out.println("Cannot execute test.");
            return;
        }
        if (failed) throw new RuntimeException("Test FAILED!");
        System.out.println("Test Passed.");
    }
}
