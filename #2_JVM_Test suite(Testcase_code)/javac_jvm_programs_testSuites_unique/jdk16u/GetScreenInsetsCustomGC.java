

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.PrintJob;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.im.InputMethodHighlight;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.net.URL;
import java.util.Map;
import java.util.Properties;


public final class GetScreenInsetsCustomGC {

    public static void main(final String[] args) {
        
        GraphicsConfiguration dc =
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice()
                        .getDefaultConfiguration();
        
        GraphicsConfiguration cd = new _GraphicsConfiguration();
        
        BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        GraphicsConfiguration bic = g.getDeviceConfiguration();
        g.dispose();

        

        
        if (Toolkit.getDefaultToolkit().getScreenInsets(dc) == null) {
            throw new NullPointerException();
        }
        
        testEmpty(Toolkit.getDefaultToolkit().getScreenInsets(cd));
        testEmpty(Toolkit.getDefaultToolkit().getScreenInsets(bic));

        try {
            Toolkit.getDefaultToolkit().getScreenInsets(null);
            throw new RuntimeException("NullPointerException is not thrown");
        } catch (NullPointerException ignored) {
            
        }

        

        Toolkit tk = new _Toolkit();
        
        if (tk.getScreenInsets(dc) == null) {
            throw new NullPointerException();
        }
        
        testEmpty(tk.getScreenInsets(cd));
        testEmpty(tk.getScreenInsets(bic));

        try {
            tk.getScreenInsets(null);
            throw new RuntimeException("NullPointerException is not thrown");
        } catch (NullPointerException ignored) {
            
        }
    }

    private static void testEmpty(final Insets insets) {
        if ((insets.left | insets.top | insets.bottom | insets.right) != 0) {
            System.err.println("Expected empty insets");
            System.err.println("Actual: " + insets);
            throw new RuntimeException();
        }
    }

    private static class _GraphicsConfiguration extends GraphicsConfiguration {

        @Override
        public GraphicsDevice getDevice() {
            return null;
        }

        @Override
        public ColorModel getColorModel() {
            return null;
        }

        @Override
        public ColorModel getColorModel(int transparency) {
            return null;
        }

        @Override
        public AffineTransform getDefaultTransform() {
            return null;
        }

        @Override
        public AffineTransform getNormalizingTransform() {
            return null;
        }

        @Override
        public Rectangle getBounds() {
            return null;
        }
    }

    private static class _Toolkit extends Toolkit {

        @Override
        public Dimension getScreenSize() throws HeadlessException {
            return null;
        }

        @Override
        public int getScreenResolution() throws HeadlessException {
            return 0;
        }

        @Override
        public ColorModel getColorModel() throws HeadlessException {
            return null;
        }

        @Override
        public String[] getFontList() {
            return new String[0];
        }

        @Override
        public FontMetrics getFontMetrics(Font font) {
            return null;
        }

        @Override
        public void sync() {

        }

        @Override
        public Image getImage(String filename) {
            return null;
        }

        @Override
        public Image getImage(URL url) {
            return null;
        }

        @Override
        public Image createImage(String filename) {
            return null;
        }

        @Override
        public Image createImage(URL url) {
            return null;
        }

        @Override
        public boolean prepareImage(Image image, int width, int height, ImageObserver observer) {
            return false;
        }

        @Override
        public int checkImage(Image image, int width, int height, ImageObserver observer) {
            return 0;
        }

        @Override
        public Image createImage(ImageProducer producer) {
            return null;
        }

        @Override
        public Image createImage(byte[] imagedata, int imageoffset, int imagelength) {
            return null;
        }

        @Override
        public PrintJob getPrintJob(Frame frame, String jobtitle, Properties props) {
            return null;
        }

        @Override
        public void beep() {

        }

        @Override
        public Clipboard getSystemClipboard() throws HeadlessException {
            return null;
        }

        @Override
        protected EventQueue getSystemEventQueueImpl() {
            return null;
        }

        @Override
        public boolean isModalityTypeSupported(Dialog.ModalityType modalityType) {
            return false;
        }

        @Override
        public boolean isModalExclusionTypeSupported(Dialog.ModalExclusionType modalExclusionType) {
            return false;
        }

        @Override
        public Map<TextAttribute, ?> mapInputMethodHighlight(InputMethodHighlight highlight) throws HeadlessException {
            return null;
        }
    }
}
