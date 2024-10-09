

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE;
import static javax.swing.UIManager.getInstalledLookAndFeels;


public final class MisplacedBorder implements Runnable {

    public static final int W = 400;
    public static final int H = 400;

    public static void main(final String[] args) throws Exception {
        for (final UIManager.LookAndFeelInfo laf : getInstalledLookAndFeels()) {
            SwingUtilities.invokeAndWait(() -> setLookAndFeel(laf));
            SwingUtilities.invokeAndWait(new MisplacedBorder());
        }
        System.out.println("Test passed");
    }

    @Override
    public void run() {
        final JMenuBar menubar = new JMenuBar();
        menubar.add(new JMenu(""));
        menubar.add(new JMenu(""));
        final JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setJMenuBar(menubar);
        frame.setSize(W / 3, H / 3);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        
        final BufferedImage bi1 = step1(menubar);

        
        final BufferedImage bi2 = step2(menubar);
        frame.dispose();

        for (int x = 0; x < W; ++x) {
            for (int y = 0; y < H; ++y) {
                if (bi1.getRGB(x, y) != bi2.getRGB(x, y)) {
                    try {
                        ImageIO.write(bi1, "png", new File("image1.png"));
                        ImageIO.write(bi2, "png", new File("image2.png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    throw new RuntimeException("Failed: wrong color");
                }
            }
        }
    }

    
    private BufferedImage step1(final JMenuBar menubar) {
        final BufferedImage bi1 = new BufferedImage(W, H, TYPE_INT_ARGB_PRE);
        final Graphics2D g2d = bi1.createGraphics();
        g2d.scale(2, 2);
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, W, H);
        menubar.paintAll(g2d);
        g2d.dispose();
        return bi1;
    }

    
    private BufferedImage step2(final JMenuBar menubar) {
        final BufferedImage bi2 = new BufferedImage(W, H, TYPE_INT_ARGB_PRE);
        final Graphics2D g2d2 = bi2.createGraphics();
        g2d2.scale(2, 2);
        g2d2.setColor(Color.RED);
        g2d2.fillRect(0, 0, W, H);
        menubar.paintAll(g2d2);
        menubar.getBorder().paintBorder(menubar, g2d2, menubar.getX(), menubar
                .getX(), menubar.getWidth(), menubar.getHeight());
        g2d2.dispose();
        return bi2;
    }

    private static void setLookAndFeel(final UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
            System.out.println("LookAndFeel: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException |
                UnsupportedLookAndFeelException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
