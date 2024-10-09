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
        long minHandles = Long.MAX_VALUE;
        long maxHandles = 0L;
        int MAX_SPAWN = 50;
        for (int i = 0; i < MAX_SPAWN; i++) {
            try {
                Process testProcess = new ProcessBuilder("cmd", "/c", "dir").start();
                Thread outputConsumer = new Thread(() -> consumeStream(testProcess.pid(), testProcess.getInputStream()));
                outputConsumer.setDaemon(true);
                outputConsumer.start();
                Thread errorConsumer = new Thread(() -> consumeStream(testProcess.pid(), testProcess.getErrorStream()));
                errorConsumer.setDaemon(true);
                errorConsumer.start();
                testProcess.waitFor();
                System.gc();
                outputConsumer.join();
                errorConsumer.join();
                long count = getProcessHandleCount();
                if (count < 0)
                    throw new AssertionError("getProcessHandleCount failed");
                minHandles = Math.min(minHandles, count);
                maxHandles = Math.max(maxHandles, count);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                throw e;
            }
        }
        final long ERROR_PERCENT = 10;
        final long ERROR_THRESHOLD = minHandles + ((minHandles + ERROR_PERCENT - 1) / ERROR_PERCENT);
        if (maxHandles >= ERROR_THRESHOLD) {
            System.out.println("Processes started: " + MAX_SPAWN);
            System.out.println("minhandles: " + minHandles);
            System.out.println("maxhandles: " + maxHandles);
            throw new AssertionError("Handle use increased by more than " + ERROR_PERCENT + " percent.");
        }
    }

    private static void consumeStream(long pid, InputStream inputStream) {
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
}
