

import java.io.ByteArrayInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;


public class ClipCloseLoss {
    static int frameCount = 441000; 
    static AudioFormat format = new AudioFormat(44100.0f, 16, 2, true, false);
    static ByteArrayInputStream bais =
    new ByteArrayInputStream(new byte[frameCount * format.getFrameSize()]);

    static int success = 0;
    static boolean failed = false;

    public static void run(Mixer m, long sleep) {
        Clip clip = null;
        try {
            if (m == null) {
                out("Using default mixer");
                clip = (Clip) AudioSystem.getClip();
            } else {
                out("Using mixer: "+m);
                DataLine.Info info = new DataLine.Info(Clip.class, format, AudioSystem.NOT_SPECIFIED);
                clip = (Clip) m.getLine(info);
            }
            out(" got clip: "+clip);
            if (!clip.getClass().toString().contains("Direct")) {
                out(" no direct audio clip -> do not test.");
                return;
            }

            out(" open");
            bais.reset();
            clip.open(new AudioInputStream(bais, format, frameCount));

            out(" clip.close()");
            
            Thread.sleep(sleep);
            
            clip.close();
            
            
            
            
            out(" clip closed");
            success++;
        } catch (LineUnavailableException luae) {
            
            System.err.println(luae);
        } catch (IllegalArgumentException iae) {
            
            System.err.println(iae);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static int getClipThreadCount() {
        int ret = 0;
        ThreadGroup tg = Thread.currentThread().getThreadGroup();
        while (tg.getParent() != null) { tg = tg.getParent(); }
        Thread[] threads = new Thread[500];
        int count = tg.enumerate(threads, true);
        for (int i = 0; i < count; i++) {
                if (threads[i].getName().contains("Direct")
                    && threads[i].getName().contains("Clip")) {
                        out("Found Direct Clip thread object: "+threads[i]);
                        ret++;
                }
        }
        return ret;
    }

    public static void main(String[] args) throws Exception    {
        if (isSoundcardInstalled()) {
            bais.mark(0);
            Mixer.Info[] infos = AudioSystem.getMixerInfo();
            for (int sleep = 0; sleep < 100; ++sleep) {
                run(null, sleep);
                for (int i = 0; i < infos.length; i++) {
                    try {
                        Mixer m = AudioSystem.getMixer(infos[i]);
                        run(m, sleep);
                    } catch (Exception e) {
                    }
                }
            }
            out("Waiting 1 second to dispose of all threads");
            Thread.sleep(1000);
            if (getClipThreadCount() > 0) {
                out("Unused clip threads exist! Causes test failure");
                failed = true;
            }
            if (failed) throw new Exception("Test FAILED!");
            if (success > 0) {
                out("Test passed.");
            } else {
                System.err.println("Test could not execute: please install an audio device");
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
            System.err.println("Exception occured: "+e);
        }
        if (!result) {
            System.err.println("Soundcard does not exist or sound drivers not installed!");
            System.err.println("This test requires sound drivers for execution.");
        }
        return result;
    }

    public static void out(String s) {
        
        System.out.println(s);
    }
}
