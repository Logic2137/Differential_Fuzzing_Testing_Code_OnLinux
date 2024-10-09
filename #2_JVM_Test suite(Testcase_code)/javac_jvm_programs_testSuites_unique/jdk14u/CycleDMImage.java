

import java.awt.Color;
import java.awt.Component;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Transparency;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class CycleDMImage extends Component implements Runnable, KeyListener {
    
    boolean painted = false;
    boolean earlyExit = false;
    Image rImage = null, wImage = null, bImage = null;
    int imgSize = 10;
    Robot robot = null;
    volatile static boolean done = false;
    static String errorMessage = null;

    public synchronized void paint(Graphics g) {
        if (!painted) {
            painted = true;
            (new Thread(this)).start();
        }
        if (rImage == null) {
            GraphicsConfiguration gc = getGraphicsConfiguration();
            rImage = gc.createCompatibleImage(imgSize, imgSize);
            wImage = gc.createCompatibleImage(imgSize, imgSize,
                                              Transparency.BITMASK);
            bImage = gc.createCompatibleImage(imgSize, imgSize,
                                              Transparency.TRANSLUCENT);
            Graphics imgGraphics = rImage.getGraphics();
            imgGraphics.setColor(Color.red);
            imgGraphics.fillRect(0, 0, imgSize, imgSize);
            imgGraphics = wImage.getGraphics();
            imgGraphics.setColor(Color.white);
            imgGraphics.fillRect(0, 0, imgSize, imgSize);
            imgGraphics = bImage.getGraphics();
            imgGraphics.setColor(Color.blue);
            imgGraphics.fillRect(0, 0, imgSize, imgSize);
        }
        g.setColor(Color.green);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(rImage, 0, 0, this);
        g.drawImage(wImage, imgSize, 0, this);
        g.drawImage(bImage, imgSize*2, 0, this);
        g.drawImage(rImage, 0, getHeight()-imgSize, null);
        g.drawImage(rImage, getWidth()-imgSize, getHeight()-imgSize, null);
        g.drawImage(rImage, getWidth()-imgSize, 0, null);
    }

    static void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {}
    }

    public boolean checkResult(DisplayMode dm) {
        if (robot == null) {
            try {
                robot = new Robot();
            }
            catch (Exception e) {
                errorMessage = "Problems creating Robot";
                return false;
            }
        }
        Rectangle bounds = getGraphicsConfiguration().getBounds();
        int pixels[] = new int[imgSize * 4];
        BufferedImage clientPixels =
            robot.createScreenCapture(new Rectangle(bounds.x, bounds.y,
                                                    imgSize*4, 1));
        clientPixels.getRGB(0, 0, imgSize * 4, 1, pixels, 0, getWidth());
        
        int colors[] = {0xffff0000, 0xffffffff, 0xff0000ff, 0xff00ff00};
        for (int color = 0; color < 4; ++color) {
            for (int i = 0; i < imgSize; ++i) {
                int pixelIndex = imgSize * color + i;
                if (pixels[pixelIndex] != colors[color]) {
                    errorMessage = "\n    DisplayMode(" +
                        dm.getWidth() + " x " +
                        dm.getHeight() + " x " +
                        dm.getBitDepth() + "bpp x " +
                        dm.getRefreshRate() +
                        ")\n    Pixel " + i +
                        ": Expected " +
                        Integer.toHexString(colors[color]) +
                        ", got " +
                        Integer.toHexString(pixels[pixelIndex]) +
                        " at " + i;
                    return false;
                }
            }
        }
        return true;
    }

    boolean displayModesEqual(DisplayMode dm1, DisplayMode dm2) {
        if (dm1.equals(dm2)) {
            return true;
        }
        
        
        
        if (dm1.getWidth() != dm2.getWidth() ||
            dm1.getHeight() != dm2.getHeight() ||
            dm1.getBitDepth() != dm2.getBitDepth())
        {
            
            return false;
        }
        
        
        
        if (dm1.getRefreshRate() == DisplayMode.REFRESH_RATE_UNKNOWN ||
            dm2.getRefreshRate() == DisplayMode.REFRESH_RATE_UNKNOWN)
        {
            return true;
        }
        return false;
    }

    public void run() {
        GraphicsDevice gd = getGraphicsConfiguration().getDevice();
        gd.setFullScreenWindow((Window) getParent());
        
        
        delay(1000);

        if (!gd.isDisplayChangeSupported()) {
            System.err.println("Display change is not supported,"+
                               " the test is considered passed.");
            finished();
            return;
        }

        
        
        
        
        
        
        
        
        ArrayList<DisplayMode> dmSubset = new ArrayList<>();
        for (final DisplayMode dm : gd.getDisplayModes()) {
            boolean skip = false;
            for (final DisplayMode dmUnique : dmSubset) {
                int bitDepth = dm.getBitDepth();
                if (bitDepth == 24 ||
                        (dmUnique.getWidth() == dm.getWidth() &&
                         dmUnique.getHeight() == dm.getHeight() &&
                         dmUnique.getBitDepth() == dm.getBitDepth())) {
                    skip = true;
                    break;
                }
            }
            if (!skip) {
                dmSubset.add(dm);
            }
        }

        
        
        
        

        for (DisplayMode newDM : dmSubset) {
            gd.setDisplayMode(newDM);
            while (!displayModesEqual(newDM, gd.getDisplayMode())) {
                delay(100);
            }
            
            delay(4000);

            
            if (!checkResult(newDM)) {
                finished();
                return;
            }

            
            if (earlyExit) {
                System.out.println("Exiting test early, by request");
                System.exit(0);
            }
        }

        
        System.out.println("Passed");
        finished();
    }

    public static void finished() {
        synchronized (CycleDMImage.class) {
            done = true;
            CycleDMImage.class.notify();
        }
    }

    

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            earlyExit = true;
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public static void main(String args[]) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (final GraphicsDevice gd: ge.getScreenDevices()) {
            if (!gd.isFullScreenSupported()) {
                System.err.println("FullScreen mode is not supported,"+
                                           " the test is considered passed.");
                continue;
            }
            done = false;
            Frame frame = new Frame(gd.getDefaultConfiguration());
            try {
                frame.setSize(400, 400);
                frame.setUndecorated(true);
                CycleDMImage comp = new CycleDMImage();
                frame.addKeyListener(comp);
                frame.add(comp);
                frame.setVisible(true);
                
                synchronized (CycleDMImage.class) {
                    while (!done) {
                        try {
                            CycleDMImage.class.wait(100);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            } finally {
                frame.dispose();
            }
            if (errorMessage != null) {
                throw new RuntimeException(errorMessage);
            }
            
            
            delay(4000);
        }
    }
}
