



import java.awt.*;
import java.awt.image.*;

public class CopyAreaOOB extends Canvas {

    private static boolean done;

    public void paint(Graphics g) {
        synchronized (this) {
            if (done) {
                return;
            }
        }

        int w = getWidth();
        int h = getHeight();

        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, w, h);

        g2d.setColor(Color.green);
        g2d.fillRect(0, 0, w, 10);

        g2d.setColor(Color.red);
        g2d.fillRect(0, 10, 50, h-10);

        
        
        g2d.copyArea(0, 10, 50, h-10, 60, 10);

        Toolkit.getDefaultToolkit().sync();

        synchronized (this) {
            done = true;
            notifyAll();
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

    private static void testRegion(BufferedImage bi, String name,
                                   int x1, int y1, int x2, int y2,
                                   int expected)
    {
        for (int y = y1; y < y2; y++) {
            for (int x = x1; x < x2; x++) {
                int actual = bi.getRGB(x, y);
                if (actual != expected) {
                    throw new RuntimeException("Test failed for " + name +
                                                       " region at x="+x+" y="+y+
                                                       " (expected="+
                                                       Integer.toHexString(expected) +
                                                       " actual="+
                                                       Integer.toHexString(actual) +
                                                       ")");
                }
            }
        }
    }

    public static void main(String[] args) {
        boolean show = (args.length == 1) && ("-show".equals(args[0]));

        CopyAreaOOB test = new CopyAreaOOB();
        Frame frame = new Frame();
        frame.setUndecorated(true);
        frame.add(test);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        
        synchronized (test) {
            while (!done) {
                try {
                    test.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException("Failed: Interrupted");
                }
            }
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {}

        
        BufferedImage capture = null;
        try {
            Robot robot = new Robot();
            Point pt1 = test.getLocationOnScreen();
            Rectangle rect = new Rectangle(pt1.x, pt1.y, 400, 400);
            capture = robot.createScreenCapture(rect);
        } catch (Exception e) {
            throw new RuntimeException("Problems creating Robot");
        } finally {
            if (!show) {
                frame.dispose();
            }
        }

        
        testRegion(capture, "green",          0,   0, 400,  10, 0xff00ff00);
        testRegion(capture, "original red",   0,  10,  50, 400, 0xffff0000);
        testRegion(capture, "background",    50,  10,  60, 400, 0xff000000);
        testRegion(capture, "in-between",    60,  10, 110,  20, 0xff000000);
        testRegion(capture, "copied red",    60,  20, 110, 400, 0xffff0000);
        testRegion(capture, "background",   110,  10, 400, 400, 0xff000000);
    }
}
