import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class DataLine_ArrayIndexOutOfBounds {

    static int total = 0;

    static int failed = 0;

    static final byte[] buffer = new byte[5000000];

    static abstract class Scenario {

        abstract int getBufferOffset(DataLine line);

        abstract int getBufferLength(DataLine line);
    }

    static Scenario[] scenarios = new Scenario[] { new Scenario() {

        public String toString() {
            return "offset is near Integer.MAX_VALUE";
        }

        public int getBufferOffset(DataLine line) {
            return Integer.MAX_VALUE - 4096;
        }

        public int getBufferLength(DataLine line) {
            return 65536;
        }
    }, new Scenario() {

        public String toString() {
            return "offset is less than buffer.length, length is large";
        }

        int getBufferOffset(DataLine line) {
            return buffer.length / 10;
        }

        int getBufferLength(DataLine line) {
            return Integer.MAX_VALUE - getBufferOffset(line) + 4096;
        }
    } };

    public static void main(String[] args) throws Exception {
        Mixer.Info[] infos = AudioSystem.getMixerInfo();
        log("" + infos.length + " mixers detected");
        for (int i = 0; i < infos.length; i++) {
            Mixer mixer = AudioSystem.getMixer(infos[i]);
            log("Mixer " + (i + 1) + ": " + infos[i]);
            try {
                mixer.open();
                for (Scenario scenario : scenarios) {
                    testSDL(mixer, scenario);
                    testTDL(mixer, scenario);
                }
                mixer.close();
            } catch (LineUnavailableException ex) {
                log("LineUnavailableException: " + ex);
            }
        }
        if (failed == 0) {
            log("PASSED (" + total + " tests)");
        } else {
            log("FAILED (" + failed + " of " + total + " tests)");
            throw new Exception("Test FAILED");
        }
    }

    final static int STOPPER_DELAY = 5000;

    static class AsyncLineStopper implements Runnable {

        private final DataLine line;

        private final long delayMS;

        private final Thread thread;

        private final Object readyEvent = new Object();

        private final Object startEvent = new Object();

        public AsyncLineStopper(DataLine line, long delayMS) {
            this.line = line;
            this.delayMS = delayMS;
            thread = new Thread(this);
            thread.setDaemon(true);
            synchronized (readyEvent) {
                thread.start();
                try {
                    readyEvent.wait();
                } catch (InterruptedException ex) {
                }
            }
        }

        public void schedule() {
            synchronized (startEvent) {
                startEvent.notifyAll();
            }
        }

        public void force() {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException ex) {
                log("join exception: " + ex);
            }
        }

        public void run() {
            try {
                synchronized (readyEvent) {
                    readyEvent.notifyAll();
                }
                synchronized (startEvent) {
                    startEvent.wait();
                }
                Thread.sleep(delayMS);
            } catch (InterruptedException ex) {
                log("    AsyncLineStopper has been interrupted: " + ex);
            }
            log("    stop...");
            line.stop();
            log("    close...");
            line.close();
        }
    }

    static void testSDL(Mixer mixer, Scenario scenario) {
        log("  Testing SDL (scenario: " + scenario + ")...");
        Line.Info linfo = new Line.Info(SourceDataLine.class);
        SourceDataLine line = null;
        try {
            line = (SourceDataLine) mixer.getLine(linfo);
            log("    got line: " + line);
            log("    open...");
            line.open();
        } catch (IllegalArgumentException ex) {
            log("    unsupported (IllegalArgumentException)");
            return;
        } catch (LineUnavailableException ex) {
            log("    unavailable: " + ex);
            return;
        }
        total++;
        log("    start...");
        line.start();
        AsyncLineStopper lineStopper = new AsyncLineStopper(line, STOPPER_DELAY);
        int offset = scenario.getBufferOffset(line);
        int len = scenario.getBufferLength(line);
        len -= len % line.getFormat().getFrameSize();
        log("    write...");
        lineStopper.schedule();
        try {
            line.write(buffer, offset, len);
            log("    ERROR: didn't get ArrayIndexOutOfBoundsException");
            failed++;
        } catch (ArrayIndexOutOfBoundsException ex) {
            log("    OK: got ArrayIndexOutOfBoundsException: " + ex);
        }
        lineStopper.force();
    }

    static void testTDL(Mixer mixer, Scenario scenario) {
        log("  Testing TDL (scenario: " + scenario + ")...");
        Line.Info linfo = new Line.Info(TargetDataLine.class);
        TargetDataLine line = null;
        try {
            line = (TargetDataLine) mixer.getLine(linfo);
            log("    got line: " + line);
            log("    open...");
            line.open();
        } catch (IllegalArgumentException ex) {
            log("    unsupported (IllegalArgumentException)");
            return;
        } catch (LineUnavailableException ex) {
            log("    unavailable: " + ex);
            return;
        }
        total++;
        log("    start...");
        line.start();
        AsyncLineStopper lineStopper = new AsyncLineStopper(line, STOPPER_DELAY);
        int offset = scenario.getBufferOffset(line);
        int len = scenario.getBufferLength(line);
        len -= len % line.getFormat().getFrameSize();
        log("    read...");
        try {
            line.read(buffer, offset, len);
            log("    ERROR: didn't get ArrayIndexOutOfBoundsException");
            failed++;
        } catch (ArrayIndexOutOfBoundsException ex) {
            log("    OK: got ArrayIndexOutOfBoundsException: " + ex);
        }
        lineStopper.force();
    }

    static void log(String s) {
        System.out.println(s);
        System.out.flush();
    }
}
