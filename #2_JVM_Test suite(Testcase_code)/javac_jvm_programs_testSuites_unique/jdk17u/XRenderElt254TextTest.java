import java.awt.*;
import java.awt.MultipleGradientPaint.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class XRenderElt254TextTest extends Frame implements Runnable {

    public volatile boolean success = false;

    public void run() {
        Image dstImg = getGraphicsConfiguration().createCompatibleVolatileImage(400, 400);
        Graphics2D g = (Graphics2D) dstImg.getGraphics();
        StringBuilder strBuilder = new StringBuilder(254);
        for (int c = 0; c < 254; c++) {
            strBuilder.append('a');
        }
        for (int i = 0; i < 100; i++) {
            g.drawString(strBuilder.toString(), 20, 20);
            Toolkit.getDefaultToolkit().sync();
        }
        success = true;
    }

    public static void main(String[] args) throws Exception {
        XRenderElt254TextTest test = new XRenderElt254TextTest();
        new Thread(test).start();
        for (int i = 0; i < 30; i++) {
            Thread.sleep(1000);
            if (test.success) {
                return;
            }
        }
        throw new RuntimeException("Test Failed");
    }
}
