


import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageFilter;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.Hashtable;


class EmptyImageConsumer implements ImageConsumer {
    @Override
    public void setDimensions(int width, int height) {
    }

    @Override
    public void setProperties(Hashtable<?, ?> props) {
    }

    @Override
    public void setColorModel(ColorModel colorModel) {
    }

    @Override
    public void setHints(int hintFlags) {
    }

    @Override
    public void setPixels(int x, int y, int width, int height,
                          ColorModel colorModel, byte[] pixels,
                          int offset, int scanSize) {
    }

    @Override
    public void setPixels(int x, int y, int width, int height,
                          ColorModel colorModel, int[] pixels,
                          int offset, int scanSize) {
    }

    @Override
    public void imageComplete(int i) {
    }
}


class EmptyImageProducer implements ImageProducer {
    @Override
    public void addConsumer(ImageConsumer imageConsumer) {
    }

    @Override
    public boolean isConsumer(ImageConsumer imageConsumer) {
        return false;
    }

    @Override
    public void removeConsumer(ImageConsumer imageConsumer) {
    }

    @Override
    public void startProduction(ImageConsumer imageConsumer) {
    }

    @Override
    public void requestTopDownLeftRightResend(ImageConsumer imageConsumer) {
    }
}


class EmptyFilteredImage extends Image {
    ImageFilter filter = null;
    ImageProducer producer = null;

    public EmptyFilteredImage(ImageProducer imgSource) {
        filter = new ImageFilter();
        producer = new FilteredImageSource(imgSource, filter);
    }

    @Override
    public int getWidth(ImageObserver observer) {
        return 100;
    }

    @Override
    public int getHeight(ImageObserver observer) {
        return 100;
    }

    @Override
    public ImageProducer getSource() {
        return producer;
    }

    @Override
    public Graphics getGraphics() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getProperty(String name, ImageObserver observer) {
        return null;
    }
}

public final class FilteredImageSourceTest {
    
    private static final int TEST_MIN_DURATION = 5000;

    
    private static volatile Throwable fail = null;

    public static void main(final String[] args)
            throws InterruptedException {
        final ImageConsumer ic = new EmptyImageConsumer();
        final ImageProducer ip = new EmptyImageProducer();
        final Image image = new EmptyFilteredImage(ip);

        
        Thread t1 = new Thread(() -> {
            try {
                while (true) {
                    image.getSource().addConsumer(ic);
                }
            } catch (Throwable t) {
                fail = t;
            }
        });
        t1.setDaemon(true);

        Thread t2 = new Thread(() -> {
            try {
                while (true) {
                    image.getSource().removeConsumer(ic);
                }
            } catch (Throwable t) {
                fail = t;
            }
        });
        t2.setDaemon(true);

        Thread t3 = new Thread(() -> {
            try {
                while (true) {
                    image.getSource().startProduction(ic);
                }
            } catch (Throwable t) {
                fail = t;
            }
        });
        t3.setDaemon(true);

        
        t1.start();
        t2.start();
        t3.start();

        
        t1.join(TEST_MIN_DURATION);
        if (fail != null) {
            throw new RuntimeException("Test failed with exception: ", fail);
        }
    }
}
