

import java.io.File;
import java.util.Random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;


public class StopStart implements Runnable {

    static int sampleRate = 8000;
    static double frequency = 2000.0;
    static double RAD = 2.0 * Math.PI;
    static Random random = new Random();

    static byte[] audioData = new byte[sampleRate/2];
    static SourceDataLine source;

    static boolean terminated = false;

    static int buffersWritten = 0;
    static long bytesWritten = 0;
    static int buffersWrittenAfter5Seconds;

    static AudioInputStream ais = null;
    static AudioFormat audioFormat;
    static String filename;

    static int executedTests=0;
    static int successfulTests = 0;

    public static void constructAIS() throws Exception {
        ais = AudioSystem.getAudioInputStream(new File(filename));
    }

    public static void doStartStopTest1() throws Exception {
        System.out.println("TEST 1: play for 3 seconds, stop/start/stop/start/play for 3 seconds...");
        source.start();
        Thread.sleep(100);
        bytesWritten = 0;
        System.out.println("Waiting for 3 seconds...");
        Thread.sleep(3000);
        buffersWrittenAfter5Seconds = buffersWritten;
        System.out.println("Buffers Written: "+buffersWritten);
        System.out.println("stop()->start()->stop()->start()");
        source.stop();
        
        source.start();
        
        source.stop();
        
        source.start();
        System.out.println("Buffers Written: "+buffersWritten);
        System.out.println("Waiting for 3 seconds...");
        Thread.sleep(3000);
        System.out.println("Buffers Written: "+buffersWritten);
        if (buffersWritten >= ((buffersWrittenAfter5Seconds * 2) - ((buffersWrittenAfter5Seconds / 4)))) {
            successfulTests++;
        }
    }

    private static int nextWaitTime() {
        int waitTime = random.nextInt(25);
        waitTime*=waitTime;
        if (waitTime<20) waitTime = 0;
        return waitTime;
    }


    public static void doStartStopTest2() throws Exception {
        System.out.println("TEST 2: start and stop 100 times with random wait in between");
        int max=100;
        for (int i=0; i<max; i++) {
            System.out.println("Round "+i);
            System.out.println("Start....");
            source.start();
            int waitTime = nextWaitTime();
            System.out.println("Waiting for "+waitTime+"ms...");
            if (waitTime>0) {
                Thread.sleep(waitTime);
            }
            System.out.println("stop()");
            source.stop();
            waitTime = nextWaitTime();
            System.out.println("Waiting for "+waitTime+"ms...");
            if (waitTime>0) {
                Thread.sleep(waitTime);
            }
        }
    }

    public static void doStartStopTest3() throws Exception {
        System.out.println("TEST 3: start and stop 100 times with random wait only every 10 rounds ");
        int max=100;
        for (int i=0; i<max; i++) {
            System.out.println("Round "+i);
            System.out.println("Start....");
            source.start();
            if (i % 10 == 9) {
                int waitTime = nextWaitTime();
                System.out.println("Waiting for "+waitTime+"ms...");
                if (waitTime>0) {
                    Thread.sleep(waitTime);
                }
            }
            System.out.println("stop()");
            source.stop();
            if (i % 13 == 12) {
                int waitTime = nextWaitTime();
                System.out.println("Waiting for "+waitTime+"ms...");
                if (waitTime>0) {
                    Thread.sleep(waitTime);
                }
            }
        }
    }

    public static void runTest(int testNum) {
        terminated = false;
        Thread thread = null;
        buffersWrittenAfter5Seconds = 0;
        
        random.setSeed(1);
        try {
            executedTests++;
            thread = new Thread(new StopStart());
            thread.start();
            switch (testNum) {
            case 1: doStartStopTest1(); break;
            case 2: doStartStopTest2(); break;
            case 3: doStartStopTest3(); break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        source.stop();
        source.close();
        if (thread!=null) {
            terminated = true;
            System.out.println("Waiting for thread to die...");
            try {
                thread.join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        filename = null;
        if (args.length>0) {
            File f = new File(args[0]);
            if (f.exists()) {
                filename = args[0];
                System.out.println("Opening "+filename);
                constructAIS();
                audioFormat = ais.getFormat();
            }
        }
        if (filename == null) {
            audioFormat = new AudioFormat((float)sampleRate, 8, 1, true, true);
            for (int i=0; i<audioData.length; i++) {
                audioData[i] = (byte)(Math.sin(RAD*frequency/sampleRate*i)*127.0);
            }
        }
        long startTime = System.currentTimeMillis();
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (int i=0; i<mixers.length; i++) {
            try {
                Mixer mixer = AudioSystem.getMixer(mixers[i]);
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                String mixerName = mixer.getMixerInfo().getName();
                try {
                    source = (SourceDataLine) mixer.getLine(info);
                    source.open(audioFormat);
                } catch (IllegalArgumentException iae) {
                    System.out.println("Mixer "+mixerName+" does not provide a SourceDataLine.");
                    continue;
                } catch (LineUnavailableException lue) {
                    System.out.println("Mixer "+mixerName+": no lines available.");
                    continue;
                }
                System.out.println("***** Testing on Mixer "+mixerName+":");
                
                
                runTest(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (mixers.length==0) {
            System.out.println("No mixers available!");
        } else {
            long duration = System.currentTimeMillis() - startTime;

            System.out.println("Test took "+(duration/1000)+"s and "+(duration % 1000)+"ms.");
        }

        System.out.println("Exiting main()");
        if (executedTests>0) {
            if (successfulTests == 0) {
                if (args.length == 0) {
                    throw new Exception("Test FAILED");
                }
                System.out.println("test FAILED.");
            } else {
                System.out.println("test successful.");
            }
        } else {
            System.out.println("Could not execute any tests - are soundcards correctly installed?");
            System.out.println("Test NOT FAILED.");
        }
    }

    public void run() {
        int len = audioData.length;
        int offset = len;
        System.out.println("Thread: started. Beginning audio i/o");
        while (!terminated) {
            try {
                
                
                
                if (offset >= len) {
                    offset = 0;
                    if (ais!=null) {
                        do {
                            len = ais.read(audioData, 0, audioData.length);
                            if (len < 0) {
                                constructAIS();
                            }
                        } while (len < 0);
                    }
                }
                int toWrite = len - offset;
                int written = source.write(audioData, offset, toWrite);
                offset+=written;
                bytesWritten += written;
                buffersWritten = (int) (bytesWritten / audioData.length);
            } catch (Exception e) {
                e.printStackTrace();
                terminated = true;
            }
        }
        System.out.println("Thread: closing line");
        source.stop();
        source.close();
        System.out.println("Thread finished");
    }
}
