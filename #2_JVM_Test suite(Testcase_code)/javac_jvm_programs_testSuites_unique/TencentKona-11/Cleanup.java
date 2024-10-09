



import java.io.*;

public class Cleanup extends Thread {

    static PipedWriter w;
    static PipedReader r;
    static boolean isWriterClosed = false;

    public void run() {
        try {
            System.out.println("Reader reading...");
            r.read(new char[2048], 0, 2048);

            
            System.out.println("Reader closing stream...");
            r.close();

            Thread.sleep(3000);
        } catch (Throwable e) {
            System.out.println("Reader exception:");
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws Exception {
        r = new PipedReader();
        w = new PipedWriter(r);

        Cleanup reader  = new Cleanup();
        reader.start();

        BufferedWriter bw = new BufferedWriter(w);

        boolean isWriterClosed = false;

        try {
            System.out.println("Writer started.");

            for (int i = 0; i < 3; i++) {
              bw.write(new char[1024], 0, (1024));
            }

            
            
            bw.close();

        } catch (Throwable e) {
            try {
                e.printStackTrace();

                
                
                bw.write('a');

            } catch (IOException ex) {
                ex.printStackTrace();
                isWriterClosed = true;
            }
        } finally {
            System.out.println("Writer done.");
            reader.join();
            if (!isWriterClosed) {
                throw new Exception("BufferedWriter is not closed properly");
            }
        }
    }
}
