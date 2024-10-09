



import java.util.concurrent.TimeUnit;

import javax.sound.sampled.*;

public class bug6400879 extends Thread {

    public static void main(String args[]) throws Exception {
        bug6400879 pThis = new bug6400879();
        
        pThis.setDaemon(true);
        pThis.start();
        monitor(pThis);
    }

    static final long BLOCK_TIMEOUT = 5000;    

    
    public static void monitor(bug6400879 pThis) throws Exception {
        long prevLoop = -1;
        long prevTime = currentTimeMillis();
        while (pThis.isAlive()) {
            if (pThis.loopCounter == prevLoop) {
                long delay = currentTimeMillis() - prevTime;
                if (delay > BLOCK_TIMEOUT) {
                    
                    log("The test is slow, delay = " + delay);
                }
            } else {
                prevLoop = pThis.loopCounter;
                prevTime = currentTimeMillis();
            }
            delay(1000);    
        }
        log("Test sucessfully passed.");
    }

    volatile long loopCounter = 0;
    final long LOOPS_PER_LINE = 100;

    public void run() {
        SourceDataLine line = null;

        DataLine.Info line_info = new DataLine.Info(SourceDataLine.class, null);
        Line.Info infos[] = AudioSystem.getSourceLineInfo(line_info);

        log("total " + infos.length + " lines");

        for (int lineNum = 0; lineNum < infos.length; lineNum++) {
            try {
                line = (SourceDataLine)AudioSystem.getLine(infos[lineNum]);
                log("testing line: " + line);
                line.open(line.getFormat());
                for (int i=0; i<LOOPS_PER_LINE; i++) {
                    log("start->stop (" + i + ")");
                    line.start();
                    line.stop();
                    log(" - OK");
                    loopCounter++;
                }
                line.close();
                line = null;
            } catch (LineUnavailableException e1) {
                log("LineUnavailableException caught, test okay.");
                log(e1.getMessage());
            } catch (SecurityException e2) {
                log("SecurityException caught, test okay.");
                log(e2.getMessage());
            } catch (IllegalArgumentException e3) {
                log("IllegalArgumentException caught, test okay.");
                log(e3.getMessage());
            }
            if (line != null) {
                line.close();
                line = null;
            }
        }

    }


    
    static long startTime = currentTimeMillis();
    static long currentTimeMillis() {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    }
    static void log(String s) {
        long time = currentTimeMillis() - startTime;
        long ms = time % 1000;
        time /= 1000;
        long sec = time % 60;
        time /= 60;
        long min = time % 60;
        time /= 60;
        System.out.println(""
                + (time < 10 ? "0" : "") + time
                + ":" + (min < 10 ? "0" : "") + min
                + ":" + (sec < 10 ? "0" : "") + sec
                + "." + (ms < 10 ? "00" : (ms < 100 ? "0" : "")) + ms
                + " (" + Thread.currentThread().getName() + ") " + s);
    }
    static void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {}
    }
}
