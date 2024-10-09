

import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.IOException;
import java.io.OutputStream;


public class StreamPipe extends Thread {

    private InputStream in;
    private OutputStream out;

    private static Object countLock = new Object();
    private static int count = 0;

    
    private StreamPipe(InputStream in, OutputStream out, String name) {
        super(name);
        this.in  = in;
        this.out = out;
    }

    
    public static StreamPipe plugTogether(InputStream in, OutputStream out) {
        String name;

        synchronized (countLock) {
            name = "java.rmi.testlibrary.StreamPipe-" + (count++);
        }

        StreamPipe pipe = new StreamPipe(in, out, name);
        pipe.setDaemon(true);
        pipe.start();
        return pipe;
    }

    
    public void run() {
        try {
            byte[] buf = new byte[1024];

            while (true) {
                int nr = in.read(buf);
                if (nr == -1)
                    break;
                out.write(buf, 0, nr);
            }
        } catch (InterruptedIOException iioe) {
            
            return;
        } catch (IOException e) {
            System.err.println("*** IOException in StreamPipe.run:");
            e.printStackTrace();
        }
    }
}
