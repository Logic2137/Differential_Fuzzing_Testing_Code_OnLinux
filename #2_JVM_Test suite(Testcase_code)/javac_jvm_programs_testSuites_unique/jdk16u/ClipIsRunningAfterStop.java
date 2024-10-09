

import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;


public final class ClipIsRunningAfterStop {

    private static volatile Exception failed;

    public static void main(final String[] args) throws Exception {
        final Runnable r = () -> {
            try {
                test();
            } catch (LineUnavailableException | IllegalArgumentException ignored) {
                
            } catch (Exception ex) {
                failed = ex;
            }
        };
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        Thread t3 = new Thread(r);
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        if (failed != null) {
            throw new RuntimeException(failed);
        }
    }

    private static void test() throws Exception {
        
        long endtime = System.nanoTime() + TimeUnit.SECONDS.toNanos(15);
        while (failed == null && endtime - System.nanoTime() > 0) {
            Clip clip = createClip();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.stop();
            if (clip.isRunning()) {
                if (clip.isRunning()) {
                    throw new RuntimeException("Clip is running");
                }
            }
            if (clip.isActive()) {
                if (clip.isActive()) {
                    throw new RuntimeException("Clip is active");
                }
            }
            clip.close();
        }
    }

    private static Clip createClip() throws LineUnavailableException {
        AudioFormat format =
                new AudioFormat(PCM_SIGNED, 44100, 8, 1, 1, 44100, false);
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        Clip clip = (Clip) AudioSystem.getLine(info);
        clip.open(format, new byte[2], 0, 2);
        return clip;
    }
}
