


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NoResizeEventOnDMChangeTest {
    public static void main(String[] args) {
        final GraphicsDevice gd = GraphicsEnvironment.
            getLocalGraphicsEnvironment().getDefaultScreenDevice();

        if (!gd.isFullScreenSupported()) {
            System.out.println("Full screen not supported, test passed");
            return;
        }

        DisplayMode dm = gd.getDisplayMode();
        final DisplayMode dms[] = new DisplayMode[2];
        for (DisplayMode dm1 : gd.getDisplayModes()) {
            if (dm1.getWidth()  != dm.getWidth() ||
                dm1.getHeight() != dm.getHeight())
            {
                dms[0] = dm1;
                break;
            }
        }
        if (dms[0] == null) {
            System.out.println("Test Passed: all DMs have same dimensions");
            return;
        }
        dms[1] = dm;

        Frame f = new Frame() {
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.red);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.green);
                g.drawRect(0, 0, getWidth()-1, getHeight()-1);
            }
        };
        f.setUndecorated(true);
        testFSWindow(gd, dms, f);

        Window w = new Window(f) {
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.magenta);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.cyan);
                g.drawRect(0, 0, getWidth()-1, getHeight()-1);
            }
        };
        testFSWindow(gd, dms, w);
        System.out.println("Test Passed.");
    }

    private static void testFSWindow(final GraphicsDevice gd,
                                     final DisplayMode dms[],
                                     final Window fsWin)
    {
        System.out.println("Testing FS window: "+fsWin);
        Component c = new Canvas() {
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.blue);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.magenta);
                g.drawRect(0, 0, getWidth()-1, getHeight()-1);
                g.setColor(Color.red);
                g.drawString("FS Window   : " + fsWin, 50, 50);
                DisplayMode dm =
                    getGraphicsConfiguration().getDevice().getDisplayMode();
                g.drawString("Display Mode: " +
                             dm.getWidth() + "x" + dm.getHeight(), 50, 75);
            }
        };
        fsWin.add("Center", c);
        fsWin.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fsWin.dispose();
                if (fsWin.getOwner() != null) {
                    fsWin.getOwner().dispose();
                }
            }
        });

        try {
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    gd.setFullScreenWindow(fsWin);
                }
            });
        } catch (Exception ex) {}

        sleep(1000);

        final ResizeEventChecker r1 = new ResizeEventChecker();
        final ResizeEventChecker r2 = new ResizeEventChecker();

        if (gd.isDisplayChangeSupported()) {
            fsWin.addComponentListener(r1);
            c.addComponentListener(r2);
            for (final DisplayMode dm1 : dms) {
                try {
                    EventQueue.invokeAndWait(new Runnable() {
                        public void run() {
                            System.err.printf("----------- Setting DM %dx%d:\n",
                                              dm1.getWidth(), dm1.getHeight());
                            try {
                                gd.setDisplayMode(dm1);
                                r1.incDmChanges();
                                r2.incDmChanges();
                            } catch (IllegalArgumentException iae) {}
                        }
                    });
                } catch (Exception ex) {}
                for (int i = 0; i < 3; i++) {
                    fsWin.repaint();
                    sleep(1000);
                }
            }
            fsWin.removeComponentListener(r1);
            c.removeComponentListener(r2);
        }
        try {
           EventQueue.invokeAndWait(new Runnable() {
               public void run() {
                   gd.setFullScreenWindow(null);
                    fsWin.dispose();
                    if (fsWin.getOwner() != null) {
                        fsWin.getOwner().dispose();
                    }
                }
            });
        } catch (Exception ex) {}

        System.out.printf("FS Window: resizes=%d, dm changes=%d\n",
                           r1.getResizes(), r1.getDmChanges());
        System.out.printf("Component: resizes=%d, dm changes=%d\n",
                          r2.getResizes(), r2.getDmChanges());
        if (r1.getResizes() < r1.getDmChanges()) {
            throw new RuntimeException("FS Window didn't receive all resizes!");
        }
        if (r2.getResizes() < r2.getDmChanges()) {
            throw new RuntimeException("Component didn't receive all resizes!");
        }
    }

    static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {}
    }
    static class ResizeEventChecker extends ComponentAdapter {
        int dmChanges;
        int resizes;

        @Override
        public synchronized void componentResized(ComponentEvent e) {
            System.out.println("Received resize event for "+e.getSource());
            resizes++;
        }
        public synchronized int getResizes() {
            return resizes;
        }
        public synchronized void incDmChanges() {
            dmChanges++;
        }
        public synchronized int getDmChanges() {
            return dmChanges;
        }
    }
}
