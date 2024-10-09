

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;


public class bug6372428 {
    public bug6372428() {
    }

    public static void main(final String[] args) {
        bug6372428 pThis = new bug6372428();
        boolean failed1 = false;
        boolean failed2 = false;
        log("");
        log("****************************************************************");
        log("*** Playback Test");
        log("****************************************************************");
        log("");
        try {
            pThis.testPlayback();
        } catch (IllegalArgumentException | LineUnavailableException e) {
            System.out.println("Playback test is not applicable. Skipped");
        } catch (Exception ex) {
            ex.printStackTrace();
            failed1 = true;
        }
        log("");
        log("");
        log("****************************************************************");
        log("*** Capture Test");
        log("****************************************************************");
        log("");
        try {
            pThis.testRecord();
        } catch (IllegalArgumentException | LineUnavailableException e) {
            System.out.println("Record test is not applicable. Skipped");
        } catch (Exception ex) {
            ex.printStackTrace();
            failed2 = true;
        }
        log("");
        log("");
        log("****************************************************************");
        if (failed1 || failed2) {
            String s = "";
            if (failed1 && failed2)
                s = "playback and capture";
            else if (failed1)
                s = "playback only";
            else
                s = "capture only";
            throw new RuntimeException("Test FAILED (" + s + ")");
        }
        log("*** All tests passed successfully.");
    }

    final static int DATA_LENGTH        = 15;   
    final static int PLAYTHREAD_DELAY   = 5;   

    

    class PlayThread extends Thread {
        SourceDataLine line;
        public PlayThread(SourceDataLine line) {
            this.line = line;
            this.setDaemon(true);
        }

        public void run() {
            log("PlayThread: starting...");
            line.start();
            log("PlayThread: delaying " + (PLAYTHREAD_DELAY * 1000) + "ms...");
            delay(PLAYTHREAD_DELAY * 1000);
            log("PlayThread: exiting...");
        }
    }

    class WriteThread extends Thread {
        SourceDataLine line;
        byte[] data;
        volatile int remaining;
        volatile boolean stopRequested = false;
        public WriteThread(SourceDataLine line, byte[] data) {
            this.line = line;
            this.data = data;
            remaining = data.length;
            this.setDaemon(true);
        }

        public void run() {
            while (remaining > 0 && !stopRequested) {
                int avail = line.available();
                if (avail > 0) {
                    if (avail > remaining)
                        avail = remaining;
                    int written = line.write(data, data.length - remaining, avail);
                    remaining -= written;
                    log("WriteThread: " + written + " bytes written");
                } else {
                    delay(100);
                }
            }
            if (remaining == 0) {
                log("WriteThread: all data has been written, draining");
                line.drain();
            } else {
                log("WriteThread: stop requested");
            }
            log("WriteThread: stopping");
            line.stop();
            log("WriteThread: exiting");
        }

        public boolean isCompleted() {
            return (remaining <= 0);
        }

        public void requestStop() {
            stopRequested = true;
        }
    }

    void testPlayback() throws LineUnavailableException {
        
        AudioFormat format = new AudioFormat(22050, 8, 1, false, false);
        byte[] soundData = new byte[(int) (format.getFrameRate() * format.getFrameSize() * DATA_LENGTH)];

        
        
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);

        line.open(format);

        
        WriteThread p1 = new WriteThread(line, soundData);
        p1.start();

        
        PlayThread p2 = new PlayThread(line);
        p2.start();

        
        long lineTime1 = line.getMicrosecondPosition() / 1000;
        long realTime1 = currentTimeMillis();
        while (true) {
            delay(500);
            if (!line.isActive()) {
                log("audio data played completely");
                break;
            }
            long lineTime2 = line.getMicrosecondPosition() / 1000;
            long realTime2 = currentTimeMillis();
            long dLineTime = lineTime2 - lineTime1;
            long dRealTime = realTime2 - realTime1;
            log("line pos: " + lineTime2 + "ms" + ", thread is " + (p2.isAlive() ? "alive" : "DIED"));
            if (dLineTime < 0) {
                throw new RuntimeException("ERROR: line position have decreased from " + lineTime1 + " to " + lineTime2);
            }
            if (dRealTime < 450) {
                
                continue;
            }
            lineTime1 = lineTime2;
            realTime1 = realTime2;
        }
    }


    

    class RecordThread extends Thread {
        TargetDataLine line;
        public RecordThread(TargetDataLine line) {
            this.line = line;
            this.setDaemon(true);
        }

        public void run() {
            log("RecordThread: starting...");
            line.start();
            log("RecordThread: delaying " + (PLAYTHREAD_DELAY * 1000) + "ms...");
            delay(PLAYTHREAD_DELAY * 1000);
            log("RecordThread: exiting...");
        }
    }

    class ReadThread extends Thread {
        TargetDataLine line;
        byte[] data;
        volatile int remaining;
        public ReadThread(TargetDataLine line, byte[] data) {
            this.line = line;
            this.data = data;
            remaining = data.length;
            this.setDaemon(true);
        }

        public void run() {
            log("ReadThread: buffer size is " + data.length + " bytes");
            delay(200);
            while ((remaining > 0) && line.isOpen()) {
                int avail = line.available();
                if (avail > 0) {
                    if (avail > remaining)
                        avail = remaining;
                    int read = line.read(data, data.length - remaining, avail);
                    remaining -= read;
                    log("ReadThread: " + read + " bytes read");
                } else {
                    delay(100);
                }
                if (remaining <= 0) {
                    log("ReadThread: record buffer is full, exiting");
                    break;
                }
            }
            if (remaining > 0) {
                log("ReadThread: line has been stopped, exiting");
            }
        }

        public int getCount() {
            return data.length - remaining;
        }
        public boolean isCompleted() {
            return (remaining <= 0);
        }
    }

    void testRecord() throws LineUnavailableException {
        
        AudioFormat format = new AudioFormat(22050, 8, 1, false, false);

        
        
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine line = (TargetDataLine)AudioSystem.getLine(info);

        line.open(format);

        
        byte[] data = new byte[(int) (format.getFrameRate() * format.getFrameSize() * DATA_LENGTH)];
        ReadThread p1 = new ReadThread(line, data);
        p1.start();

        
        
        RecordThread p2 = new RecordThread(line);
        p2.start();

        
        long endTime = currentTimeMillis() + DATA_LENGTH * 1000;

        long realTime1 = currentTimeMillis();
        long lineTime1 = line.getMicrosecondPosition() / 1000;

        while (realTime1 < endTime && !p1.isCompleted()) {
            delay(100);
            long lineTime2 = line.getMicrosecondPosition() / 1000;
            long realTime2 = currentTimeMillis();
            long dLineTime = lineTime2 - lineTime1;
            long dRealTime = realTime2 - realTime1;
            log("line pos: " + lineTime2 + "ms" + ", thread is " + (p2.isAlive() ? "alive" : "DIED"));
            if (dLineTime < 0) {
                line.stop();
                line.close();
                throw new RuntimeException("ERROR: line position have decreased from " + lineTime1 + " to " + lineTime2);
            }
            if (dRealTime < 450) {
                
                continue;
            }
            lineTime1 = lineTime2;
            realTime1 = realTime2;
        }
        log("stopping line...");
        line.stop();
        line.close();

        
    }

    void playRecorded(AudioFormat format, byte[] data) throws Exception {
        
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);

        line.open();
        line.start();

        int remaining = data.length;
        while (remaining > 0) {
            int avail = line.available();
            if (avail > 0) {
                if (avail > remaining)
                    avail = remaining;
                int written = line.write(data, data.length - remaining, avail);
                remaining -= written;
                log("Playing: " + written + " bytes written");
            } else {
                delay(100);
            }
        }

        line.drain();
        line.stop();
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
