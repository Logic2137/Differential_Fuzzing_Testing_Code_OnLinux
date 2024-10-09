import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.ImageConsumer;
import java.awt.Image;
import java.awt.Container;

public class AddNoLeak {

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");
        Container cont = new Container();
        Image img = cont.createImage(new DummyImageSource());
        for (int i = 0; i < 15000; i++) {
            img.getWidth(new ImageObserver() {

                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    return false;
                }
            });
            if (i % 100 == 0) {
                System.gc();
            }
        }
    }

    private static class DummyImageSource implements ImageProducer {

        public void addConsumer(ImageConsumer ic) {
        }

        public boolean isConsumer(ImageConsumer ic) {
            return false;
        }

        public void removeConsumer(ImageConsumer ic) {
        }

        public void startProduction(ImageConsumer ic) {
        }

        public void requestTopDownLeftRightResend(ImageConsumer ic) {
        }
    }
}
