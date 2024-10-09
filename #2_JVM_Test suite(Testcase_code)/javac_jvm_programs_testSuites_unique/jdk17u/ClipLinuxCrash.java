import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class ClipLinuxCrash {

    static Clip clip;

    public static long bytes2Ms(long bytes, AudioFormat format) {
        return (long) (bytes / format.getFrameRate() * 1000 / format.getFrameSize());
    }

    static int staticLen = 1000;

    static boolean addLen = true;

    public static long start() throws Exception {
        AudioFormat fmt = new AudioFormat(44100, 16, 2, true, false);
        if (addLen) {
            staticLen += (int) (staticLen / 5) + 1000;
        } else {
            staticLen -= (int) (staticLen / 5) + 1000;
        }
        if (staticLen > 8 * 44100 * 4) {
            staticLen = 8 * 44100 * 4;
            addLen = !addLen;
        }
        if (staticLen < 1000) {
            staticLen = 1000;
            addLen = !addLen;
        }
        int len = staticLen;
        len -= (len % 4);
        byte[] fakedata = new byte[len];
        InputStream is = new ByteArrayInputStream(fakedata);
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
        AudioInputStream ais = new AudioInputStream(is, format, fakedata.length / format.getFrameSize());
        out("    preparing to play back " + len + " bytes == " + bytes2Ms(len, format) + "ms audio...");
        DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
        clip = (Clip) AudioSystem.getLine(info);
        clip.addLineListener(new LineListener() {

            public void update(LineEvent e) {
                if (e.getType() == LineEvent.Type.STOP) {
                    out("    calling close() from event dispatcher thread");
                    ((Clip) e.getSource()).close();
                } else if (e.getType() == LineEvent.Type.CLOSE) {
                }
            }
        });
        out("    opening...");
        try {
            clip.open(ais);
        } catch (Throwable t) {
            t.printStackTrace();
            clip.close();
            clip = null;
        }
        ais.close();
        if (clip != null) {
            out("    starting...");
            clip.start();
        }
        return bytes2Ms(fakedata.length, format);
    }

    public static void main(String[] args) throws Exception {
        if (AudioSystem.getMixerInfo().length == 0) {
            System.out.println("Cannot execute test: no mixers installed!");
            System.out.println("Not Failed.");
            return;
        }
        try {
            int COUNT = 10;
            out();
            out("4498848 Sound causes crashes on Linux (testing with Clip)");
            if (args.length > 0) {
                COUNT = Integer.parseInt(args[0]);
            }
            for (int i = 0; i < COUNT; i++) {
                out("  trial " + (i + 1) + "/" + COUNT);
                start();
                int waitTime = 500 + (1000 * (i % 2));
                out("    waiting for " + waitTime + " ms for audio playback to stop...");
                Thread.sleep(waitTime);
                out("    calling close() from main thread");
                if (clip != null) {
                    clip.close();
                }
                out("    waiting for 2 seconds...");
                Thread.sleep(2000);
                out();
            }
            out("  waiting for 1 second...");
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
            out("  waiting for 1 second");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
            }
            throw e;
        }
        out("Test passed");
    }

    static void out() {
        out("");
    }

    static void out(String s) {
        System.out.println(s);
        System.out.flush();
    }
}
