

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;


public class bug6415669 {

    public static void main(String args[]) throws Exception {
        String osStr = System.getProperty("os.name");
        boolean isWin = osStr.toLowerCase().startsWith("windows");
        log("OS: " + osStr);
        log("Arch: " + System.getProperty("os.arch"));
        if (!isWin) {
            log("The test is for Windows only");
            return;
        }

        bug6415669 This = new bug6415669();
        if (This.test()) {
            log("Test sucessfully passed.");
        } else {
            log("Test FAILED!");
            throw new RuntimeException("Test FAILED!");
        }
    }

    volatile Transmitter transmitter = null;
    Thread openThread = null;
    boolean test() {
        openThread = new Thread(new Runnable() {
            public void run() {
                try {
                    log("openThread: getting transmitter...");
                    transmitter = MidiSystem.getTransmitter();
                    log("openThread:   - OK: " + transmitter);
                } catch (MidiUnavailableException ex) {
                    log("openThread:   - Exception: ");
                    ex.printStackTrace(System.out);
                    log("openThread: skipping...");
                }
                log("openThread: exiting...");
            }
        });
        log("starting openThread...");
        openThread.start();

        while (openThread.isAlive())
            delay(500);
        
        delay(500);

        if (transmitter == null) {
            return true;   
        }

        log("closing transmitter");
        transmitter.close();
        log("  - OK");

        return true;
    }

    
    static long startTime = currentTimeMillis();
    static long currentTimeMillis() {
        
        return System.currentTimeMillis();
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
