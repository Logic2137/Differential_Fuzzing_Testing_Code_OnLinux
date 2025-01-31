import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import static java.awt.font.TextAttribute.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class DrawStrSuper extends Component {

    int angle = 0;

    static boolean interactive = false;

    int wid = 400, hgt = 400;

    BufferedImage bi = null;

    void paintImage() {
        if (bi == null) {
            bi = new BufferedImage(wid, hgt, BufferedImage.TYPE_INT_RGB);
        }
        Graphics2D g2d = bi.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, wid, hgt);
        g2d.translate(200, 200);
        Font fnt = new Font("Arial", Font.PLAIN, 20);
        fnt = fnt.deriveFont(60.0f);
        HashMap attrMap = new HashMap();
        AffineTransform aff = AffineTransform.getRotateInstance(angle * Math.PI / 180.0);
        attrMap.put(SUPERSCRIPT, SUPERSCRIPT_SUPER);
        attrMap.put(TRANSFORM, aff);
        fnt = fnt.deriveFont(attrMap);
        g2d.setFont(fnt);
        g2d.setColor(Color.yellow);
        TextLayout tl = new TextLayout("Text", fnt, g2d.getFontRenderContext());
        g2d.fill(tl.getBounds());
        g2d.setColor(Color.black);
        g2d.drawLine(-3, 0, 3, 0);
        g2d.drawLine(0, -3, 0, 3);
        g2d.setColor(Color.blue);
        g2d.drawString("Text", 0, 0);
        g2d.setColor(Color.red);
        tl.draw(g2d, 0f, 0f);
        int blue = Color.blue.getRGB();
        for (int px = 0; px < wid; px++) {
            for (int py = 0; py < hgt; py++) {
                int rgb = bi.getRGB(px, py);
                if (rgb == blue) {
                    throw new RuntimeException("Unexpected color : " + Integer.toHexString(rgb) + " at x=" + px + " y=" + py);
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        paintImage();
        g.drawImage(bi, 0, 0, null);
    }

    static class Runner extends Thread {

        DrawStrSuper dss;

        Runner(DrawStrSuper dss) {
            this.dss = dss;
        }

        public void run() {
            while (true) {
                if (!interactive && dss.angle > 360) {
                    return;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    return;
                }
                dss.angle += 10;
                dss.repaint();
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

    public static void main(String[] argv) throws InterruptedException {
        if (argv.length > 0)
            interactive = true;
        Frame f = new Frame("Text bounds test");
        f.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        DrawStrSuper dss = new DrawStrSuper();
        f.add(dss, BorderLayout.CENTER);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        Runner runner = new Runner(dss);
        runner.start();
        runner.join();
    }
}
