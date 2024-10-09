

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ProcessHandle;


public class CheckHandles {

    
    private static native long getProcessHandleCount();

    public static void main(String[] args) throws Exception {
        System.loadLibrary("CheckHandles");

        System.out.println("mypid: " + ProcessHandle.current().pid());

        
        int MAX_WARMUP = 20;
        long prevCount = getProcessHandleCount();
        for (int i = 0; i < MAX_WARMUP; i++) {
            oneProcess();
            System.gc();        
            sleep(10);

            long count = getProcessHandleCount();
            if (count < 0)
                throw new AssertionError("getProcessHandleCount failed");
            System.out.println("warmup handle delta: " + (count - prevCount));
            prevCount = count;
        }
        System.out.println("Warmup done");
        System.out.println();

        prevCount = getProcessHandleCount();
        long startHandles = prevCount;
        long maxHandles = startHandles;
        int MAX_SPAWN = 50;
        for (int i = 0; i < MAX_SPAWN; i++) {
            oneProcess();
            System.gc();        
            sleep(10);

            long count = getProcessHandleCount();
            if (count < 0)
                throw new AssertionError("getProcessHandleCount failed");
            System.out.println("handle delta: " + (count - prevCount));
            prevCount = count;
            maxHandles = Math.max(maxHandles, count);
        }

        System.out.println("Processes started: " + MAX_SPAWN);
        System.out.println("startHandles: " + startHandles);
        System.out.println("maxHandles:   " + maxHandles);

        final float ERROR_PERCENT = 10.0f;   
        final long ERROR_THRESHOLD = startHandles + Math.round(startHandles * ERROR_PERCENT / 100.0f);
        if (maxHandles >= ERROR_THRESHOLD) {
            throw new AssertionError("Handle use increased by more than " + ERROR_PERCENT + " percent.");
        }
    }

    
    private static void oneProcess() {
        try {

            Process testProcess = new ProcessBuilder("cmd", "/c", "dir").start();

            Thread outputConsumer = new Thread(() -> consumeStream(testProcess.getInputStream()));
            outputConsumer.setDaemon(true);
            outputConsumer.start();
            Thread errorConsumer = new Thread(() -> consumeStream(testProcess.getErrorStream()));
            errorConsumer.setDaemon(true);
            errorConsumer.start();

            testProcess.waitFor();
            outputConsumer.join();
            errorConsumer.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Exception", e);
        }
    }

    private static void consumeStream(InputStream inputStream) {
        BufferedReader reader = null;
        try {
            int lines = 0;
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while (reader.readLine() != null) {
                lines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            
        }
    }
}
