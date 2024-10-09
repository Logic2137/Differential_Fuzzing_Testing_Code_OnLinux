


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.util.concurrent.CountDownLatch;

public class DrawXORModeTest extends Component {

    public static void main(String[] args) {
        final DrawXORModeTest c = new DrawXORModeTest();

        final Frame f = new Frame("XOR mode test");
        f.add(c);
        f.pack();

        f.setVisible(true);

        try {
            c.checkResult();
        } finally {
            f.dispose();
        }
    }

    @Override
    public void paint(Graphics g) {
        if (g == null || !(g instanceof Graphics2D)) {
            return;
        }
        g.setColor(Color.white);
        g.setXORMode(Color.black);
        Graphics2D dg = (Graphics2D) g;
        Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f,
                new float[]{1.0f, 1.0f},
                0.0f);
        dg.setStroke(stroke);
        try {
            dg.draw(new Line2D.Float(10, 10, 20, 20));
        } catch (Throwable e) {
            synchronized (this) {
                theError = e;
            }
        } finally {
            didDraw.countDown();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 100);
    }

    public void checkResult() {
        try {
            didDraw.await();
        } catch (InterruptedException e) {
        }

        synchronized (this) {
            if (theError != null) {
                System.out.println("Error: " + theError);

                throw new RuntimeException("Test FAILED.");
            }
        }
        System.out.println("Test PASSED.");

    }

    private Throwable theError = null;

    private final CountDownLatch didDraw = new CountDownLatch(1);
}
