



import javax.imageio.ImageWriter;

import com.sun.imageio.plugins.jpeg.JPEGImageWriter;

public class ResetOutOfMemory {

    public static void main(String args[]) {
        ImageWriter writer = new JPEGImageWriter(null);
        try {
            writer.reset();
        } catch (OutOfMemoryError e) {
            throw new RuntimeException("Got OutOfMemoryError!");
        }
    }
}
