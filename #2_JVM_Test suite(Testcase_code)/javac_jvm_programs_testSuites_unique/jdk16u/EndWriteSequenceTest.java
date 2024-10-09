



import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class EndWriteSequenceTest {
    public static void main(String[] args) throws IOException {
        ImageWriter w =
            ImageIO.getImageWritersByFormatName("GIF").next();

        boolean gotCorrectException = false;

        
        try {
            w.reset(); 
            w.endWriteSequence();
        } catch (IllegalStateException e) {
            gotCorrectException = true;
        } catch (Throwable e) {
            throw new RuntimeException("Test failed.", e);
        }
        if (!gotCorrectException) {
            throw new RuntimeException("Test failed.");
        }

        
        ByteArrayOutputStream baos =
            new ByteArrayOutputStream();

        ImageOutputStream ios =
            ImageIO.createImageOutputStream(baos);

        w.setOutput(ios);

        
        gotCorrectException = false;
        try {
            w.endWriteSequence();
        } catch  (IllegalStateException e) {
            gotCorrectException = true;
        } catch (Throwable e) {
            throw new RuntimeException("Test failed.", e);
        }
        if (!gotCorrectException) {
            throw new RuntimeException("Test failed.");
        }

        System.out.println("Test passed.");
    }
}
