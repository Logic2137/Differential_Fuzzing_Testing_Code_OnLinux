import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.SliderUI;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static javax.swing.UIManager.getInstalledLookAndFeels;

public final class PaintThumbSize {

    private static final int SCALE = 2;

    private static final int SHIFT = 100;

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo laf : getInstalledLookAndFeels()) {
            EventQueue.invokeAndWait(() -> setLookAndFeel(laf));
            EventQueue.invokeAndWait(PaintThumbSize::test);
            if (laf.getClassName().contains("Metal")) {
                EventQueue.invokeAndWait(() -> {
                    System.err.println("\tAdditional theme: DefaultMetalTheme");
                    MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
                    test();
                });
            }
        }
    }

    private static void test() {
        BufferedImage bi = new BufferedImage(500, 500, TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        g.setColor(Color.BLACK);
        g.scale(SCALE, SCALE);
        g.translate(SHIFT, SHIFT);
        JSlider slider = new JSlider();
        SliderUI ui = slider.getUI();
        if (ui instanceof BasicSliderUI) {
            BasicSliderUI bui = (BasicSliderUI) ui;
            bui.setThumbLocation(0, 0);
            bui.paintThumb(g);
            for (int y = 0; y < bi.getHeight(); ++y) {
                for (int x = 0; x < bi.getWidth(); ++x) {
                    if (x >= SHIFT * SCALE && y >= SHIFT * SCALE) {
                        continue;
                    }
                    if (bi.getRGB(x, y) != Color.CYAN.getRGB()) {
                        System.err.println("x = " + x);
                        System.err.println("y = " + y);
                        try {
                            ImageIO.write(bi, "png", new File("image.png"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        throw new RuntimeException("Wrong color");
                    }
                }
            }
        }
        g.dispose();
    }

    private static void setLookAndFeel(UIManager.LookAndFeelInfo laf) {
        try {
            System.err.println("LookAndFeel: " + laf.getClassName());
            UIManager.setLookAndFeel(laf.getClassName());
        } catch (UnsupportedLookAndFeelException ignored) {
            System.err.println("Unsupported LookAndFeel: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
