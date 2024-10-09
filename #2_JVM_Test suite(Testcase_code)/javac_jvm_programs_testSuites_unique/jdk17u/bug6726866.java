import java.awt.Color;
import java.awt.Window;
import javax.swing.JApplet;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

public class bug6726866 extends JApplet {

    public void init() {
        JFrame frame = new JFrame("bug6726866");
        frame.setUndecorated(true);
        setWindowNonOpaque(frame);
        JDesktopPane desktop = new JDesktopPane();
        desktop.setBackground(Color.GREEN);
        JInternalFrame iFrame = new JInternalFrame("Test", true, true, true, true);
        iFrame.add(new JLabel("internal Frame"));
        iFrame.setBounds(10, 10, 300, 200);
        iFrame.setVisible(true);
        desktop.add(iFrame);
        frame.add(desktop);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setVisible(true);
        frame.toFront();
    }

    public static void setWindowNonOpaque(Window window) {
        Color bg = window.getBackground();
        if (bg == null) {
            bg = new Color(0, 0, 0, 0);
        }
        window.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), 0));
    }
}
