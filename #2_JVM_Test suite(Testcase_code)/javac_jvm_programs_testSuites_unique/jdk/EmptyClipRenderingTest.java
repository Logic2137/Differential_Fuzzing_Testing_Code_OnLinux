

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import javax.imageio.ImageIO;
import sun.awt.ConstrainableGraphics;


public class EmptyClipRenderingTest {
    static final int IMG_W = 400;
    static final int IMG_H = 400;

    
    static HashSet<Rectangle> rects;

    volatile boolean isActivated = false;
    volatile boolean isPainted;
    private static boolean showErrors = false;

    public EmptyClipRenderingTest() {
        
        initClips();

        HashSet<RuntimeException> errors = new HashSet<RuntimeException>();

        BufferedImage screenResult = testOnscreen();
        try {
            testResult(screenResult, "Screen");
        } catch (RuntimeException e) {
            errors.add(e);
        }

        BufferedImage destBI =
            new BufferedImage(IMG_W, IMG_H, BufferedImage.TYPE_INT_RGB);
        runTest((Graphics2D)destBI.getGraphics());
        try {
            testResult(destBI, "BufferedImage");
        } catch (RuntimeException e) {
            errors.add(e);
        }

        GraphicsConfiguration gc =
                GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice().getDefaultConfiguration();
        VolatileImage destVI = gc.createCompatibleVolatileImage(IMG_W, IMG_H);
        destVI.validate(gc);
        runTest((Graphics2D)destVI.getGraphics());
        try {
            testResult(destVI.getSnapshot(), "VolatileImage");
        } catch (RuntimeException e) {
            errors.add(e);
        }

        if (errors.isEmpty()) {
            System.err.println("Test PASSED.");
        } else {
            for (RuntimeException re : errors) {
                re.printStackTrace();
            }
            if (showErrors) {
                System.err.println("Test FAILED: "+ errors.size() +
                                   " subtest failures.");
            } else {
                throw new RuntimeException("Test FAILED: "+ errors.size() +
                                           " subtest failures.");
            }
        }
    }

    
    private static void add4Rects(HashSet<Rectangle> rects, Rectangle area) {
        if (area.width < 10 || area.height < 10) {
            rects.add(area);
            return;
        }
        
        rects.add(new Rectangle(area.x, area.y, 5, area.height));
        rects.add(new Rectangle(area.x + area.width - 5, area.y, 5, area.height));
        
        int width = area.width - 2*(5 + 1);
        rects.add(new Rectangle(area.x+6, area.y, width, 5));
        rects.add(new Rectangle(area.x+6, area.y + area.height - 5, width, 5));
        
        area.grow(-6, -6);
        add4Rects(rects, area);
    }

    
    private static void initClips() {
        rects = new HashSet<Rectangle>();
        add4Rects(rects, new Rectangle(0, 0, IMG_W, IMG_H));
        System.err.println("Total number of test rects: " + rects.size());
    }

    
    private BufferedImage testOnscreen() throws HeadlessException {
        final Canvas destComponent;
        final Object lock = new Object();
        Frame f = new Frame("Test Frame");
        f.setUndecorated(true);
        f.add(destComponent = new Canvas() {
            public void paint(Graphics g) {
                isPainted = true;
            }
            public Dimension getPreferredSize() {
                return new Dimension(IMG_W, IMG_H);
            }
        });
        f.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                if (!isActivated) {
                    synchronized (lock) {
                        isActivated = true;
                        lock.notify();
                    }
                }
            }
        });
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        synchronized(lock) {
            while (!isActivated) {
                try {
                    lock.wait(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        Robot r;
        try {
            r = new Robot();
        } catch (AWTException ex) {
            throw new RuntimeException("Can't create Robot");
        }
        BufferedImage bi;
        int attempt = 0;
        do {
            if (++attempt > 10) {
                throw new RuntimeException("Too many attempts: " + attempt);
            }
            isPainted = false;
            runTest((Graphics2D) destComponent.getGraphics());
            r.waitForIdle();
            Toolkit.getDefaultToolkit().sync();
            bi = r.createScreenCapture(
                    new Rectangle(destComponent.getLocationOnScreen().x,
                            destComponent.getLocationOnScreen().y,
                            destComponent.getWidth(),
                            destComponent.getHeight()));
        } while (isPainted);
        f.setVisible(false);
        f.dispose();
        return bi;
    }

    
    void runTest(Graphics2D destGraphics) {
        destGraphics.setColor(Color.black);
        destGraphics.fillRect(0, 0, IMG_W, IMG_H);

        destGraphics.setColor(Color.red);
        for (Rectangle clip : rects) {
            Graphics2D g2d = (Graphics2D)destGraphics.create();
            g2d.setColor(Color.red);
            
            if (g2d instanceof ConstrainableGraphics) {
                ((ConstrainableGraphics)g2d).constrain(clip.x, clip.y,
                                                       clip.width, clip.height);
            }
            g2d.setClip(clip);

            for (Rectangle renderRegion : rects) {
                if (renderRegion != clip) {
                    
                    Graphics2D rG = (Graphics2D)
                        g2d.create(renderRegion.x, renderRegion.y,
                                   renderRegion.width, renderRegion.height);
                    rG.fillRect(0,0, renderRegion.width, renderRegion.height);
                }
            }
        }
    }

    void testResult(final BufferedImage bi, final String desc) {
        for (int y = 0; y < bi.getHeight(); y++) {
            for (int x = 0; x < bi.getWidth(); x++) {
                if (bi.getRGB(x, y) != Color.black.getRGB()) {
                    if (showErrors) {
                        Frame f = new Frame("Error: " + desc);
                        f.add(new Component() {
                            public void paint(Graphics g) {
                                g.drawImage(bi, 0, 0, null);
                            }
                            public Dimension getPreferredSize() {
                                return new Dimension(bi.getWidth(),
                                                     bi.getHeight());
                            }
                        });
                        f.pack();
                        f.setVisible(true);
                    }
                    try {
                        String fileName =
                            "EmptyClipRenderingTest_"+desc+"_res.png";
                        System.out.println("Writing resulting image: "+fileName);
                        ImageIO.write(bi, "png", new File(fileName));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    throw new RuntimeException("Dest: "+desc+
                        " was rendered to at x="+
                        x + " y=" + y +
                        " pixel="+Integer.toHexString(bi.getRGB(x,y)));
                }
            }
        }
    }

    public static void main(String argv[]) {
        for (String arg : argv) {
            if (arg.equals("-show")) {
                showErrors = true;
            } else {
                usage("Incorrect argument:" + arg);
            }
        }
        new EmptyClipRenderingTest();
    }

    private static void usage(String string) {
        System.out.println(string);
        System.out.println("Usage: EmptyClipRenderingTest [-show]");
    }
}
