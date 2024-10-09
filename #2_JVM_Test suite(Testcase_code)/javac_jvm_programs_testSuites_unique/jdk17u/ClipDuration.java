import java.io.ByteArrayInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

public class ClipDuration {

    public static int run(Mixer m) {
        int res = 1;
        int frameCount = 441000;
        AudioFormat f = new AudioFormat(44100.0f, 16, 2, true, false);
        AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(new byte[frameCount * f.getFrameSize()]), f, frameCount);
        AudioFormat format = audioInputStream.getFormat();
        Clip m_clip = null;
        try {
            if (m == null) {
                m_clip = (Clip) AudioSystem.getClip();
            } else {
                DataLine.Info info = new DataLine.Info(Clip.class, format, AudioSystem.NOT_SPECIFIED);
                m_clip = (Clip) m.getLine(info);
            }
            System.out.println("Got clip: " + m_clip);
            m_clip.open(audioInputStream);
            long microseconds = m_clip.getMicrosecondLength();
            System.out.println("getFrameLength()=" + m_clip.getFrameLength() + " frames");
            System.out.println("getMicrosecondLength()=" + microseconds + " us");
            if (Math.abs(microseconds - 10000000) < 50) {
                System.out.println("->Clip OK");
                res = 0;
            }
        } catch (LineUnavailableException luae) {
            System.err.println(luae);
            res = 3;
        } catch (Throwable t) {
            System.out.println("->Exception:" + t);
            t.printStackTrace();
            res = 2;
        }
        if (m_clip != null) {
            m_clip.close();
        }
        return res;
    }

    public static void main(String[] args) throws Exception {
        if (isSoundcardInstalled()) {
            int res = 3;
            res = run(null);
            Mixer.Info[] infos = AudioSystem.getMixerInfo();
            for (int i = 0; i < infos.length; i++) {
                try {
                    Mixer m = AudioSystem.getMixer(infos[i]);
                    int r = run(m);
                    if (r == 1)
                        res = 1;
                } catch (Exception e) {
                }
            }
            if (res != 1) {
                System.out.println("Test passed");
            } else {
                if (res == 2) {
                    System.err.println("Test could not execute: test threw unexpected Exception.");
                    throw new Exception("Test threw exception");
                } else if (res == 3) {
                    System.err.println("Test could not execute: please install an audio device");
                    return;
                }
                throw new Exception("Test returned wrong length");
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
