

import java.applet.AudioClip;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioSystem.NOT_SPECIFIED;


public final class AutoCloseTimeCheck {

    public static void main(final String[] args) throws Exception {
        
        File file = new File("audio.wav");
        try {
            AudioFormat format =
                    new AudioFormat(PCM_SIGNED, 44100, 8, 1, 1, 44100, false);
            AudioSystem.write(getStream(format), Type.WAVE, file);
        } catch (final Exception ignored) {
            return; 
        }
        try {
            testSmallDelay(file);
            testBigDelay(file);
        } finally {
            Files.delete(file.toPath());
        }
    }

    
    private static void testBigDelay(final File file) throws Exception {
        AudioClip clip = (AudioClip) file.toURL().getContent();
        clip.loop();
        clip.stop();
        sleep(20000); 
        if (count() != 0) {
            throw new RuntimeException("Thread was found");
        }
    }

    
    private static void testSmallDelay(final File file) throws IOException {
        AudioClip clip = (AudioClip) file.toURL().getContent();
        long threadID = 0;
        
        long endtime = System.nanoTime() + TimeUnit.SECONDS.toNanos(15);
        while (endtime - System.nanoTime() > 0) {
            clip.loop();
            sleep(500);

            long data = count();
            if (data != threadID) {
                System.out.println("Playing on new thread: " + data + " at "
                                           + new java.util.Date());
                if (threadID == 0) {
                    threadID = data;
                } else {
                    throw new RuntimeException("Thread was changed");
                }
            }

            clip.stop();
            sleep(500);
        }
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    private static long count() {
        for (final Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals("Direct Clip")) {
                return t.getId();
            }
        }
        return 0;
    }

    private static AudioInputStream getStream(final AudioFormat format) {
        final int dataSize = 5000 * format.getFrameSize();
        final InputStream in = new ByteArrayInputStream(new byte[dataSize]);
        return new AudioInputStream(in, format, NOT_SPECIFIED);
    }
}
