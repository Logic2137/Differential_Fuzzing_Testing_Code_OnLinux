

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteProgressListener;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;

import static java.awt.image.BufferedImage.TYPE_BYTE_BINARY;


public final class WriteAfterAbort implements IIOWriteProgressListener {

    private volatile boolean abortFlag = true;
    private volatile boolean isAbortCalled;
    private volatile boolean isCompleteCalled;
    private volatile boolean isProgressCalled;
    private volatile boolean isStartedCalled;
    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    private static FileOutputStream fos;
    private static File file;

    private void test(final ImageWriter writer) throws IOException {
        try {
            
            final BufferedImage imageWrite =
                    new BufferedImage(WIDTH, HEIGHT, TYPE_BYTE_BINARY);
            final Graphics2D g = imageWrite.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.dispose();

            
            file = File.createTempFile("temp", ".img");
            fos = new SkipWriteOnAbortOutputStream(file);
            final ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
            writer.setOutput(ios);
            writer.addIIOWriteProgressListener(this);

            
            writer.write(imageWrite);
            if (!isStartedCalled) {
                throw new RuntimeException("Started should be called");
            }
            if (!isProgressCalled) {
                throw new RuntimeException("Progress should be called");
            }
            if (!isAbortCalled) {
                throw new RuntimeException("Abort should be called");
            }
            if (isCompleteCalled) {
                throw new RuntimeException("Complete should not be called");
            }
            
            ios.flush();

            
            abortFlag = false;
            isAbortCalled = false;
            isCompleteCalled = false;
            isProgressCalled = false;
            isStartedCalled = false;
            writer.write(imageWrite);

            if (!isStartedCalled) {
                throw new RuntimeException("Started should be called");
            }
            if (!isProgressCalled) {
                throw new RuntimeException("Progress should be called");
            }
            if (isAbortCalled) {
                throw new RuntimeException("Abort should not be called");
            }
            if (!isCompleteCalled) {
                throw new RuntimeException("Complete should be called");
            }
            ios.close();

            
            final BufferedImage imageRead = ImageIO.read(file);
            for (int x = 0; x < WIDTH; ++x) {
                for (int y = 0; y < HEIGHT; ++y) {
                    if (imageRead.getRGB(x, y) != imageWrite.getRGB(x, y)) {
                        throw new RuntimeException("Test failed.");
                    }
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

    public static void main(final String[] args) throws IOException {
        final IIORegistry registry = IIORegistry.getDefaultInstance();
        final Iterator<ImageWriterSpi> iter = registry.getServiceProviders(
                ImageWriterSpi.class, provider -> true, true);

        
        int numFailures = 0;
        while (iter.hasNext()) {
            final WriteAfterAbort writeAfterAbort = new WriteAfterAbort();
            final ImageWriter writer = iter.next().createWriterInstance();
            System.out.println("ImageWriter = " + writer);
            try {
                writeAfterAbort.test(writer);
            } catch (Exception e) {
                System.err.println("Test failed for \""
                    + writer.getOriginatingProvider().getFormatNames()[0]
                    + "\" format.");
                numFailures++;
            }
        }
        if (numFailures == 0) {
            System.out.println("Test passed.");
        } else {
            throw new RuntimeException("Test failed.");
        }
    }

    

    @Override
    public void imageComplete(ImageWriter source) {
        isCompleteCalled = true;
    }

    @Override
    public void imageProgress(ImageWriter source, float percentageDone) {
        isProgressCalled = true;
        if (percentageDone > 50 && abortFlag) {
            source.abort();
        }
    }

    @Override
    public void imageStarted(ImageWriter source, int imageIndex) {
        isStartedCalled = true;
    }

    @Override
    public void writeAborted(final ImageWriter source) {
        isAbortCalled = true;
    }

    @Override
    public void thumbnailComplete(ImageWriter source) {
    }

    @Override
    public void thumbnailProgress(ImageWriter source, float percentageDone) {
    }

    @Override
    public void thumbnailStarted(ImageWriter source, int imageIndex,
                                 int thumbnailIndex) {
    }

    
    private class SkipWriteOnAbortOutputStream extends FileOutputStream {

        SkipWriteOnAbortOutputStream(File file) throws FileNotFoundException {
            super(file);
        }

        @Override
        public void write(int b) throws IOException {
            if (!abortFlag) {
                super.write(b);
            }
        }

        @Override
        public void write(byte[] b) throws IOException {
            if (!abortFlag) {
                super.write(b);
            }
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            if (!abortFlag) {
                super.write(b, off, len);
            }
        }
    }
}

