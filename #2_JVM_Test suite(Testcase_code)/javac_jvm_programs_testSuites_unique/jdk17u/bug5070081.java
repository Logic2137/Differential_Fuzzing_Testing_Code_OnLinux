import java.util.concurrent.TimeUnit;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;

public class bug5070081 {

    static AudioFormat format = new AudioFormat(22050, 8, 1, false, false);

    static byte[] soundData = new byte[(int) (format.getFrameRate() * format.getFrameSize() * 3)];

    static final int LOOP_COUNT = 5;

    static boolean test() throws Exception {
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        Clip clip = null;
        boolean bSuccess = true;
        try {
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(format, soundData, 0, soundData.length);
        } catch (LineUnavailableException | IllegalArgumentException ignored) {
            return bSuccess;
        }
        long nLengthMS = clip.getMicrosecondLength() / 1000;
        System.out.println("  Clip length:");
        System.out.println("    frames: " + clip.getFrameLength());
        System.out.println("    seconds: " + nLengthMS / 1000.0);
        clip.start();
        Thread.sleep(1000);
        long time1 = currentTimeMillis();
        long pos1 = clip.getFramePosition();
        clip.stop();
        long pos2 = clip.getFramePosition();
        long time2 = currentTimeMillis();
        System.out.println("  Position before stop: " + pos1);
        System.out.println("  Position after stop: " + pos2);
        long timeDiff = Math.abs(time2 - time1);
        long posDiff = (long) (Math.abs(pos2 - pos1) / 22.05);
        System.out.println("  d(time): " + timeDiff + " ms;" + "d(clip pos time): " + posDiff + " ms.");
        long nDerivation = posDiff - timeDiff;
        if (nDerivation > 50) {
            System.out.println("  ERROR(1): The deviation is too much: " + nDerivation + " ms");
            bSuccess = false;
        }
        Thread.sleep(1000);
        clip.start();
        Thread.sleep(100);
        while (clip.isRunning()) ;
        int nEndPos = clip.getFramePosition();
        System.out.println("  Position at end: " + nEndPos);
        if (nEndPos > clip.getFrameLength()) {
            System.out.println("  ERROR(2): end position if out of range");
            bSuccess = false;
        }
        clip.close();
        return bSuccess;
    }

    public static void main(String[] args) throws Exception {
        for (int count = 1; count <= LOOP_COUNT; count++) {
            System.out.println("loop " + count + "/" + LOOP_COUNT);
            if (!test()) {
                System.out.println("Test FAILED");
                throw new RuntimeException("Test FAILED.");
            }
        }
        System.out.println("Test passed sucessfully");
    }

    private static long currentTimeMillis() {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    }
}
