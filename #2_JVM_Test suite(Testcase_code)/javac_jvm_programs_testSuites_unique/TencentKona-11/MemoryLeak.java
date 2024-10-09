



import java.io.*;

public class MemoryLeak {
    public static void main(String[] args) throws Throwable {
        byte[] bytes = new byte[1 << 20];
        String dir = System.getProperty("test.src", ".");
        File testFile = new File(dir, "input.txt");
        FileInputStream s = new FileInputStream(testFile);
        s.close();
        for (int i = 0; i < 10000; i++) {
            try {
                s.read(bytes);
                throw new Error("expected IOException");
            } catch (IOException expected) {
                
            } catch (OutOfMemoryError oome) {
                System.out.printf("Got OutOfMemoryError, i=%d%n", i);
                throw oome;
            }
        }
    }
}
