import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class LineClipTest extends Component implements Runnable {

    int clipBumpVal = 5;

    static int clipSize = 100;

    int clipX1;

    int clipY1;

    static final int NUM_QUADS = 9;

    Point[][] quadrants = new Point[NUM_QUADS][];

    static boolean dynamic = false;

    BufferedImage imageChecker = null;

    Color unclippedColor = Color.blue;

    Color clippedColor = Color.red;

    int testW = -1, testH = -1;

    VolatileImage testImage = null;

    static boolean keepRunning = false;

    static boolean quickTest = false;

    static boolean rectTest = false;

    static boolean runTestDone = false;

    static Frame f = null;

    boolean gridError(Graphics g) {
        boolean error = false;
        if (imageChecker == null || (imageChecker.getWidth() != testW) || (imageChecker.getHeight() != testH)) {
            GraphicsConfiguration gc = getGraphicsConfiguration();
            ColorModel cm = gc.getColorModel();
            WritableRaster wr = cm.createCompatibleWritableRaster(getWidth(), getHeight());
            imageChecker = new BufferedImage(cm, wr, cm.isAlphaPremultiplied(), null);
        }
        Graphics gChecker = imageChecker.getGraphics();
        gChecker.drawImage(testImage, 0, 0, this);
        int clippedPixelColor = clippedColor.getRGB();
        int unclippedPixelColor = unclippedColor.getRGB();
        int wrongPixelColor = clippedPixelColor;
        boolean insideClip = false;
        for (int row = 0; row < getHeight(); ++row) {
            for (int col = 0; col < getWidth(); ++col) {
                if (row >= clipY1 && row < (clipY1 + clipSize) && col >= clipX1 && col < (clipX1 + clipSize)) {
                    wrongPixelColor = unclippedPixelColor;
                } else {
                    wrongPixelColor = clippedPixelColor;
                }
                int pixel = imageChecker.getRGB(col, row);
                if (pixel == wrongPixelColor) {
                    System.out.println("FAILED: pixel = " + Integer.toHexString(pixel) + " at (x, y) = " + col + ", " + row);
                    g.setColor(Color.magenta);
                    g.drawRect(col - 1, row - 1, 2, 2);
                    error = true;
                }
            }
        }
        return error;
    }

    void drawLineGrid(Graphics screenGraphics, Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        for (int srcQuad = 0; srcQuad < NUM_QUADS; ++srcQuad) {
            for (int dstQuad = 0; dstQuad < NUM_QUADS; ++dstQuad) {
                for (int srcPoint = 0; srcPoint < quadrants[srcQuad].length; ++srcPoint) {
                    int sx = quadrants[srcQuad][srcPoint].x;
                    int sy = quadrants[srcQuad][srcPoint].y;
                    for (int dstPoint = 0; dstPoint < quadrants[dstQuad].length; ++dstPoint) {
                        int dx = quadrants[dstQuad][dstPoint].x;
                        int dy = quadrants[dstQuad][dstPoint].y;
                        if (!rectTest) {
                            g.setColor(unclippedColor);
                            g.drawLine(sx, sy, dx, dy);
                            g.setClip(clipX1, clipY1, clipSize, clipSize);
                            g.setColor(clippedColor);
                            g.drawLine(sx, sy, dx, dy);
                        } else {
                            g.setColor(unclippedColor);
                            int w = dx - sx;
                            int h = dy - sy;
                            g.drawRect(sx, sy, w, h);
                            g.setClip(clipX1, clipY1, clipSize, clipSize);
                            g.setColor(clippedColor);
                            g.drawRect(sx, sy, w, h);
                        }
                        g.setClip(null);
                    }
                    if (!dynamic) {
                        screenGraphics.drawImage(testImage, 0, 0, this);
                        if (!quickTest && gridError(g)) {
                            throw new java.lang.RuntimeException("Failed");
                        }
                    }
                }
            }
            if (!dynamic && quickTest && gridError(g)) {
                throw new java.lang.RuntimeException("Failed");
            }
        }
        if (!dynamic) {
            System.out.println("PASSED");
            if (!keepRunning) {
                f.dispose();
            }
        }
    }

    public void paint(Graphics g) {
        if (dynamic || testImage == null || getWidth() != testW || getHeight() != testH) {
            runTest(g);
        }
        if (testImage != null) {
            g.drawImage(testImage, 0, 0, this);
        }
    }

    public void runTest(Graphics screenGraphics) {
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        clipX1 = (getWidth() - clipSize) / 2;
        clipY1 = (getHeight() - clipSize) / 2;
        int clipX2 = clipX1 + clipSize;
        int clipY2 = clipY1 + clipSize;
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int leftX = 0;
        int topY = 0;
        int rightX = getWidth() - 1;
        int bottomY = getHeight() - 1;
        int quadIndex = 0;
        int xOffset = 0;
        int yOffset = 0;
        if (quadrants[0] == null) {
            for (int i = 0; i < 9; ++i) {
                int numPoints = (i == 4) ? 9 : 3;
                quadrants[i] = new Point[numPoints];
            }
        }
        quadrants[quadIndex] = new Point[] { new Point(leftX + xOffset, clipY1 - 1 - yOffset), new Point(leftX + xOffset, topY + yOffset), new Point(clipX1 - 1 - xOffset, topY + yOffset) };
        quadIndex++;
        yOffset++;
        quadrants[quadIndex] = new Point[] { new Point(clipX1 + 1 + xOffset, topY + yOffset), new Point(centerX + xOffset, topY + yOffset), new Point(clipX2 - 1 - xOffset, topY + yOffset) };
        quadIndex++;
        ++yOffset;
        quadrants[quadIndex] = new Point[] { new Point(clipX2 + 1 + xOffset, topY + yOffset), new Point(rightX - xOffset, topY + yOffset), new Point(rightX - xOffset, clipY1 - 1 - yOffset) };
        quadIndex++;
        yOffset = 0;
        ++xOffset;
        quadrants[quadIndex] = new Point[] { new Point(leftX + xOffset, clipY1 + 1 + yOffset), new Point(leftX + xOffset, centerY + yOffset), new Point(leftX + xOffset, clipY2 - 1 - yOffset) };
        quadIndex++;
        ++yOffset;
        quadrants[quadIndex] = new Point[] { new Point(clipX1 + 1 + xOffset, clipY1 + 1 + yOffset), new Point(centerX + xOffset, clipY1 + 1 + yOffset), new Point(clipX2 - 1 - xOffset, clipY1 + 1 + yOffset), new Point(clipX1 + 1 + xOffset, centerY + yOffset), new Point(centerX + xOffset, centerY + yOffset), new Point(clipX2 - 1 - xOffset, centerY + yOffset), new Point(clipX1 + 1 + xOffset, clipY2 - 1 - yOffset), new Point(centerX + xOffset, clipY2 - 1 - yOffset), new Point(clipX2 - 1 - xOffset, clipY2 - 1 - yOffset) };
        quadIndex++;
        ++yOffset;
        quadrants[quadIndex] = new Point[] { new Point(rightX - xOffset, clipY1 + 1 + yOffset), new Point(rightX - xOffset, centerY + yOffset), new Point(rightX - xOffset, clipY2 - 1 - yOffset) };
        quadIndex++;
        yOffset = 0;
        ++xOffset;
        quadrants[quadIndex] = new Point[] { new Point(leftX + xOffset, clipY2 + 1 + yOffset), new Point(leftX + xOffset, bottomY - yOffset), new Point(clipX1 - 1 - xOffset, bottomY - yOffset) };
        quadIndex++;
        ++yOffset;
        quadrants[quadIndex] = new Point[] { new Point(clipX1 + 1 + xOffset, bottomY - yOffset), new Point(centerX + xOffset, bottomY - yOffset), new Point(clipX2 - 1 - xOffset, bottomY - yOffset) };
        quadIndex++;
        ++yOffset;
        quadrants[quadIndex] = new Point[] { new Point(clipX2 + 1 + xOffset, bottomY - yOffset), new Point(rightX - xOffset, bottomY - yOffset), new Point(rightX - xOffset, clipY2 + 1 + yOffset) };
        if (testImage != null) {
            testImage.flush();
        }
        testW = getWidth();
        testH = getHeight();
        testImage = createVolatileImage(testW, testH);
        Graphics g = testImage.getGraphics();
        do {
            int valCode = testImage.validate(getGraphicsConfiguration());
            if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
                testImage.flush();
                testImage = createVolatileImage(testW, testH);
                g = testImage.getGraphics();
            }
            drawLineGrid(screenGraphics, g);
        } while (testImage.contentsLost());
        if (dynamic) {
            g.setClip(null);
            g.setColor(Color.black);
            g.drawRect(clipX1, clipY1, clipSize, clipSize);
            screenGraphics.drawImage(testImage, 0, 0, this);
        }
        runTestDone = true;
    }

    public void run() {
        while (true) {
            clipSize += clipBumpVal;
            if (clipSize > getWidth() || clipSize < 0) {
                clipBumpVal = -clipBumpVal;
                clipSize += clipBumpVal;
            }
            update(getGraphics());
            try {
                Thread.sleep(50);
            } catch (Exception e) {
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals("-dynamic")) {
                dynamic = true;
            } else if (args[i].equals("-rect")) {
                rectTest = true;
            } else if (args[i].equals("-quick")) {
                quickTest = true;
            } else if (args[i].equals("-keep")) {
                keepRunning = true;
            } else {
                try {
                    clipSize = Integer.parseInt(args[i]);
                } catch (Exception e) {
                }
            }
        }
        f = new Frame();
        f.setSize(500, 500);
        LineClipTest test = new LineClipTest();
        f.add(test);
        if (dynamic) {
            Thread t = new Thread(test);
            t.start();
        }
        f.setVisible(true);
        while (!runTestDone) {
            try {
                Thread.sleep(50);
            } catch (Exception e) {
            }
        }
    }
}
