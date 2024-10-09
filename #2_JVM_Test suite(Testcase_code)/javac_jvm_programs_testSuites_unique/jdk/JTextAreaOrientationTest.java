


import java.awt.ComponentOrientation;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class JTextAreaOrientationTest  {

    static JFrame frame;
    static Rectangle bounds;

    public static boolean compareBufferedImages(BufferedImage bufferedImage0, BufferedImage bufferedImage1) {
        int width = bufferedImage0.getWidth();
        int height = bufferedImage0.getHeight();

        if (width != bufferedImage1.getWidth() || height != bufferedImage1.getHeight()) {
            return false;
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (bufferedImage0.getRGB(x, y) != bufferedImage1.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static void setLookAndFeel(UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
        } catch (UnsupportedLookAndFeelException ignored) {
            System.out.println("Unsupported L&F: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            System.out.println("Testing L&F: " + laf.getClassName());
            SwingUtilities.invokeAndWait(() -> setLookAndFeel(laf));

            BufferedImage left = test(ComponentOrientation.LEFT_TO_RIGHT);
            ImageIO.write(left, "png", new File("JTextAreaTest-left.png"));
            BufferedImage right = test(ComponentOrientation.RIGHT_TO_LEFT);
            ImageIO.write(right, "png", new File("JTextAreaTest-right.png"));
            if (compareBufferedImages(left, right)) {
                throw new RuntimeException("Orientation change is not effected");
            }
        }
    }

    private static BufferedImage test(ComponentOrientation orientation) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            frame = new JFrame("Swing JTextArea component");
            JTextArea ta = new JTextArea();
            ta.setText("Swing JTextArea component");
            ta.setName("jtext");
            ta.setComponentOrientation(orientation);
            frame.getContentPane().add(ta);
            frame.setSize(300, 100);
            frame.setUndecorated(true);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
        Thread.sleep(1000);
        SwingUtilities.invokeAndWait(() -> {
            bounds = frame.getBounds();
        });
        BufferedImage img = new BufferedImage(bounds.width, bounds.height, TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        frame.paint(g);
        g.dispose();
        Thread.sleep(1000);
        SwingUtilities.invokeAndWait(() -> frame.dispose());
        return img;
    }
}
