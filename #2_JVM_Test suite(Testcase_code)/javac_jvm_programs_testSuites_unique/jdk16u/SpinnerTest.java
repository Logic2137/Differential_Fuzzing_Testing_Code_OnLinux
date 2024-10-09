


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SwingUtilities;
import java.awt.event.KeyEvent;
import java.awt.Point;
import java.awt.Robot;

public class SpinnerTest
{
    private static JFrame frame;
    private static JSpinner spinner;

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(100);
        try {
            SwingUtilities.invokeAndWait(() -> {
                
                frame = new JFrame("SpinnerDemo");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JPanel panel = new JPanel();
                String[] values = {"Month: ", "Year: ", null, "Date", "Sent"};

                SpinnerListModel listModel = new SpinnerListModel(values);

                JLabel l = new JLabel("Spinner");
                panel.add(l);

                spinner = new JSpinner(listModel);
                l.setLabelFor(spinner);
                panel.add(spinner);

                panel.setOpaque(true); 
                frame.setContentPane(panel);

                
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            });
            robot.waitForIdle();
            robot.delay(1000);
            Point loc = spinner.getLocationOnScreen();

            robot.mouseMove(loc.x, loc.y);
            robot.keyPress(KeyEvent.VK_SPACE);
            robot.keyRelease(KeyEvent.VK_SPACE);

        } finally {
            if (frame != null) {
                SwingUtilities.invokeAndWait(() -> frame.dispose());
            }
        }
    }
}
