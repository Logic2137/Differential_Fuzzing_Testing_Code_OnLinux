

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.CyclicBarrier;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import sun.awt.AppContext;
import sun.awt.SunToolkit;

import static javax.swing.UIManager.getInstalledLookAndFeels;


public final class bug6190373 {

    private static AppContext app1;
    private static AppContext app2;
    private static final int LOOP_COUNT = 10000;
    private static final CyclicBarrier barrier = new CyclicBarrier(2);

    public static void main(final String[] args) throws Exception {
        final Thread t1 = new Thread(new ThreadGroup("firstGroup"), () -> {
            app1 = SunToolkit.createNewAppContext();
            test(true);
        });
        final Thread t2 = new Thread(new ThreadGroup("secondGroup"), () -> {
            app2 = SunToolkit.createNewAppContext();
            test(false);
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        app1.dispose();
        app2.dispose();
    }

    private static void test(final boolean lock) {
        for (final UIManager.LookAndFeelInfo laf : getInstalledLookAndFeels()) {
            try {
                SwingUtilities.invokeAndWait(() -> setLookAndFeel(laf));
                barrier.await();
                SwingUtilities.invokeAndWait(() -> slam(lock));
                barrier.await();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void slam(final boolean lock) {
        JButton button = new JButton("HI");
        button.setSize(100, 100);
        BufferedImage image = new BufferedImage(100, 100,
                                                BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < LOOP_COUNT; i++) {
            Graphics g = image.getGraphics();
            if (lock) {
                synchronized (button.getTreeLock()) {
                    button.paint(g);
                }
            } else {
                button.paint(g);
            }
            g.dispose();
        }
    }

    private static void setLookAndFeel(final UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
            System.out.println("LookAndFeel: " + laf.getClassName());
        } catch (final UnsupportedLookAndFeelException ignored){
            System.out.println("Unsupported LookAndFeel: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
