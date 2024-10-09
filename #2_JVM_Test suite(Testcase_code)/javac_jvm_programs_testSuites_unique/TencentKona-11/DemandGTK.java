



import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import java.awt.Robot;

public class DemandGTK {

    static JFrame frame;
    public static void createAndShow() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch(Exception cnf) {
            cnf.printStackTrace();
            throw new RuntimeException("GTK LaF must be supported");
        }
        frame = new JFrame("JFrame");
        frame.setSize(200, 200);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(DemandGTK::createAndShow);
        Robot robot = new Robot();
        robot.waitForIdle();
        robot.delay(1000);
        SwingUtilities.invokeAndWait( () -> {
            frame.setVisible(false);
            frame.dispose();
        });

    }
}

