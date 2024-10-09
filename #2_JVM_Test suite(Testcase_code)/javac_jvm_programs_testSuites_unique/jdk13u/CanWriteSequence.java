import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;

public final class CanWriteSequence {

    private static File file;

    private static FileOutputStream fos;

    public static void main(final String[] args) throws Exception {
        final IIORegistry registry = IIORegistry.getDefaultInstance();
        final Iterator<ImageWriterSpi> iter = registry.getServiceProviders(ImageWriterSpi.class, provider -> true, true);
        while (iter.hasNext()) {
            final ImageWriter writer = iter.next().createWriterInstance();
            System.out.println("ImageWriter = " + writer);
            test(writer);
        }
        System.out.println("Test passed");
    }

    private static void test(final ImageWriter writer) throws Exception {
        try {
            file = File.createTempFile("temp", ".img");
            fos = new FileOutputStream(file);
            final ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
            writer.setOutput(ios);
            final IIOMetadata data = writer.getDefaultStreamMetadata(null);
            if (writer.canWriteSequence()) {
                writer.prepareWriteSequence(data);
            } else {
                try {
                    writer.prepareWriteSequence(data);
                    throw new RuntimeException("UnsupportedOperationException was not thrown");
                } catch (final UnsupportedOperationException ignored) {
                }
            }
        } finally {
            writer.dispose();
            if (file != null) {
                if (fos != null) {
                    fos.close();
                }
                Files.delete(file.toPath());
            }
        }
    }
}
