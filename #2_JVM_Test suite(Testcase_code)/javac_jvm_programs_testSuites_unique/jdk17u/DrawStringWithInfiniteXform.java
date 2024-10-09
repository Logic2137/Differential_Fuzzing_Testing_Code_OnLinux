import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.Timer;
import java.util.TimerTask;

public class DrawStringWithInfiniteXform {

    Timer timer;

    boolean done;

    class ScheduleTask extends TimerTask {

        public void run() {
            timer.cancel();
            if (!done) {
                throw new RuntimeException("drawString with InfiniteXform transform takes long time");
            }
        }
    }

    public DrawStringWithInfiniteXform() {
        timer = new Timer();
        timer.schedule(new ScheduleTask(), 10000);
    }

    public static void main(String[] args) {
        DrawStringWithInfiniteXform test = new DrawStringWithInfiniteXform();
        test.start();
    }

    private void start() {
        float[] vals = new float[6];
        for (int i = 0; i < 6; i++) vals[i] = Float.POSITIVE_INFINITY;
        AffineTransform nanTX = new AffineTransform(vals);
        BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.rotate(Float.POSITIVE_INFINITY);
        Font font = g2d.getFont();
        Font xfiniteFont;
        for (int i = 0; i < 2000; i++) {
            xfiniteFont = font.deriveFont(Float.POSITIVE_INFINITY);
            g2d.setFont(xfiniteFont);
            g2d.drawString("abc", 20, 20);
        }
        done = true;
        System.out.println("Test passed");
    }
}
