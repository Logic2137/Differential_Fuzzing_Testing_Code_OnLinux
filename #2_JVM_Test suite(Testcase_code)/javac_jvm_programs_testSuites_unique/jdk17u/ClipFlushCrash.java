import java.io.ByteArrayInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

public class ClipFlushCrash {

    static int frameCount = 441000;

    static AudioFormat format = new AudioFormat(44100.0f, 16, 2, true, false);

    static ByteArrayInputStream bais = new ByteArrayInputStream(new byte[frameCount * format.getFrameSize()]);

    static int success = 0;

    public static void run(Mixer m) {
        Clip clip = null;
        try {
            if (m == null) {
                out("Using default mixer");
                clip = (Clip) AudioSystem.getClip();
            } else {
                out("Using mixer: " + m);
                DataLine.Info info = new DataLine.Info(Clip.class, format, AudioSystem.NOT_SPECIFIED);
                clip = (Clip) m.getLine(info);
            }
            out(" got clip: " + clip);
            if (!clip.getClass().toString().contains("Direct")) {
                out(" no direct audio clip -> do not test.");
                return;
            }
            out(" open");
            bais.reset();
            clip.open(new AudioInputStream(bais, format, frameCount));
            AT at1 = new AT(clip, "flush thread", 123) {

                public void doAction() throws Exception {
                    log("flush");
                    clip.flush();
                }
            };
            AT at2 = new AT(clip, "setFramePosition thread", 67) {

                public void doAction() throws Exception {
                    int pos = (int) (Math.random() * clip.getFrameLength());
                    log("setPosition to frame " + pos);
                    clip.setFramePosition(pos);
                }
            };
            AT at3 = new AT(clip, "start/stop thread", 300) {

                public void doAction() throws Exception {
                    if (clip.isRunning()) {
                        log("stop");
                        clip.stop();
                    } else {
                        log("start");
                        clip.setFramePosition(0);
                        clip.start();
                    }
                }
            };
            AT at4 = new AT(clip, "open/close thread", 600) {

                public synchronized void doAction() throws Exception {
                    log("close");
                    clip.close();
                    wait(50);
                    if (!terminated) {
                        log("open");
                        bais.reset();
                        clip.open(new AudioInputStream(bais, format, frameCount));
                    }
                }
            };
            out(" clip.start");
            clip.start();
            out(" for 10 seconds, call start/stop, setFramePosition, and flush from other threads");
            at1.start();
            at2.start();
            at3.start();
            at4.start();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ie) {
            }
            out(" finished.");
            at1.terminate();
            at2.terminate();
            at3.terminate();
            at4.terminate();
            out(" clip.close()");
            clip.close();
            success++;
        } catch (LineUnavailableException luae) {
            System.err.println(luae);
        } catch (IllegalArgumentException iae) {
            System.err.println(iae);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        if (isSoundcardInstalled()) {
            bais.mark(0);
            run(null);
            Mixer.Info[] infos = AudioSystem.getMixerInfo();
            for (int i = 0; i < infos.length; i++) {
                try {
                    Mixer m = AudioSystem.getMixer(infos[i]);
                    run(m);
                } catch (Exception e) {
                }
            }
            if (success > 0) {
                out("No crash -> Test passed");
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
            System.err.println("Exception occured: " + e);
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

    private abstract static class AT extends Thread {

        protected boolean terminated = false;

        protected Clip clip;

        private int waitTime;

        public AT(Clip clip, String name, int waitTime) {
            super(name);
            this.clip = clip;
            this.waitTime = waitTime;
        }

        public abstract void doAction() throws Exception;

        public void run() {
            log("start");
            while (!terminated) {
                try {
                    synchronized (this) {
                        wait(waitTime);
                    }
                    if (!terminated) {
                        doAction();
                    }
                } catch (Exception e) {
                    log("exception: " + e);
                }
            }
            log("exit");
        }

        public synchronized void terminate() {
            log("terminate");
            terminated = true;
            notifyAll();
        }

        protected void log(String s) {
            out("   " + getName() + ": " + s);
        }
    }
}
