

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import javax.imageio.ImageIO;
import sun.awt.OSInfo;
import sun.awt.SunToolkit;
import sun.awt.image.MultiResolutionToolkitImage;


public class MultiResolutionToolkitImageTest {

    private static final int IMAGE_WIDTH = 300;
    private static final int IMAGE_HEIGHT = 200;
    private static final Color COLOR_1X = Color.GREEN;
    private static final Color COLOR_2X = Color.BLUE;
    private static final String IMAGE_NAME_1X = "image.png";
    private static final String IMAGE_NAME_2X = "image@2x.png";
    private static final int WAIT_TIME = 400;
    private static volatile boolean isImageLoaded = false;
    private static volatile boolean isRVObserverCalled = false;

    public static void main(String[] args) throws Exception {

        if (!checkOS()) {
            return;
        }
        generateImages();
        testToolkitMultiResolutionImageLoad();
    }

    static void testToolkitMultiResolutionImageLoad() throws Exception {
        File imageFile = new File(IMAGE_NAME_1X);
        String fileName = imageFile.getAbsolutePath();
        Image image = Toolkit.getDefaultToolkit().getImage(fileName);
        SunToolkit toolkit = (SunToolkit) Toolkit.getDefaultToolkit();
        toolkit.prepareImage(image, -1, -1, new LoadImageObserver());

        final long time = WAIT_TIME + System.currentTimeMillis();
        while ((!isImageLoaded || !isRVObserverCalled)
                && System.currentTimeMillis() < time) {
            Thread.sleep(50);
        }

        if(!isImageLoaded){
            throw new RuntimeException("Image is not loaded!");
        }

        if(!isRVObserverCalled){
            throw new RuntimeException("Resolution Variant observer is not called!");
        }
    }

    static void generateImages() throws Exception {
        if (!new File(IMAGE_NAME_1X).exists()) {
            generateImage(1);
        }

        if (!new File(IMAGE_NAME_2X).exists()) {
            generateImage(2);
        }
    }

    static void generateImage(int scale) throws Exception {
        BufferedImage image = new BufferedImage(scale * IMAGE_WIDTH, scale * IMAGE_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(scale == 1 ? COLOR_1X : COLOR_2X);
        g.fillRect(0, 0, scale * IMAGE_WIDTH, scale * IMAGE_HEIGHT);
        File file = new File(scale == 1 ? IMAGE_NAME_1X : IMAGE_NAME_2X);
        ImageIO.write(image, "png", file);
    }

    static boolean checkOS() {
        return OSInfo.getOSType() == OSInfo.OSType.MACOSX;
    }

    static class LoadImageObserver implements ImageObserver {

        @Override
        public boolean imageUpdate(Image img, int infoflags, int x, int y,
                int width, int height) {

            if (isRVObserver()) {
                isRVObserverCalled = true;
                SunToolkit toolkit = (SunToolkit) Toolkit.getDefaultToolkit();
                Image resolutionVariant = getResolutionVariant(img);
                int rvFlags = toolkit.checkImage(resolutionVariant, width, height,
                        new IdleImageObserver());
                if (rvFlags < infoflags) {
                    throw new RuntimeException("Info flags are greater than"
                            + " resolution varint info flags");
                }
            } else if ((infoflags & ALLBITS) != 0) {
                isImageLoaded = true;
            }

            return (infoflags & ALLBITS) == 0;
        }
    }

    static boolean isRVObserver() {
        Exception e = new Exception();

        for (StackTraceElement elem : e.getStackTrace()) {
            if (elem.getClassName().endsWith("ObserverCache")) {
                return true;
            }
        }
        return false;
    }

    static class IdleImageObserver implements ImageObserver {

        @Override
        public boolean imageUpdate(Image img, int infoflags, int x, int y,
                int width, int height) {
            return false;
        }
    }

    static Image getResolutionVariant(Image image) {
        return ((MultiResolutionToolkitImage) image).getResolutionVariant();
    }
}
