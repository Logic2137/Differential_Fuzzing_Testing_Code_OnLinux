



import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.lang.Exception;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;


public class DisplayChangeVITest extends JFrame implements Runnable {

    private final Random rnd = new Random();
    private VolatileImage bb;
    private BufferedImage sprite;
    private VolatileImage volSprite;

    private static boolean done = false;
    private static final Object lock = new Object();
    private static final int TEST_REPS = 3;

    private ArrayList<DisplayMode> dms;

    DisplayChangeVITest() {
        selectDisplayModes();
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    synchronized (lock) {
                        done = true;
                    }
                }
            }
        });
        sprite = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        sprite.getRaster().getDataBuffer();
        Graphics g = sprite.getGraphics();
        g.setColor(Color.yellow);
        g.fillRect(0, 0, sprite.getWidth(), sprite.getHeight());
    }

    void render(Graphics g) {
        do {
            
            initBackbuffer();

            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());

            Graphics gg = bb.getGraphics();
            gg.setColor(new Color(rnd.nextInt(0x00ffffff)));
            gg.fillRect(0, 0, bb.getWidth(), bb.getHeight());
            for (int x = 0; x < 10; x++) {
                gg.drawImage(sprite, x*200, 0, null);
                gg.drawImage(volSprite, x*200, 500, null);
            }

            g.drawImage(bb, 0, 0, null);
        } while (bb.contentsLost());
    }

    private static void sleep(long msec) {
        try { Thread.sleep(msec); } catch (InterruptedException e) {}
    }

    private int reps = 0;
    public void run() {
        GraphicsDevice gd = getGraphicsConfiguration().getDevice();
        if (gd.isDisplayChangeSupported() && dms.size() > 0) {
            while (!done && reps++ < TEST_REPS) {
                for (DisplayMode dm : dms) {
                    System.err.printf("Entering DisplayMode[%dx%dx%d]\n",
                        dm.getWidth(), dm.getHeight(), dm.getBitDepth());
                    gd.setDisplayMode(dm);

                    initBackbuffer();
                    for (int i = 0; i < 10; i++) {
                        
                        render(getGraphics());
                        
                        repaint();
                        sleep(100);
                    }
                    sleep(1500);
                }
            }
        } else {
            System.err.println("Display mode change " +
                               "not supported. Test passed.");
        }
        dispose();
        synchronized (lock) {
            done = true;
            lock.notify();
        }
    }

    private void createBackbuffer() {
        if (bb == null ||
            bb.getWidth() != getWidth() || bb.getHeight() != getHeight())
        {
            bb = createVolatileImage(getWidth(), getHeight());
        }
    }

    private void initBackbuffer() {
        createBackbuffer();

        int res = bb.validate(getGraphicsConfiguration());
        if (res == VolatileImage.IMAGE_INCOMPATIBLE) {
            bb = null;
            createBackbuffer();
            bb.validate(getGraphicsConfiguration());
            res = VolatileImage.IMAGE_RESTORED;
        }
        if (res == VolatileImage.IMAGE_RESTORED) {
            Graphics g = bb.getGraphics();
            g.setColor(new Color(rnd.nextInt(0x00ffffff)));
            g.fillRect(0, 0, bb.getWidth(), bb.getHeight());

            volSprite = createVolatileImage(100, 100);
        }
        volSprite.validate(getGraphicsConfiguration());
    }

    private void selectDisplayModes() {
        GraphicsDevice gd =
            GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice();
        dms = new ArrayList<DisplayMode>();
        DisplayMode dmArray[] = gd.getDisplayModes();
        boolean found8 = false, found16 = false,
                found24 = false, found32 = false;
        for (DisplayMode dm : dmArray) {
            if (!found8 &&
                (dm.getBitDepth() == 8 ||
                 dm.getBitDepth() == DisplayMode.BIT_DEPTH_MULTI)  &&
                (dm.getWidth() >= 800 && dm.getWidth() < 1024))
            {
                dms.add(dm);
                found8 = true;
                continue;
            }
            if (!found32 &&
                (dm.getBitDepth() == 32 ||
                 dm.getBitDepth() == DisplayMode.BIT_DEPTH_MULTI)  &&
                dm.getWidth() >= 1280)
            {
                dms.add(dm);
                found32 = true;
                continue;
            }
            if (!found16 &&
               dm.getBitDepth() == 16 &&
                (dm.getWidth() >= 1024 && dm.getWidth() < 1280))
            {
                dms.add(dm);
                found16 = true;
                continue;
            }
            if (found8 && found16 && found32) {
                break;
            }
        }
        System.err.println("Found display modes:");
        for (DisplayMode dm : dms) {
            System.err.printf("DisplayMode[%dx%dx%d]\n",
                dm.getWidth(), dm.getHeight(), dm.getBitDepth());
        }
    }

    public static void main(String[] args) throws Exception {
        DisplayChangeVITest test = new DisplayChangeVITest();
        GraphicsDevice gd =
            GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(test);
            Thread t = new Thread(test);
            t.run();
            synchronized (lock) {
                while (!done) {
                    try {
                        lock.wait(50);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            System.err.println("Test Passed.");
        } else {
            System.err.println("Full screen not supported. Test passed.");
        }
    }
}
