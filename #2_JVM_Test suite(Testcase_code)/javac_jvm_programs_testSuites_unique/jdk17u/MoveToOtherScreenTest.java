import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.InvocationTargetException;

public class MoveToOtherScreenTest {

    private static volatile boolean twoDisplays = true;

    private static final Canvas canvas = new Canvas();

    private static final Frame[] frms = new JFrame[2];

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice[] gds = ge.getScreenDevices();
                if (gds.length < 2) {
                    System.out.println("Test requires at least 2 displays");
                    twoDisplays = false;
                    return;
                }
                for (int i = 0; i < 2; i++) {
                    GraphicsConfiguration conf = gds[i].getConfigurations()[0];
                    JFrame frm = new JFrame("Frame " + i);
                    frm.setLocation(conf.getBounds().x, 0);
                    frm.setSize(new Dimension(400, 400));
                    frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frm.setVisible(true);
                    frms[i] = frm;
                }
                canvas.setBackground(Color.red);
                frms[0].add(canvas);
            }
        });
        if (!twoDisplays) {
            return;
        }
        Thread.sleep(200);
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                frms[1].add(canvas);
            }
        });
        for (Frame frm : frms) {
            frm.dispose();
        }
    }
}
