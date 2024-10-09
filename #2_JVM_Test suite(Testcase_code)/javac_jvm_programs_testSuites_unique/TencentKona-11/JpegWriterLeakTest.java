



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class JpegWriterLeakTest {

    public static void main(String[] args) {
        final ReferenceQueue<ImageWriter> queue = new ReferenceQueue<>();
        final ArrayList<Reference<? extends ImageWriter>> refs = new ArrayList<>();

        int count = 2;

        do {
            ImageWriter writer =
                    ImageIO.getImageWritersByFormatName("jpeg").next();

            final WeakReference<? extends ImageWriter> ref =
                    new WeakReference<>(writer, queue);

            refs.add(ref);


            try {
                final ImageOutputStream os =
                        ImageIO.createImageOutputStream(new ByteArrayOutputStream());
                writer.setOutput(os);

                writer.write(getImage());


                
            } catch (IOException e) {
            } finally {
                writer = null;
            }
            count--;
        } while (count > 0);


        System.out.println("Wait for GC...");

        final long testTimeOut = 60000L;

        final long startTime = System.currentTimeMillis();

        while (!refs.isEmpty()) {
            
            final long now = System.currentTimeMillis();

            if (now - startTime > testTimeOut) {
                System.out.println();
                throw new RuntimeException("Test FAILED.");
            }

            System.gc();

            try {
                System.out.print(".");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            };

            Reference<? extends ImageWriter> r = queue.poll();
            if (r != null) {
                System.out.println("Got reference: " + r);
                refs.remove(r);
            }
        }
        System.out.println("Test PASSED.");
    }

    private static BufferedImage getImage() {
        int width = 2500;
        int height = new Random().nextInt(2500) + 1;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();
        g.setColor(Color.blue);
        g.fillRect(0, 0, width, height);

        return image;
    }
}
