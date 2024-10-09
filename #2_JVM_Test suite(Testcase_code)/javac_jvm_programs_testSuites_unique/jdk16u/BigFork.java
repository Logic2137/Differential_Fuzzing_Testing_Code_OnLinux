

import java.util.*;
import java.io.*;


public class BigFork {
    static final Random rnd = new Random();
    static void touchPages(byte[] chunk) {
        final int pageSize = 4096;
        for (int i = 0; i < chunk.length; i+= pageSize) {
            chunk[i] = (byte) rnd.nextInt();
        }
    }

    static void showCommittedMemory() throws IOException {
        BufferedReader r =
            new BufferedReader(
                new InputStreamReader(
                    new FileInputStream("/proc/meminfo")));
        System.out.println("-------");
        String line;
        while ((line = r.readLine()) != null) {
            if (line.startsWith("Commit")) {
                System.out.printf("%s%n", line);
            }
        }
        System.out.println("-------");
    }

    public static void main(String[] args) throws Throwable {
        showCommittedMemory();

        final int chunkSize = 1024 * 1024 * 100;
        List<byte[]> chunks = new ArrayList<byte[]>(100);
        try {
            for (;;) {
                byte[] chunk = new byte[chunkSize];
                touchPages(chunk);
                chunks.add(chunk);
            }
        } catch (OutOfMemoryError e) {
            chunks.set(0, null);        
            System.gc();
            int size = chunks.size();
            System.out.printf("size=%.2gGB%n", (double)size/10);

            showCommittedMemory();

            
            Process p = new ProcessBuilder("/bin/true").start();
            p.waitFor();
        }
    }
}
